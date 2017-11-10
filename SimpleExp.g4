grammar SimpleExp;

// Crea un campo nella classe del lexer che farà da contatore degli errori di sintassi
@lexer::members {
	int lexicalErrors = 0;
}

// PARSER RULES
/* A differenza della teoria, si usano lettere minuscole per non confonderle con i simboli.
 * Questa è la traduzione della grammatica nel testo dell'esercitazione.
 * Nota: in ANTLR "epsilon" si indica lasciando vuoto dopo "|".
 * "prog" è l'inizio. Inseriamo una stampa. */
prog	: exp { System.out.println("Parsing finished!"); };
//exp		: term exp2;
//exp2	: PLUS exp | /* epsilon */;
exp		: term (PLUS exp)?; /* Sostitutivo delle due righe prima ma in EBNF. */
// term	: value term2;
// term2	: TIMES term | /* epsilon */;
term	: value (TIMES term)?; /* Sostitutivo delle due righe prima ma in EBNF. */
value	: NUM | LPAR exp RPAR;

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