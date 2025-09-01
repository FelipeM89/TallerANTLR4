# Calculadora con ANTLér v4 (Cap.4, LabeledExpr)



*Objetivo: preparar el entorno, generar la calculadora del capítulo 4 (gramática LabeledExpr.g4), compilarla, ejecutarla, y mostrar cómo ver el árbol sintáctico. También incluye solución de problemas comunes.*

 -- Instalar Java
```bash
      sudo apt update
      sudo apt install default-jdk -y
      java -version    # verifica que exista
      javac -version   # verifica compilador
```

Descargar ANTLR (JAR)
Descarga el complete jar de ANTLR y colócalo en /usr/local/lib (ruta usada en esta guía).
```bash
sudo mkdir -p /usr/local/lib
sudo wget -O /usr/local/lib/antlr-4.13.2-complete.jar https://www.antlr.org/download/antlr-4.13.2-complete.jar
ls -l /usr/local/lib/antlr-4.13.2-complete.jar
```
Configurar alias / variables (opcional pero práctico)
Añade estas líneas a ~/.bashrc o ~/.zshrc (según tu shell). Si usas zsh, modifica ~/.zshrc.
```bash
# coloca en ~/.bashrc o ~/.zshrc
export ANTLR_JAR="/usr/local/lib/antlr-4.13.2-complete.jar"
export CLASSPATH=".:$ANTLR_JAR:$CLASSPATH"
alias antlr4='java -jar "$ANTLR_JAR"'
alias grun='java -cp "$ANTLR_JAR":. org.antlr.v4.gui.TestRig'
```
Luego recarga:
```bash
# si usas bash
source ~/.bashrc
# si usas zsh
source ~/.zshrc
```
Crear proyecto (archivos fuente)
Crea un directorio de trabajo y guarda los archivos.
```bash
# directorio del proyecto
mkdir -p ~/antlr-calc
cd ~/antlr-calc

# si tu JAR está en /usr/local/lib y se llama antlr-4.13.2-complete.jar:
ANTLR_JAR=/usr/local/lib/antlr-4.13.2-complete.jar

# verifica que exista
ls -l "$ANTLR_JAR"
# si no aparece, sustituye la ruta por la correcta

```

## LabeledExpr.g4 (gramática)
```antlr
cat > LabeledExpr.g4 <<'EOF'
grammar LabeledExpr;

prog: stat+ ;

stat
  : expr NEWLINE             # printExpr
  | ID '=' expr NEWLINE      # assign
  | NEWLINE                  # blank
  ;

expr
  : expr op=('*'|'/') expr   # MulDiv
  | expr op=('+'|'-') expr   # AddSub
  | INT                      # int
  | ID                       # id
  | '(' expr ')'             # parens
  ;

ID      : [a-zA-Z]+ ;
INT     : [0-9]+ ;
NEWLINE : '\r'? '\n' ;
WS      : [ \t]+ -> skip ;
EOF

```
**Guárdalo como LabeledExpr.g4.**
EvalVisitor.java (visitor mínimo, tal como en el tour)
```java
cat > EvalVisitor.java <<'EOF'
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
    public Integer visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getText().equals("*")) return left * right;
        return left / right; // división entera (como en el tour)
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
EOF

```
**Guárdalo como EvalVisitor.java.**

Calc.java (main)
```java
cat > Calc.java <<'EOF'
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
EOF

```
**Guárdalo como Calc.java.**

   Generar código ANTLR (lexer, parser, visitor)
   Desde ~/antlr-calc ejecuta:
```bash
   # usando alias antlr4 (si lo configuraste)
   antlr4 -visitor -no-listener LabeledExpr.g4

   # o explícito:
   java -jar "$ANTLR_JAR" -visitor -no-listener LabeledExpr.g4
```
Se generarán archivos como:

- LabeledExprLexer.java
- LabeledExprParser.java
- LabeledExprBaseVisitor.java
- LabeledExprVisitor.java
- y algunos .tokens / .interp.


### Ejecucion 
sin interacturar
```bash
echo -e "a=5\nb=2\na+b*3\n(a+b)*3\n" | java -cp .:"$ANTLR_JAR" Calc
```
para ver el arbol
```bash
alias grun='java -cp ".:/usr/local/lib/antlr-4.13.1-complete.jar" org.antlr.v4.gui.TestRig'
grun LabeledExpr prog -gui
```
modo interactivo 
```bash
java -cp .:"$ANTLR_JAR" Calc
```
## COMANDO UTILES 
```bash
# borrar .class y archivos generados por ANTLR
rm -f *.class
rm -f LabeledExpr*.java
# regenerar y compilar de nuevo
java -jar "$ANTLR_JAR" -visitor -no-listener LabeledExpr.g4
javac -cp .:"$ANTLR_JAR" *.java
```
## **Ejemplos**
<img width="465" height="169" alt="Screenshot From 2025-08-31 16-58-17" src="https://github.com/user-attachments/assets/1d2b9f8c-f3fd-4d80-b183-f3321ed97cee" />


---
<img width="778" height="584" alt="Screenshot From 2025-08-31 16-59-28" src="https://github.com/user-attachments/assets/23b4982f-cfc2-42b9-8c7d-9fa6a9756aca" />




