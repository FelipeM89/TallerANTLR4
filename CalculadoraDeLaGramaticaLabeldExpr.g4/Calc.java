import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

public class Calc {
    public static void main(String[] args) throws IOException {
        // lee todo stdin
        CharStream input = CharStreams.fromStream(System.in);

        // lexer -> tokens -> parser
        LabeledExprLexer lexer = new LabeledExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LabeledExprParser parser = new LabeledExprParser(tokens);

        // regla inicial
        ParseTree tree = parser.prog();

        // visitar árbol
        EvalVisitor eval = new EvalVisitor();
        eval.visit(tree);
    }
}
