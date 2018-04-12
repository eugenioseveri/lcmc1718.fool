grammar FOOL;

@header {
	import java.util.Map;
	import java.util.HashMap;
	import java.util.ArrayList;
	import ast.*;
}

@parser::members {
	private int nestingLevel = 0;
	// Array di tabelle dove l'indice dell'array è il livello sintattico, ossia il livello di scope, indice 0 = dichiarazioni globali, indice 1 = dichiarazioni locali (mappano identificatori con i valori)
	ArrayList<HashMap<String,STEntry>> symTable = new ArrayList<HashMap<String,STEntry>>();
	// Il livello dell'ambiente con dichiarazioni più esterne è 0 (nelle slide è 1); il fronte della lista di tabelle è "symTable.get(nestingLevel)"
	
	HashMap<String,HashMap<String,STEntry>> classTable = new HashMap<String,HashMap<String,STEntry>>();
}

@lexer::members {
	int lexicalErrors=0;
}


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

prog returns [Node ast]:
	{	
		HashMap<String,STEntry> hm = new HashMap<> ();
		symTable.add(hm);
	}
	( e=exp {$ast = new ProgNode($e.ast);} //Caso in cui non ho dichiarazioni, quindi un programma semplice  
	| LET ( c=cllist (d=declist)? 
        	| d=declist
            ) IN e=exp 
		{	$ast = new ProgLetInNode($d.astlist,$e.ast); //TODO aggiungere c.astlist nel costruttore
			//non gli passo solo exp ma anche declist che sarà un albero di nodi (parseTree), cioè tutte le vecchie dichiarazioni fatte. Stessa cosa per cllist, gli passo tutte le dichiarazioni di classi fatte.
		} 
	) {	symTable.remove(nestingLevel); } SEMIC ;

 
