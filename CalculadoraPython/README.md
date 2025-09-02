# Calculadora ANTLR4 en Python3

Este proyecto implementa una **calculadora avanzada** utilizando **ANTLR4** y **Python3**.  
La calculadora soporta variables, operaciones aritméticas básicas y funciones matemáticas avanzadas como `sin`, `cos`, `tan`, `sqrt`, `ln`, `log` y `factorial (!)`.

---

### Instalar ANTLR4 en Linux (ejemplo en Kali/Ubuntu)

```bash
sudo apt update
sudo apt install default-jdk -y
wget https://www.antlr.org/download/antlr-4.13.2-complete.jar -O /usr/local/lib/antlr-4.13.2-complete.jar
```
Configura los alias en tu ~/.bashrc o ~/.zshrc:
```bash
export CLASSPATH=".:/usr/local/lib/antlr-4.13.2-complete.jar:$CLASSPATH"
alias antlr4='java -jar /usr/local/lib/antlr-4.13.2-complete.jar'
alias grun='java org.antlr.v4.gui.TestRig'
```
#### Crear entorno virtual en Python
Para mantener limpio el sistema:
```bash

python3 -m venv venv
source venv/bin/activate
pip install antlr4-python3-runtime
```
Para salir del entorno virtual:
```bash
deactivate
```
### Generar el parser en Python
Dentro del directorio del proyecto:

```bash
antlr4 -Dlanguage=Python3 -visitor -no-listener LabeledExpr.g4
```
## Ejecutar la calculadora

Activa el entorno:
```bash
source venv/bin/activate
```
Corre la calculadora:
```bash
python Calc.py
```
## Ejemplo


<img width="670" height="387" alt="Screenshot From 2025-09-01 21-55-52" src="https://github.com/user-attachments/assets/54ec72b5-d71f-43e6-a3d7-df98612577bf" />




