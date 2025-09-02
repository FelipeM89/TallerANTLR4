grammar LabeledExpr;

prog: stat+ ;

stat
  : expr NEWLINE             # printExpr
  | ID '=' expr NEWLINE      # assign
  | NEWLINE                  # blank
  ;

expr
  : expr '!'                                 # Factorial
  | func=('sin'|'cos'|'tan'|'sind'|'cosd'|'tand'|'sqrt'|'ln'|'log') '(' expr ')'  # Function
  | expr op=('*'|'/') expr                   # MulDiv
  | expr op=('+'|'-') expr                   # AddSub
  | FLOAT                                    # float
  | INT                                      # int
  | ID                                       # id
  | '(' expr ')'                             # parens
  ;

ID      : [a-zA-Z]+ ;
FLOAT   : [0-9]+ '.' [0-9]* | '.' [0-9]+ ;
INT     : [0-9]+ ;
NEWLINE : '\r'? '\n' ;
WS      : [ \t]+ -> skip ;

