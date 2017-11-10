grammar SimpleExpLeft;

// Crea un campo nella classe del lexer che farà da contatore degli errori di sintassi
@lexer::members {
	int lexicalErrors = 0;
}

// PARSER RULES
prog	: e=exp { System.out.println("Parsing finished! Result is: " + $e.trans); }; /* Qui "e" è il risultato dell'espressione. */
//exp		: term exp2;
//exp2	: PLUS term exp2 |; /* epsilon */
exp returns [int trans]	: t=term {$trans=$t.trans;} (PLUS t=term {$trans+=$t.trans;} )*; /* Sostitutivo delle due righe prima ma in EBNF. Restituisce un intero di nome "trans" (serve per calcolare il valore delle espressioni); "$trans" è il valore di ritorno. */
//term	: value term2;
//term2	: TIMES value term2 |; /* epsilon */
term returns [int trans]	: v=value {$trans=$v.trans;} (TIMES v=value {$trans*=$v.trans;} )*; /* Sostitutivo delle due righe prima ma in EBNF. */
value returns [int trans]	: n=NUM {$trans=Integer.parseInt($n.text);} | LPAR e=exp {$trans=$e.trans;} RPAR;

// LEXER RULES
PLUS	: '+'; /* Quali sono i token? Per i simboli fissi di lunghezza unitaria è immediato. */
TIMES	: '*';
LPAR	: '(';
RPAR	: ')';
NUM		: '0' | ('1'..'9')('0'..'9')*; /* Espressione regolare. */
WHITESP	: (' '|'\n'|'\t'|'\r')+ -> channel(HIDDEN); /* I "whitespace" vanno riconosciuti ma non passati al parser. */
 /* Gestione dei commenti (C-like). In questo caso non vogliamo il maximal-match,
  * altrimenti in caso di commenti multipli vengono uniti. Con il "?" si rende "*" non greedy. */
COMMENT	: '/*' .*? '*/' -> channel(HIDDEN);
 /* Gli errori vanno in fondo, quindi con priorità di match minima. Il "." è il carattere wildcard.
  * Notare che, poiché si vuole che in caso di errori di compilazione si vada avanti per trovare eventuali errori successivi,
 * l'albero sintattico deve essere "corretto" in modo da proseguire.
 * Per la sintassi, lo fa ANTLR automaticamente; per il lessico, lo gestiamo noi. */
ERR		: . { System.out.println("Invalid char: " + getText()); lexicalErrors++; } -> channel(HIDDEN);