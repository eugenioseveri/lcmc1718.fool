grammar FOOL;

@header {
import ast.*;
}

@lexer::members {
int lexicalErrors=0;
}


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

prog returns [Node ast]	: e=exp {$ast=new ProgNode($e.ast);} SEMIC ;

exp	returns [Node ast]	: t=term {$ast=$t.ast;} (PLUS t=term {$ast=new PlusNode($ast,$t.ast);} )* ;

term returns [Node ast]	: f=factor {$ast=$f.ast;} (TIMES f=factor {$ast=new TimesNode($ast,$f.ast);} )* ;

factor returns [Node ast]	: v=value {$ast=$v.ast;} (EQ v=value {$ast=new EqNode($ast,$v.ast);} )* ;

value returns [Node ast]	:
    i=INTEGER {$ast=new IntNode(Integer.parseInt($i.text));}
	| TRUE {$ast=new BoolNode(true);}
	| FALSE {$ast=new BoolNode(false);}
	| LPAR e=exp RPAR {$ast=$e.ast;}
	| IF x=exp THEN CLPAR y=exp CRPAR
		   ELSE CLPAR z=exp CRPAR
		   {$ast=new IfNode($x.ast,$y.ast,$z.ast);}
	| PRINT LPAR e=exp RPAR {$ast=new PrintNode($e.ast);}
 	;


/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
SEMIC	: ';' ;
EQ		: '==' ;
PLUS	: '+' ;
TIMES	: '*' ;
INTEGER : ('-')?(('1'..'9')('0'..'9')*) | '0';
TRUE	: 'true' ;
FALSE	: 'false' ;
LPAR 	: '(' ;
RPAR	: ')' ;
CLPAR 	: '{' ;
CRPAR	: '}' ;
IF		: 'if' ;
THEN 	: 'then' ;
ELSE 	: 'else' ;
PRINT	: 'print' ;


WHITESP : (' '|'\t'|'\n'|'\r')+ -> channel(HIDDEN);
COMMENT : '/*' (.)*? '*/' -> channel(HIDDEN);

ERR	    : . { System.out.println("Invalid char: "+ getText()); lexicalErrors++; } -> channel(HIDDEN);
