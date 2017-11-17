grammar FOOL;

@header {
	import java.util.HashMap;
	import ast.*;
}

@parser::members {
	private int nestingLevel = 0;
	// Array di tabelle dove l'indice dell'array è il livello sintattico, ossia il livello di scope, indice 0 = dichiarazioni globali, indice 1 = dichiarazioni locali (mappano identificatori con i valori)
	ArrayList<HashMap<String,STEntry>> symTable = new ArrayList<HashMap<String,STEntry>>();
	// Il livello dell'ambiente con dichiarazioni più esterne è 0 (nelle slide è 1); il fronte della lista di tabelle è "symTable.get(nestingLevel)"
}

@lexer::members {
	int lexicalErrors=0;
}


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

prog returns [Node ast]: e=exp {$ast = new ProgNode($e.ast);} SEMIC
	| LET
		{	HashMap<String,STEntry> hm = new HashMap<String,STEntry>();
			symTable.add(hm);
		}
	d=declist IN e=exp SEMIC
		{	$ast = new ProgLetInNode($d.astlist,$e.ast);
			symTable.remove(nestingLevel);
		};

// Lista di dichiarazioni (di variabili o funzioni). La chiusura "+" indica una o più volte.
declist	returns [ArrayList<Node> astlist]:
	{$astlist = new ArrayList<Node>();}	
	(
		(	VAR i=ID COLON t=type ASS e=exp
			{	VarNode v = new VarNode($i.text,$t.ast,$e.ast);
				$astlist.add(v);
				HashMap<String,STEntry> hm = symTable.get(nestingLevel);
				// Verificare che nello scope attuale (il fronte della tabella), la variabile sia già stata dichiarata. "put" sostituisce, ma se la chiave era già occupata restituisce la coppia vecchia, altrimenti null.
				if(hm.put($i.text, new STEntry(nestingLevel)) != null) {
					System.out.println("Var id" + $i.text + " at line " + $i.line + " already declared.");
					System.exit(0);
				};
			}
		| FUN i=ID COLON t=type 
			{	
				FunNode f = new FunNode($i.text,$t.ast);
				$astlist.add(f);
				HashMap<String,STEntry> hm = symTable.get(nestingLevel);
				// Verificare che nello scope attuale (il fronte della tabella), la funzione sia già stata dichiarata. "put" sostituisce, ma se la chiave era già occupata restituisce la coppia vecchia, altrimenti null.
				if(hm.put($i.text, new STEntry(nestingLevel)) != null) {
					System.out.println("Fun id" + $i.text + " at line " + $i.line + " already declared.");
					System.exit(0);
				};
				// Entro dentro un nuovo scope.
				nestingLevel++;
				HashMap<String,STEntry> hmn = new HashMap<String,STEntry>();
				symTable.add(hmn);
			}
			LPAR
				(i=ID COLON t=type
					{
						ParNode p1 = new ParNode($i.text,$t.ast);
						f.addPar(p1);
						if (hmn.put($i.text, new STEntry(nestingLevel)) != null) {
							//Errore identificatore (parametro) già dichiarato
							System.out.println("Par ID: " + $i.text + " at line " + $i.line + " already declared");
							System.exit(0);
						}
					}
				(COMMA i=ID COLON t=type
					{
						ParNode p2 = new ParNode($i.text,$t.ast);
						f.addPar(p2);
						if (hmn.put($i.text, new STEntry(nestingLevel)) != null){
							//Errore identificatore (parametro) già dichiarato
							System.out.println("Par ID: " + $i.text + " at line " + $i.line + " already declared");
							System.exit(0);
						}
					}
				)*
			)?
			RPAR
			(LET d=declist IN {f.addDec($d.astlist);})? e=exp
				{	f.addBody($e.ast);
					symTable.remove(nestingLevel--);
				}
		) SEMIC 
	)+;

type returns [Node ast]	: 
	INT	{$ast = new IntTypeNode();} // Rappresenta l'elemento sintattico del tipo int e non il valore.
	| BOOL	{$ast = new BoolTypeNode();}
	;

exp	returns [Node ast]: t=term {$ast = $t.ast;} (PLUS t=term {$ast = new PlusNode($ast,$t.ast);})* ;

term returns [Node ast]: f=factor {$ast = $f.ast;} (TIMES f=factor {$ast = new TimesNode($ast,$f.ast);})* ;

factor returns [Node ast] : v=value {$ast = $v.ast;} (EQ v=value {$ast = new EqNode($ast,$v.ast);})* ;

value returns [Node ast]	:
	i = INTEGER   {$ast = new IntNode(Integer.parseInt($i.text));}
	| TRUE      {$ast = new BoolNode(true);}
	| FALSE     {$ast = new BoolNode(false);}
	| LPAR e = exp RPAR {$ast = $e.ast;}  // Le parentesi lasciano l'albero inalterato.
	| IF e1 = exp THEN CLPAR e2 =exp CRPAR
		ELSE CLPAR e3 = exp CRPAR {$ast = new IfNode($e1.ast,$e2.ast,$e3.ast);}
	| PRINT LPAR e=exp RPAR	{$ast = new PrintNode($e.ast);}
	| i=ID // Identificatore di una variabile.
		{	// Cerco la dichiarazione dentro la symbol table
			int j = nestingLevel;
			STEntry entry = null;
			while(j>=0 && entry==null) {
				entry = symTable.get(j--).get($i.text);
			}
			if(entry==null) {
				System.out.println("Id" + $i.text + " at line " + $i.line + " not declared.");
				System.exit(0);
			}
			$ast = new IdNode($i.text, entry);
		} 
	;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
SEMIC	: ';' ;
COLON	: ':' ;
COMMA 	: ',' ;
EQ		: '==' ;
ASS		: '=' ;
PLUS	: '+' ;
TIMES	: '*' ;
INTEGER : ('-')?(('1'..'9')('0'..'9')*) | '0';
TRUE	: 'true' ;
FALSE	: 'false' ;
LPAR	: '(' ;
RPAR	: ')' ;
CLPAR 	: '{' ;
CRPAR	: '}' ;
IF		: 'if' ;
THEN	: 'then' ;
ELSE	: 'else' ;
PRINT	: 'print' ;
LET		: 'let' ;
IN		: 'in' ;
VAR		: 'var' ;
FUN		: 'fun' ;
INT		: 'int' ;
BOOL	: 'bool' ;

ID		: ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')* ;

WHITESP	: (' '|'\t'|'\n'|'\r')+ -> channel(HIDDEN) ;

COMMENT	: '/*' (.)*? '*/' -> channel(HIDDEN) ;

ERR		: . { System.out.println("Invalid char: "+ getText()); lexicalErrors++; } -> channel(HIDDEN);
