# Calculadora con ANTLér v4 (Cap.4, LabeledExpr)

## **Guía  completa paso a paso (desde 0) — Kali / Debian / Ubuntu**

*Objetivo: preparar el entorno, generar la calculadora del capítulo 4 (gramática LabeledExpr.g4), compilarla, ejecutarla, y mostrar cómo ver el árbol sintáctico. También incluye solución de problemas comunes.*

1. Requisitos

- Sistema: Kali / Debian / Ubuntu.
- Conexión a Internet (para descargar ANTLR si no lo tienes).
- Permisos para usar sudo para instalar paquetes opcionales.

 -- Instalar Java
```bash
      sudo apt update
      sudo apt install default-jdk -y
      java -version    # verifica que exista
      javac -version   # verifica compilador
```

2.Descargar ANTLR (JAR)
Descarga el complete jar de ANTLR y colócalo en /usr/local/lib (ruta usada en esta guía).
```bash
sudo mkdir -p /usr/local/lib
sudo wget -O /usr/local/lib/antlr-4.13.1-complete.jar https://www.antlr.org/download/antlr-4.13.1-complete.jar
ls -l /usr/local/lib/antlr-4.13.1-complete.jar
```
3. Configurar alias / variables (opcional pero práctico)
Añade estas líneas a ~/.bashrc o ~/.zshrc (según tu shell). Si usas zsh, modifica ~/.zshrc.
```bash
# coloca en ~/.bashrc o ~/.zshrc
export ANTLR_JAR="/usr/local/lib/antlr-4.13.1-complete.jar"
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
4. Crear proyecto (archivos fuente)
Crea un directorio de trabajo y guarda los archivos.
```bash
# directorio del proyecto
mkdir -p ~/antlr-calc
cd ~/antlr-calc

# si tu JAR está en /usr/local/lib y se llama antlr-4.13.1-complete.jar:
ANTLR_JAR=/usr/local/lib/antlr-4.13.1-complete.jar

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

5. Generar código ANTLR (lexer, parser, visitor)
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

6 Error 
**UnsupportedClassVersionError: Calc has been compiled by a more recent version 
of the Java Runtime (class file version 65.0), this version ... up to 61.0**

Esto significa:

- Tu javac (compilador) es Java 21 (class file version 65.0).
- Tu java (runtime) es Java 17 (class file version 61.0).
- Es decir, compilaste con una versión más nueva que la que tienes para ejecutar.

SOLUCION
```bash
rm -f *.class
javac --release 17 -cp .:"$ANTLR_JAR" *.java
```
### Ejecucion 
sin interacturar
```bash
echo -e "a=5\nb=2\na+b*3\n(a+b)*3\n" | java -cp .:"$ANTLR_JAR" Calc
```
para ver el arbol
```bash
grun LabeledExpr prog -tree
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
