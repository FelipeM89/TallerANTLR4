grammar LabeledExpr;

prog: stat+ ;

stat
  : expr NEWLINE             # printExpr
  | ID '=' expr NEWLINE      # assign
  | NEWLINE                  # blank
  ;

expr
  : func=FUNC '(' expr ')'   # FuncExpr
  | expr '!'                 # Factorial
  | expr op=('*'|'/') expr   # MulDiv
  | expr op=('+'|'-') expr   # AddSub
  | INT                      # int
  | ID                       # id
  | '(' expr ')'             # parens
  ;

FUNC    : 'sin' | 'cos' | 'tan' | 'sqrt' | 'ln' | 'log' ;
ID      : [a-zA-Z]+ ;
INT     : [0-9]+ ;
NEWLINE : '\r'? '\n' ;
WS      : [ \t]+ -> skip ;
