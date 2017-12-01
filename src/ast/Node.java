package ast;

public interface Node {

	String toPrint(String indent);

	/*
	 * Fa il type checking e ritorna: - per una espressione, il suo tipo
	 * (oggetto BoolTypeNode o IntTypeNode); - per una dichiarazione, "null".
	 */
	Node typeCheck();

	/*
	 * Generazione del codice.
	 */
	String codeGeneration();
}