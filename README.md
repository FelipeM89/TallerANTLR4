# TallerANTLR4

## **¿Qué es ANTLR v4?**

ANTLR significa ANother Tool for Language Recognition.
Es una herramienta que sirve para leer, procesar y entender lenguajes (no solo lenguajes de programación, también expresiones, mini-lenguajes, archivos CSV, JSON, etc.).

En resumen:

- Tú escribes una gramática (reglas de tu lenguaje).

- ANTLR genera automáticamente un programa (lexer + parser).

- Ese programa puede leer texto y producir un árbol sintáctico (parse tree).

- Con un Visitor o Listener puedes recorrer el árbol y hacer algo útil (evaluar, compilar, transformar, validar, etc.).

## ¿Qué genera ANTLR?

A partir de un archivo .g4 (la gramática), ANTLR genera:

1. Lexer (tokenizador):

- Divide la entrada en pedacitos (tokens).

- Ejemplo: "a = 5 + 3" → ID(a), EQUAL(=), INT(5), PLUS(+), INT(3)

2. Parser:

- Usa los tokens y aplica las reglas gramaticales que escribiste.

- Construye un árbol sintáctico (parse tree).

3. Clases auxiliares:

- BaseVisitor o BaseListener → permiten recorrer el árbol.

- Interfaces → con métodos como visitAddSub, enterAssign, etc.
