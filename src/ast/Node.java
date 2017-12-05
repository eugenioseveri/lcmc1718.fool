package ast;

public interface Node {

	/**
	 * Fornisce una rappresentazione testuale del "Node" specificato, comprensiva di indentazione.
	 * @param indent L'indentazione da aggiungere all'elemento corrente.
	 */
	String toPrint(String indent);

	/**
	 * Fa il type checking.
	 * @return Per una espressione, il suo tipo (oggetto BoolTypeNode o IntTypeNode); per una dichiarazione, "null".
	 */
	Node typeCheck();

	/**
	 * Generazione del codice.
	 * @return Stringa contenente il codice assembly generato.
	 */
	String codeGeneration();
}