//gestione della dichiarazione delle classi per semplicità le classi sono contenute solamente nell'ambiente globale (nesting level 0)
cllist returns [List<Node> astlist]:
	{	$astlist = new ArrayList<Node>();
		int offset = -2; // Indice di convenzione di inizio (che viene decrementato)
	}
 	( CLASS i=ID
 		{
 			Map<String,STEntry> hm = symTable.get(nestingLevel); // Tabella del livello corrente (detta tabella del fronte o hash table dell'ambiente dove sto parsando)
 			// verificare se esiste già la classe e dare errore di conseguenza
 			if (classTable.get($i.text) != null){
 				// Errore identificatore (classe) già dichiarata
				System.out.println("Class id: " + $i.text + " at line " + $i.line + " already declared.");
				System.exit(0);
 			}
 			List<Node> fieldsList = new ArrayList<>(); // Lista dei campi vuota
 			List<Node> methodsList = new ArrayList<>(); // Lista dei metodi vuota
 			
         	ClassTypeNode classType = new ClassTypeNode(fieldsList,methodsList);
         	
 			//TODO dichiarare Entry della superclasse a null
 			
 			STEntry entry = new STEntry(nestingLevel,classType,offset--); //STEntry della classe da inserire al livello 0 della symbolTable
 			hm.put($i.text,entry); //inserisco nella hashMap di livello 0 la classe appena dichiarata
 			
 			//creare la superclass entry a null anche se non estendo nessuna classe
 			
 		}
 		(EXTENDS ei=ID
 		{
 			//verificare che esista la classe padre se non la trovo termino il PARSER
 			if (classTable.get($ei.text) == null){
 				// Errore identificatore (classe) già dichiarata
				System.out.println("Class id: " + $i.text + " at line " + $i.line + " not declared.");
				System.exit(0);
 			}
 			//TODO clonare il ClassTypeNode della classe da cui si eredita
 			//TODO clonare la virtualTable della classe da cui si eredita
 			
 			//prelevare Entry della super classe
 			
 			// Clono la STEntry della superclass ed estenderla
 		}
 		)? 
 		{  			
 			
 			ClassNode c = new ClassNode($i.text,entry,fieldsList,methodsList); //con l'ereditarietà superclasse
 			$astlist.add(c);
 			
 			// Entro dentro un nuovo scope.
			nestingLevel++;  // Aumento il livello perchè sono all'interno di una classe (anche i parametri passati alla funzione rientrano nel livello interno)
			HashMap<String,STEntry> virtualTable = new HashMap<>(); //VirtualTable vuota
         	classTable.put($i.text,virtualTable);
			symTable.add(virtualTable);
 		}
 		LPAR { int fieldOffset = -1; //offset iniziale dei campi di una classe (offset che aumenta verso il basso)
 			}
 			(c1=ID COLON t1=type { 
 				//aggiunto il campo nella lista e di conseguenza viene aggiornata la ClassTypeNode per riferimento
 				fieldsList.add(new FieldNode($c1.text,$t1.ast));
 				//TODO CASO EXTENDS: fare controllo se il campo esiste già... Non deve dare errore ma sovrascriverlo (preservando l'offset)
 				virtualTable.put($c1.text, new STEntry(nestingLevel,$t1.ast,fieldOffset--));
 				
 			} (COMMA c2=ID COLON t2=type {
 			
 				//aggiunto il campo nella lista e di conseguenza viene aggiornata la ClassTypeNode per riferimento
 				fieldsList.add(new FieldNode($c2.text,$t2.ast));
 				//TODO CASO EXTENDS: fare controllo se il campo esiste già... Non deve dare errore ma sovrascriverlo (preservando l'offset)
 				virtualTable.put($c2.text, new STEntry(nestingLevel,$t2.ast,fieldOffset--));
 			}
 			)* )? RPAR
 				// inizia la dichiarazione e definizione dei metodi   
              CLPAR { 
              	int methodOffset = 0; //offset iniziale dei metodi di una classe (offset che aumenta verso l'alto)
            	List<Node> parList = new ArrayList<>();
            	List<Node> decList = new ArrayList<>(); 		
 			}
                 ( FUN m1=ID COLON mt1=type {
                 	//aggiunto il metodo nella lista e di conseguenza viene aggiornata la ClassTypeNode per riferimento
                 	MethodNode methodNode = new MethodNode($m1.text,$mt1.ast,parList,decList);
                 	methodsList.add(methodNode);
                 	STEntry methodEntry = new STEntry(nestingLevel,$mt1.ast,methodOffset++,true);
                 	//methodOffset += 2; //TODO verificare -> Il layout higher-order occupa due posti invece di uno anche per i metodi HO all'interno di una classe
                 	//TODO CASO EXTENDS: fare controllo se il metodo esiste già... Non deve dare errore ma sovrascriverlo (preservando l'offset)
                 	virtualTable.put($m1.text,methodEntry);
                 	
                 	// Entro dentro un nuovo scope.
					nestingLevel++;  // Aumento il livello perchè sono all'interno di un metodo (anche i parametri passati al metodo rientrano nel livello interno)
					HashMap<String,STEntry> hmn = new HashMap<String,STEntry>();
					symTable.add(hmn);
					
					// per quanto riguarda il layout dei metodi devo rifarmi a quello delle funzioni: i parametri iniziano dall'offset 1 e vado ad incremento
       				int parOffset = 1; //i parametri iniziano da 1 nel layout e l'offset si incrementa (quindi da 1 in su)

       				// le dichiarazioni da -2 e decremento
       				int varOffset = -2;
                	
                 } LPAR {
                 	ArrayList<Node> parTypes = new ArrayList<>();
                 } (p1=ID COLON pt1=hotype {
                 		parTypes.add($pt1.ast);
                 		ParNode p1 = new ParNode($p1.text,$pt1.ast);
                 		parList.add(p1);
                 		
                 		//controllo se il parametro è di tipo funzionale (in tal caso si devono riservare due spazi)
                 		if ($pt1.ast instanceof ArrowTypeNode) {
                 			parOffset++;
                 		}
                 		
                 		if (hmn.put($p1.text, new STEntry(nestingLevel,$pt1.ast,parOffset++)) != null) {
							// Errore identificatore (parametro) già dichiarato
							System.out.println("Par ID: " + $p1.text + " at line " + $p1.line + " already declared");
							System.exit(0);
						}
                 		
                 	}
                 	(COMMA p2=ID COLON pt2=hotype {
                 		parTypes.add($pt2.ast);
                 		ParNode p2 = new ParNode($p2.text,$pt2.ast);
                 		parList.add(p2);
                 		
                 		//controllo se il parametro è di tipo funzionale (in tal caso si devono riservare due spazi)
                 		if ($pt2.ast instanceof ArrowTypeNode) {
                 			parOffset++;
                 		}
                 		
                 		//inserisco il parametro nella mappa della symboltable corrispondete al nestingLevel che stiamo parsando
                 		if (hmn.put($p2.text, new STEntry(nestingLevel,$pt2.ast,parOffset++)) != null) {
							// Errore identificatore (parametro) già dichiarato
							System.out.println("Par ID: " + $p2.text + " at line " + $p2.line + " already declared");
							System.exit(0);
						}
                 		
                 	})* )? RPAR {
                 		// ora è possibile istanziare il nodo che rappresenta il tipo della funzione
						ArrowTypeNode functionType = new ArrowTypeNode(parTypes,$mt1.ast);
						methodEntry.addType(functionType);
						// aggiungo il tipo anche al MethodNode
						methodNode.setSymType(functionType); 
                 	}
	                (LET (VAR v1=ID COLON vt1=type ASS e1=exp SEMIC {
	                		VarNode v = new VarNode($v1.text,$vt1.ast,$e1.ast);
	                		decList.add(v);
	                		
	                		if (hmn.put($v1.text, new STEntry(nestingLevel,$vt1.ast,varOffset--)) != null) { //TODO controllare se l'offset è corretto
								// Errore identificatore (parametro) già dichiarato
								System.out.println("Var ID: " + $v1.text + " at line " + $v1.line + " already declared");
								System.exit(0);
							}
	                		
	                	})* IN)? e2=exp {
	                		methodNode.addBody($e2.ast);
	                	}
        	       SEMIC { symTable.remove(nestingLevel--); }  //è finito lo scope del metodo quindo posso rimuovere la symboltable corrispondente e decrementare il nestinglevel, quindi elimino il contesto del metodo
        	     )*
        	    // Ora devo ricordarmi di chiudere il livello interno della classe (livello virtual table) torno a lv.0
  				//A livello zero della symTable ricordo che ho tutti i nomi delle classi, con relativa STEntry, in questo modo le tengo in vita                 
				CRPAR {symTable.remove(nestingLevel--);}
          )+ ; 

