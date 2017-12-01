package lib;

import ast.*;

// Funzioni ausialiarie di libreria per FOOL.
public class FOOLLib {
	
	private static int labCount = 0; // Contatore utilizzato per generare etichette fresh
	
	// Funzione che definisce l'idea di sottotipo - Valuta se il tipo di "a" è <= al tipo di "b", dove "a" e "b" sono tipi di base (Int o Bool).
	public static boolean isSubtype(Node a, Node b) {
		return a.getClass().equals(b.getClass()) || // Caso dell'uguaglianza
				(a instanceof BoolTypeNode && b instanceof IntTypeNode); // Caso sottotipo definisce che Bool è sottotipo di Int
	}
	
	// Ogni volta che è chiamato genera una nuova etichetta
	public static String freshLabel() {
		return "label" + labCount++;
	}

}
