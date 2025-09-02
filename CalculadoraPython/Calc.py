import sys
from antlr4 import *
from LabeledExprLexer import LabeledExprLexer
from LabeledExprParser import LabeledExprParser
from EvalVisitor import EvalVisitor

def main():
    visitor = EvalVisitor()
    print("=== 🧮 Calculadora ANTLR en Python ===")
    print("Funciones: sin, cos, tan, sqrt, ln, log, factorial (!) ")
    print("Modo: grados por defecto. (usa radianes internamente si desactivas)")
    print("Escribe ENTER vacío para salir.\n")

    while True:
        try:
            line = input(">>> ")
            if line.strip() == "":
                break

            input_stream = InputStream(line + "\n")
            lexer = LabeledExprLexer(input_stream)
            stream = CommonTokenStream(lexer)
            parser = LabeledExprParser(stream)
            tree = parser.prog()

            visitor.visit(tree)

        except EOFError:
            break
        except Exception as e:
            print(f"❌ Error: {e}")

if __name__ == '__main__':
    main()