// Lista di dichiarazioni (di variabili o funzioni). La chiusura "+" indica una o più volte.
declist	returns [ArrayList<Node> astlist]:
	{	$astlist = new ArrayList<Node>();
		int offset = -2; // Indice di convenzione di inizio (che viene decrementato)
	}
	(
		(	VAR i=ID COLON t=hotype ASS e=exp
			{	VarNode v = new VarNode($i.text,$t.ast,$e.ast);
				$astlist.add(v);
				// ora che ho dichiarato la var la aggiungo alla symbol table
				HashMap<String,STEntry> hm = symTable.get(nestingLevel); // Tabella del livello corrente (detta tabella del fronte o hash table dell'ambiente dove sto parsando)
				// Verificare che nello scope attuale (il fronte della tabella), la variabile sia già stata dichiarata. "put" sostituisce, ma se la chiave era già occupata restituisce la coppia vecchia, altrimenti null.
				if(hm.put($i.text, new STEntry(nestingLevel,$t.ast,offset--)) != null) {
					// Errore identificatore (variabile) già dichiarata
					System.out.println("Var id: " + $i.text + " at line " + $i.line + " already declared.");
					System.exit(0);
				};
			}
		| FUN i=ID COLON t=hotype //TODO qui ci va type invece di hotype ma genera errore...
			{	
				FunNode f = new FunNode($i.text,$t.ast);
				$astlist.add(f);
				HashMap<String,STEntry> hm = symTable.get(nestingLevel);
				// Verificare che nello scope attuale (il fronte della tabella), la funzione sia già stata dichiarata. "put" sostituisce, ma se la chiave era già occupata restituisce la coppia vecchia, altrimenti null.
				STEntry entry = new STEntry(nestingLevel,offset);
				offset -= 2; // Il layout higher-order occupa due posti invece di uno
				// inserisco l'ID della funzione nella symbol table 
				if(hm.put($i.text, entry) != null) {
					System.out.println("Fun id: " + $i.text + " at line " + $i.line + " already declared.");
					System.exit(0);
				};
				// Entro dentro un nuovo scope.
				nestingLevel++;  // Aumento il livello perchè sono all'interno di una funzione (anche i parametri passati alla funzione rientrano nel livello interno)
				HashMap<String,STEntry> hmn = new HashMap<String,STEntry>();
				symTable.add(hmn);
			}
			LPAR {	ArrayList<Node> parTypes = new ArrayList<Node>();
					int parOffset = 1; //i parametri iniziano da 1 nel layout e l'offset si incrementa (quindi da 1 in su)
				}
				(i=ID COLON fty=hotype
					{ // Creare il ParNode, lo attacco al FunNode invocando addPar, aggiungo una STentry alla hashmap hmn
						parTypes.add($fty.ast);
						ParNode p1 = new ParNode($i.text,$fty.ast);
						f.addPar(p1);
						
						 // nel caso in cui sia presente qualche parametro di tipo funzionale devo riservare due spazi.
						if ($fty.ast instanceof ArrowTypeNode) {
                    		parOffset++;
                  		}
						if (hmn.put($i.text, new STEntry(nestingLevel,$fty.ast,parOffset++)) != null) {
							// Errore identificatore (parametro) già dichiarato
							System.out.println("Par ID: " + $i.text + " at line " + $i.line + " already declared");
							System.exit(0);
						}
					}
				(COMMA i=ID COLON ty=hotype
					{// Creare il ParNode, lo attacco al FunNode invocando addPar, aggiungo una STentry alla hashmap hmn
						parTypes.add($ty.ast);
						ParNode p2 = new ParNode($i.text,$ty.ast);
						f.addPar(p2);
						
						 // nel caso in cui sia presente qualche parametro di tipo funzionale devo riservare due spazi.
						if ($ty.ast instanceof ArrowTypeNode) {
                      		parOffset++;
                    	}
						if (hmn.put($i.text, new STEntry(nestingLevel,$ty.ast,parOffset++)) != null){
							//Errore identificatore (parametro) già dichiarato
							System.out.println("Par ID: " + $i.text + " at line " + $i.line + " already declared");
							System.exit(0);
						}
					}
				)*
			)?
			RPAR { 
				// ora è possibile istanziare il nodo che rappresenta il tipo della funzione
				ArrowTypeNode functionType = new ArrowTypeNode(parTypes,$t.ast);
				entry.addType(functionType);
				// aggiungo il tipo anche al FunNode
				f.setSymType(functionType); }
			(LET d=declist IN {f.addDec($d.astlist);})? e=exp
				{	f.addBody($e.ast);
					symTable.remove(nestingLevel--); // Diminuisco nestingLevel perchè esco dallo scope della funzione
				}
		) SEMIC 
	)+ ;

