import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends LabeledExprBaseVisitor<Integer> {
    // memoria variable -> valor
    Map<String,Integer> memory = new HashMap<>();

    // stat: expr NEWLINE  -> printExpr
    @Override
    public Integer visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr());
        System.out.println(value);
        return 0;
    }

    // stat: ID '=' expr NEWLINE -> assign
    @Override
    public Integer visitAssign(LabeledExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        int value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    // expr: INT -> int
    @Override
    public Integer visitInt(LabeledExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    // expr: ID -> id
    @Override
    public Integer visitId(LabeledExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if (memory.containsKey(id)) return memory.get(id);
        return 0; // por defecto 0 si no existe
    }

    // expr: '(' expr ')' -> parens
    @Override
    public Integer visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    // expr: expr ('*'|'/') expr -> MulDiv
@Override
    public Double visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));

        return switch (ctx.op.getType()) {
            case LabeledExprParser.MUL -> left * right;
            case LabeledExprParser.DIV -> {
                if (right == 0) {
                    System.err.println("Error: División por cero");
                    return null;
                }
                return left / right;
            }
            case LabeledExprParser.MOD -> left % right;
            case LabeledExprParser.POW -> Math.pow(left, right);
            default -> 0.0;
        };
    }

    // expr: expr ('+'|'-') expr -> AddSub
    @Override
    public Integer visitAddSub(LabeledExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getText().equals("+")) return left + right;
        return left - right;
    }
}
