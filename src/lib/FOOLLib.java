package lib;

import ast.*;

// Funzioni ausialiarie di libreria per FOOL.
public class FOOLLib {
	
	// Valuta se il tipo di "a" è <= al tipo di "b", dove "a" e "b" sono tipi di base (int o bool).
	public static boolean isSubtype(Node a, Node b) {
		return a.getClass().equals(b.getClass()) || // Caso dell'uguaglianza
				(a instanceof BoolTypeNode && b instanceof IntTypeNode);
	}

}