hotype returns [Node ast] :
	t=type {$ast=$t.ast;}
	| a=arrow {$ast=$a.ast;}
	;

type returns [Node ast]	:
	INT	{$ast = new IntTypeNode();} // Rappresenta l'elemento sintattico del tipo int e non il valore; invece IntNode è il nodo con il valore intero
	| BOOL	{$ast = new BoolTypeNode();} //Lo stesso ragionamento fatto sopra
	| i=ID  {$ast = new RefTypeNode($i.text);} //Riferimento ad un oggetto (instanza di una classe)
	;
	
arrow returns [Node ast] :
	{
		//lista dei parametri
		ArrayList<Node> parList = new ArrayList<Node>();
	}
	LPAR (h=hotype {parList.add($h.ast);}
			(COMMA h=hotype {parList.add($h.ast);})*
		)? RPAR ARROW t=type {$ast = new ArrowTypeNode(parList,$t.ast);};

exp	returns [Node ast]:
	t=term {$ast = $t.ast;}
	(PLUS t=term {$ast = new PlusNode($ast,$t.ast);}
	| MINUS t=term {$ast = new MinusNode($ast, $t.ast);}
	| OR t=term {$ast = new OrNode($ast, $t.ast);}
	)* ;

term returns [Node ast]:
	f=factor {$ast = $f.ast;}
	(TIMES f=factor {$ast = new TimesNode($ast,$f.ast);}
	| DIV f=factor {$ast = new DivNode($ast, $f.ast);}
	| AND f=factor {$ast = new AndNode($ast, $f.ast);}
	)* ;

factor returns [Node ast]:
	v=value {$ast = $v.ast;}
	(EQ v=value {$ast = new EqNode($ast,$v.ast);}
	| GE v=value {$ast = new GreaterEqNode($ast,$v.ast);}
	| LE v=value {$ast = new LessEqNode($ast,$v.ast);}
	)* ;

