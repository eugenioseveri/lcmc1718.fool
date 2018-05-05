package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ast.*;

// Funzioni ausialiarie di libreria per FOOL.
public class FOOLLib {
	
	public static final int MEMSIZE = 10000;
	private static int labCount = 0; // Contatore utilizzato per generare etichette fresh
	private static int funLabCount = 0; //Come labCount, ma per le funzioni
	private static int methodLabCount = 0; //Come labCount, ma peeìr le funzioni
	private static String funCode = ""; // Stringa contentente i codici delle funzioni
	private static Map<String,String> superType = new HashMap<>(); // mappa le classi con le relative superclassi: classID -> superClassID
	private static List<List<String>> dispatchTables = new ArrayList<>(); // lista delle dispatchTable di ogni classe (solo nomi dei metodi)
	/**
	 * Funzione che definisce l'idea di sottotipo.
	 * Valuta se il tipo di "a" è <= al tipo di "b", dove "a" e "b" sono tipi di base (Int o Bool).
	 * @return True se "a" è sottotipo di "b", False altrimenti
	 */
	public static boolean isSubtype(Node a, Node b) {
		
		if (a instanceof ArrowTypeNode && b instanceof ArrowTypeNode) {
			/*--RELAZIONE DI CO-VARIANZA sul tipo di ritorno (cioè posso prendere un tipo di ritorno più specifico)--*/
			// Controllo che il tipo di ritorno sia l'uno il sottotipo dell'altro
			if (!isSubtype(((ArrowTypeNode) a).getRet(),((ArrowTypeNode) b).getRet())) {
				return false;
			}
			
			// Controllo che il numero dei parametri sia lo stesso
			if (((ArrowTypeNode) a).getParlist().size() != ((ArrowTypeNode) b).getParlist().size()) {
				return false;
			}
			
			/*--RELAZIONE DI CONTRO-VARIANZA sul tipo dei parametri (cioè posso prendere argomenti più generali)--*/
			// Vado a controllare che ogni coppia di parametri siano sottotipo l'uno dell'altro (parametro di b sottotipo di quello di a)
			for (int i=0; i< ((ArrowTypeNode) a).getParlist().size(); i++) {
				if(!isSubtype(((ArrowTypeNode) b).getParlist().get(i),((ArrowTypeNode) a).getParlist().get(i))) {
					return false;
				}
			}
			return true;
		} else if (a instanceof RefTypeNode && b instanceof RefTypeNode) { // se sono entrambi classi vado a vedere le relazioni nella mappa superType
			// se entrambi i ref type node contengono il riferimento alla stessa classe
			if (((RefTypeNode) a).getId().compareTo(((RefTypeNode) b).getId()) == 0) {
				return true;
			}
			String currentClass = ((RefTypeNode) a).getId();
			String superClass = null;
			do {
				superClass = superType.get(currentClass);
				if (superClass != null && superClass.compareTo(((RefTypeNode) b).getId()) == 0) {
					return true;
				}
				currentClass = superClass;
			} while (superClass != null);
			return false;
		} else if (a instanceof EmptyTypeNode && b instanceof RefTypeNode) { //se a è null allora è sottotipo di qualsiasi classe
			return true;
		} else if (a instanceof BoolTypeNode && b instanceof IntTypeNode) { // Caso sottotipo definisce che Bool è sottotipo di Int
			return true;
		} else if (a.getClass().equals(b.getClass())) { // Caso dell'uguaglianza
			return true;
		} else {
			return false;
		}
	}
	
	//Metodo per ottimizzazione OO e integrazione HO con OO
	public static Node lowestCommonAncestor(Node a, Node b) {
		
		if (a instanceof EmptyTypeNode)
			return b;

		if (b instanceof EmptyTypeNode)
			return a;
		
		// se sto lavorando con classi
		if (a instanceof RefTypeNode && b instanceof RefTypeNode) {
			
			RefTypeNode currentType = (RefTypeNode) a;
			
			do {
				// controllo che b non sia un sottotipo diretto della classe attualmente considerata
				if (FOOLLib.isSubtype(b, currentType)) {
					// nel caso in cui lo sia => ritorno la classe corrente a
					return currentType;
				}
				//Risalgo la gerarchia passando alla supercalsse della classe corrente a e con quello costruisco un nodo ClassTypeNode per il tipo della super classe 
				currentType = new RefTypeNode(superType.get(currentType.getId()));
				
			} while (currentType.getId() != null);
			
		} else if(a instanceof ArrowTypeNode && b instanceof ArrowTypeNode) {
			// se sto lavorando con tipo funzionali (con lo stesso numero di parametri)
			ArrowTypeNode typeA = (ArrowTypeNode) a;
			ArrowTypeNode typeB = (ArrowTypeNode) b;

			if (typeA.getParlist().size() != typeB.getParlist().size()) {
				return null;
			}
			
			// controllo il tipo di ritorno ricorsivamente (tipo di ritorno il risultato della chiamata ricorsiva (covarianza) )
			Node retType = FOOLLib.lowestCommonAncestor(typeA.getRet(), typeB.getRet());
			if (retType == null) {
				return null;
			}

			// Controllo i parametri (tipo di parametro i-esimo il tipo che è sottotipo dell'altro (controvarianza))
			ArrayList<Node> parList = new ArrayList<Node>();
			for (int i = 0; i < typeA.getParlist().size(); i++) {
				if (FOOLLib.isSubtype(typeA.getParlist().get(i), typeB.getParlist().get(i))) {
					parList.add(typeA.getParlist().get(i));
				} else if (FOOLLib.isSubtype(typeB.getParlist().get(i), typeA.getParlist().get(i))) {
					parList.add(typeB.getParlist().get(i));
				} else {
					// nessuno dei due i-esimi parametri è sottotipo dell'altro
					return null;
				}
			}
			// sono arrivato in fondo, ritorno il tipo lowestCommonAncestor
			return new ArrowTypeNode(parList, retType);
		} else {
			//per a e b tipi bool/int torna int se almeno uno è int, bool altrimenti
			if (a instanceof IntTypeNode || b instanceof IntTypeNode)
				return new IntTypeNode();
			else
				return new BoolTypeNode();
		}
		
		return null;
	}
	
	/**
	 * Ogni volta che è chiamato genera una nuova etichetta
	 * @return etichetta
	 */
	public static String freshLabel() {
		return "label" + labCount++;
	}
	
	/**
	 * Come freshLabel(), ma per le funzioni.
	 * @see #FOOLLib.freshLabel()
	 */
	// Come freshLabel, ma per le funzioni
	public static String freshFunLabel() {
		return "function" + funLabCount++;
	}
	/**
	 * Come freshLabel(), ma per i metodi.
	 * @see #FOOLLib.freshLabel()
	 */
	public static String freshMethodLabel() {
		return "method" + methodLabCount ++;
	}
	
	/**
	 * Inserisce il codice assembly di una funzione all'elenco di quelli già esistenti 
	 */
	public static void putCode(String code) {
		funCode += "\n" + code; // Viene aggiunta una linea vuota prima di ogni funzione (per leggibilità del codice)
	}
	
	/**
	 * Restituisce la stringa contenente i codici delle funzioni.
	 */
	public static String getCode() {
		return funCode;
	}
	
	public static void addSuperType(final String strClass, final String strSuperClass) {
		superType.put(strClass, strSuperClass);
	}
	
	public static void addDispatchTable(final List<String> dt) {
		dispatchTables.add(dt);
	}
	
	public static List<List<String>> getDispatchTable() {
		return dispatchTables;
	}

}
