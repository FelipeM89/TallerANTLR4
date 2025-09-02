import math
from LabeledExprVisitor import LabeledExprVisitor
from LabeledExprParser import LabeledExprParser

class EvalVisitor(LabeledExprVisitor):
    def __init__(self):
        super().__init__()
        self.memory = {}
        self.use_degrees = True  # ✅ podemos alternar entre radianes/grados

    def visitPrintExpr(self, ctx:LabeledExprParser.PrintExprContext):
        value = self.visit(ctx.expr())
        print(value)
        return value

    def visitAssign(self, ctx:LabeledExprParser.AssignContext):
        id = ctx.ID().getText()
        value = self.visit(ctx.expr())
        self.memory[id] = value
        return value

    def visitInt(self, ctx:LabeledExprParser.IntContext):
        return int(ctx.INT().getText())

    def visitId(self, ctx:LabeledExprParser.IdContext):
        return self.memory.get(ctx.ID().getText(), 0)

    def visitParens(self, ctx:LabeledExprParser.ParensContext):
        return self.visit(ctx.expr())

    def visitMulDiv(self, ctx:LabeledExprParser.MulDivContext):
        left = self.visit(ctx.expr(0))
        right = self.visit(ctx.expr(1))
        try:
            if ctx.op.text == "*":
                return left * right
            return left / right
        except ZeroDivisionError:
            print("❌ Error: división por cero")
            return float("nan")

    def visitAddSub(self, ctx:LabeledExprParser.AddSubContext):
        left = self.visit(ctx.expr(0))
        right = self.visit(ctx.expr(1))
        return left + right if ctx.op.text == "+" else left - right

    # ✅ Factorial
    def visitFactorial(self, ctx:LabeledExprParser.FactorialContext):
        value = self.visit(ctx.expr())
        if value < 0:
            print("❌ Error: factorial de número negativo")
            return float("nan")
        return math.factorial(value)

    # ✅ Funciones matemáticas
    def visitFuncExpr(self, ctx:LabeledExprParser.FuncExprContext):
        arg = self.visit(ctx.expr())
        func = ctx.func.text.lower()

        # Convertir a radianes si estamos en modo grados
        val = math.radians(arg) if self.use_degrees and func in ["sin", "cos", "tan"] else arg

        try:
            if func == "sin": return math.sin(val)
            if func == "cos": return math.cos(val)
            if func == "tan": return math.tan(val)
            if func == "sqrt":
                if arg < 0:
                    print("❌ Error: raíz cuadrada negativa")
                    return float("nan")
                return math.sqrt(arg)
            if func == "ln":
                if arg <= 0:
                    print("❌ Error: ln de número no positivo")
                    return float("nan")
                return math.log(arg)
            if func == "log":
                if arg <= 0:
                    print("❌ Error: log de número no positivo")
                    return float("nan")
                return math.log10(arg)
        except Exception as e:
            print(f"❌ Error en función {func}: {e}")
            return float("nan")

        print(f"❌ Función desconocida: {func}")
        return float("nan")
