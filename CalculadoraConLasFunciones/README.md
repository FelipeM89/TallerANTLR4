#  Calculadora ANTLR4 en Java

Este proyecto implementa una **calculadora** usando **ANTLR4** y **Java** basada en el libro *The Definitive ANTLR4 Reference*.  
Soporta:

- Operaciones aritméticas básicas: `+ - * /`
- Variables (`a=5`)
- Funciones matemáticas:
  - `sin(x), cos(x), tan(x)` → en radianes
  - `sind(x), cosd(x), tand(x)` → en grados
  - `sqrt(x)` raíz cuadrada
  - `ln(x)` logaritmo natural
  - `log(x)` logaritmo base 10
- Factorial: `n!`
- Manejo de errores: división por 0, raíz de negativo, logaritmos inválidos, factorial inválido, etc.

---

##  Instalación

1. **Clonar repositorio**  
   ```bash
   git clone https://github.com/tuusuario/tu-repo.git
   cd tu-repo
   ```
2. ****Instalar ANTLR4
Descarga el JAR de ANTLR4 (ejemplo con versión 4.13.2):
   ```bash
   curl -O https://www.antlr.org/download/antlr-4.13.2-complete.jar
   sudo mv antlr-4.13.1-complete.jar /usr/local/lib/
   ```
   Exporta variables (agrega a tu ~/.bashrc o ~/.zshrc):
 ```bash
 export ANTLR_JAR=/usr/local/lib/antlr-4.13.1-complete.jar
 alias antlr4='java -jar $ANTLR_JAR'
 alias grun='java org.antlr.v4.gui.TestRig'
```
3. Generar código con ANTLR
   ```bash
   antlr4 -visitor -no-listener LabeledExpr.g4
   ```
4. Compilar
   ```bash
   javac -cp .:/usr/local/lib/antlr-4.13.2-complete.jar *.java
   ```
5. Ejecución
   ```bash
   java -cp .:/usr/local/lib/antlr-4.13.2-complete.jar Calc 
   ```
## EJEMPLO

<img width="544" height="229" alt="Screenshot From 2025-09-01 20-53-46" src="https://github.com/user-attachments/assets/c9c2d7d8-a872-41df-9463-4f15f05cb90a" />

<img width="543" height="164" alt="Screenshot From 2025-09-01 20-53-59" src="https://github.com/user-attachments/assets/3aafce15-da3d-43b4-81d7-718bdf43af3c" />



   