value returns [Node ast]:
	i = INTEGER	{$ast = new IntNode(Integer.parseInt($i.text));}
	| TRUE		{$ast = new BoolNode(true);}
	| FALSE		{$ast = new BoolNode(false);}
	| NULL		{$ast = new EmptyNode();}    
	| NEW i=ID LPAR { 
		
			//controllare se esiste la classe che si vuole istanziare
			if (classTable.get($i.text) == null) {
				System.out.println("Class " + $i.text + " at line " + $i.line + " not declared.");
				System.exit(0);
			}
			//Prelevo la STEntry (che contiene ClassTypeNode) della classe dalla symbol table a nestinglevel 0 
			STEntry classEntry = symTable.get(0).get($i.text);
			
			List<Node> argList = new ArrayList<>(); //Lista degli argomenti passati al NewNode
		}
		(e1=exp { argList.add($e1.ast); }
			(COMMA e2=exp {argList.add($e2.ast); } )* 
		)? RPAR {$ast = new NewNode($i.text,classEntry,argList);}
	| LPAR e = exp RPAR {$ast = $e.ast;}  // Le parentesi lasciano l'albero inalterato.
	| IF e1 = exp THEN CLPAR e2 =exp CRPAR
		ELSE CLPAR e3 = exp CRPAR {$ast = new IfNode($e1.ast,$e2.ast,$e3.ast);}
	| NOT LPAR e=exp RPAR {$ast = new NotNode($e.ast);}
	| PRINT LPAR e=exp RPAR	{$ast = new PrintNode($e.ast);}
	| i=ID // Identificatore di una variabile o funzione. Combinazioni possibili ID (variabile,classe) 
		{	// Cerco la dichiarazione dentro la symbol table e il livello di scope corrente fino allo scope globale (level = 0)
			int j = nestingLevel;
			STEntry entry = null;
			while(j>=0 && entry==null) {
				entry = symTable.get(j--).get($i.text);
			}
			if(entry==null) { // Dichiarazione non presente nella symbol table quindi variabile non dichiarata
				System.out.println("Id " + $i.text + " at line " + $i.line + " not declared.");
				System.exit(0);
			}
			$ast = new IdNode($i.text, entry, nestingLevel); // Inserito il nestinglevel per verifiche sullo scope della variabile
		}
		// Supporto alle chiamate a funzioni. Combinazioni possibili ID() (funzione vuota) - ID(exp) (funzione con variabili)
		( LPAR { ArrayList<Node> argList = new ArrayList<Node>(); }
			( a=exp { argList.add($a.ast); } //tutte volte che incontro un'espressione l'aggiungo alla lista dei parametri
				(COMMA a=exp { argList.add($a.ast); }
				)*
			)? RPAR { $ast = new CallNode($i.text,entry,argList,nestingLevel); } // Inserito il nestinglevel per verifiche sullo scope della funzione chiamata
		| DOT i2=ID LPAR {
			
			//controllare se esiste la classe da istanziare
			Map<String,STEntry> classEntry = classTable.get($i.text);
			if (classEntry == null) {
				System.out.println("Class " + $i.text + " at line " + $i.line + " not declared.");
				System.exit(0);
			}
			
			STEntry methodEntry = classEntry.get($i2.text);
			//controllo se il metodo esiste (verifico l'esistenza)
			if (methodEntry == null){
				System.out.println("Method " + $i.text + " at line " + $i.line + " not declared.");
				System.exit(0);
			} else if (!methodEntry.isMethod()){ //controllo se l'id è un metodo)
				System.out.println($i.text + " at line " + $i.line + " is not a method.");
				System.exit(0);
			}
			
			//TODO serve controllare se la classe è già stata istanziata?
				
			List<Node> argList = new ArrayList<>(); //Lista degli argomenti passati al ClassCallNode
			}(e1=exp {argList.add($e1.ast);} 
				(COMMA e2=exp {argList.add($e2.ast);})*
			)? RPAR {$ast = new ClassCallNode($i.text,$i2.text,entry,methodEntry,argList,nestingLevel);}
			
		)? ;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/
SEMIC	: ';' ;
COLON	: ':' ;
COMMA 	: ',' ;
DOT		: '.' ;
EQ		: '==' ;
GE		: '>=' ;
LE		: '<=' ;
NOT		: '!' ;
AND		: '&&' ;
OR		: '||' ;
ASS		: '=' ;
PLUS	: '+' ;
MINUS	: '-' ;
TIMES	: '*' ;
DIV		: '/' ;
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
CLASS	: 'class' ;
EXTENDS	: 'extends' ;
NEW		: 'new' ;
NULL	: 'null' ;
INT		: 'int' ;
BOOL	: 'bool' ;
ARROW	: '->' ;

INTEGER : ('-')?(('1'..'9')('0'..'9')*) | '0' ;
ID		: ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')* ;
WHITESP	: (' '|'\t'|'\n'|'\r')+ -> channel(HIDDEN) ;
COMMENT	: '/*' (.)*? '*/' -> channel(HIDDEN) ;
ERR		: . { System.out.println("Invalid char: "+ getText()); lexicalErrors++; } -> channel(HIDDEN);
