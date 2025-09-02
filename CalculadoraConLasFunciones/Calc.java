import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Calc {
    public static void main(String[] args) throws IOException {
        
        InputStream is = System.in;
        if (args.length > 0) {
            is = new FileInputStream(args[0]);
        }

        CharStream input = CharStreams.fromStream(is);

        LabeledExprLexer lexer = new LabeledExprLexer(input);
        
        lexer.removeErrorListeners();
        lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        LabeledExprParser parser = new LabeledExprParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);


        ParseTree tree = parser.prog();

        EvalVisitor eval = new EvalVisitor();
        eval.visit(tree);
    }

    // Listener para mensajes de error más claros
    static class DescriptiveErrorListener extends BaseErrorListener {
        static final DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e)
        {
            System.err.printf("Error de sintaxis en línea %d:%d: %s%n", line, charPositionInLine, msg);
        }
    }
}

