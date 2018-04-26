package lib;

import java.util.HashMap;
import java.util.Map;

import ast.*;

// Funzioni ausialiarie di libreria per FOOL.
public class FOOLLib {
	
	private static int labCount = 0; // Contatore utilizzato per generare etichette fresh
	private static int funLabCount = 0; //Come labCount, ma per le funzioni
	private static String funCode = ""; // Stringa contentente i codici delle funzioni
	private static Map<String,String> superType = new HashMap<>(); // mappa le classi con le relative superclassi: classID -> superClassID
	
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
	
	/**
	 * Ogni volta che è chiamato genera una nuova etichetta
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

}
