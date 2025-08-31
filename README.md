# TallerANTLR4

## **¿Qué es ANTLR v4?**

ANTLR significa ANother Tool for Language Recognition.
Es una herramienta que sirve para leer, procesar y entender lenguajes (no solo lenguajes de programación, también expresiones, mini-lenguajes, archivos CSV, JSON, etc.).

En resumen:

- Tú escribes una gramática (reglas de tu lenguaje).

- ANTLR genera automáticamente un programa (lexer + parser).

- Ese programa puede leer texto y producir un árbol sintáctico (parse tree).

- Con un Visitor o Listener puedes recorrer el árbol y hacer algo útil (evaluar, compilar, transformar, validar, etc.).
