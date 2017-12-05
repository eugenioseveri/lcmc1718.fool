package lib;

import ast.*;

// Funzioni ausialiarie di libreria per FOOL.
public class FOOLLib {
	
	private static int labCount = 0; // Contatore utilizzato per generare etichette fresh
	private static int funLabCount = 0; //Come labCount, ma per le funzioni
	private static String funCode = ""; // Stringa contentente i codici delle funzioni
	
	/**
	 * Funzione che definisce l'idea di sottotipo.
	 * Valuta se il tipo di "a" è <= al tipo di "b", dove "a" e "b" sono tipi di base (Int o Bool).
	 * @return True se "a" è sottotipo di "b", False altrimenti
	 */
	public static boolean isSubtype(Node a, Node b) {
		return a.getClass().equals(b.getClass()) || // Caso dell'uguaglianza
				(a instanceof BoolTypeNode && b instanceof IntTypeNode); // Caso sottotipo definisce che Bool è sottotipo di Int
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

}
