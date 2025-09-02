import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends LabeledExprBaseVisitor<Double> {
    // memoria: variable -> valor
    Map<String, Double> memory = new HashMap<>();

    private static final double EPS = 1e-9;

    // -------- utils ----------
    private static boolean isInteger(double x) {
        return Math.abs(x - Math.rint(x)) < EPS;
    }

    private static boolean isZero(double x) {
        return Math.abs(x) < EPS;
    }

    private static double toRadians(double deg) {
        return Math.toRadians(deg);
    }

    // -------- statements ----------
    @Override
    public Double visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        Double value = visit(ctx.expr());
        System.out.println(value);
        return 0.0;
    }

    @Override
    public Double visitAssign(LabeledExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        Double value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    // -------- primarios ----------
    @Override
    public Double visitInt(LabeledExprParser.IntContext ctx) {
        return Double.valueOf(ctx.INT().getText());
    }

    @Override
    public Double visitFloat(LabeledExprParser.FloatContext ctx) {
        return Double.valueOf(ctx.FLOAT().getText());
    }

    @Override
    public Double visitId(LabeledExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        return memory.getOrDefault(id, 0.0);
    }

    @Override
    public Double visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    // -------- binarios ----------
    @Override
    public Double visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));
        String op = ctx.op.getText();

        if (op.equals("*")) {
            return left * right;
        } else {
            if (isZero(right)) {
                System.err.println("Error: división por cero");
                return Double.NaN;
            }
            return left / right;
        }
    }

    @Override
    public Double visitAddSub(LabeledExprParser.AddSubContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));
        String op = ctx.op.getText();
        return op.equals("+") ? left + right : left - right;
    }

    // -------- factorial ----------
    @Override
    public Double visitFactorial(LabeledExprParser.FactorialContext ctx) {
        double v = visit(ctx.expr());
        if (!isInteger(v) || v < 0) {
            System.err.println("Error: factorial definido solo para enteros no negativos");
            return Double.NaN;
        }
        int n = (int) Math.rint(v);
        // para evitar overflow de double hacia Infinity, limitamos a 170! aprox
        if (n > 170) {
            System.err.println("Error: factorial demasiado grande (n <= 170)");
            return Double.NaN;
        }
        double acc = 1.0;
        for (int i = 2; i <= n; i++) acc *= i;
        return acc;
    }

    // -------- funciones ----------
    @Override
    public Double visitFunction(LabeledExprParser.FunctionContext ctx) {
        String f = ctx.func.getText();  // sin, cos, tan, sind, cosd, tand, sqrt, ln, log
        double x = visit(ctx.expr());

        switch (f) {
            // radianes
            case "sin":  return Math.sin(x);
            case "cos":  return Math.cos(x);
            case "tan":
                // control de verticales (tan ~ infinito)
                if (Double.isInfinite(Math.tan(x)) || Math.abs(Math.cos(x)) < EPS) {
                    System.err.println("Error: tan(x) indefinida para este valor (radianes)");
                    return Double.NaN;
                }
                return Math.tan(x);

            // grados
            case "sind":
                return Math.sin(toRadians(x));
            case "cosd":
                return Math.cos(toRadians(x));
            case "tand":
                double c = Math.cos(toRadians(x));
                if (Math.abs(c) < EPS) {
                    System.err.println("Error: tan(x) indefinida para este valor (grados)");
                    return Double.NaN;
                }
                return Math.tan(toRadians(x));

            case "sqrt":
                if (x < 0) {
                    System.err.println("Error: sqrt(x) requiere x >= 0");
                    return Double.NaN;
                }
                return Math.sqrt(x);

            case "ln":
                if (x <= 0) {
                    System.err.println("Error: ln(x) requiere x > 0");
                    return Double.NaN;
                }
                return Math.log(x);

            case "log":
                if (x <= 0) {
                    System.err.println("Error: log10(x) requiere x > 0");
                    return Double.NaN;
                }
                return Math.log10(x);

            default:
                System.err.println("Error: función no soportada: " + f);
                return Double.NaN;
        }
    }
}

