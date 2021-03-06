package ast;

import lib.FOOLLib;

public class LessEqNode implements Node {

	private final Node left;
	private final Node right;

	public LessEqNode(final Node left, final Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "LessOrEqual\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Serve tenere conto dell'ereditarietà (singola)
		final Node l = this.left.typeCheck();
		final Node r = this.right.typeCheck();
		if (!(FOOLLib.isSubtype(l, r) || FOOLLib.isSubtype(r, l))) {
			System.out.println("Incompatible types in less or equal!");
			System.exit(0);
		}
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		final String l1 = FOOLLib.freshLabel();
		final String l2 = FOOLLib.freshLabel();
		/* Codice generato dal figlio di sinistra seguito dal codice generato dal sottoalbero di destra + operazione di "confronto".
		 * Verifico l'ugualianza tra i due valori utilizzando 'bleq':
		 * se il secondo valore è maggiore o uguale al primo allora salto a l1 e pusho 1 (TRUE), altrimenti non salto e pusho 0 (FALSE).
		 */
		return this.left.codeGeneration()
				+ this.right.codeGeneration()
				+ "bleq " + l1 + "\n" // Se la condizione è vera salto alla label1 e push 1 (true) altrimenti push 0 (false) e salto a label2
				+ "push 0\n"
				+ "b " + l2 + "\n"
				+ l1 + ": \n"
				+ "push 1\n"
				+ l2 + ": \n";
	}

	@Override
	public Node cloneNode() {
		return new LessEqNode(this.left.cloneNode(), this.right.cloneNode());
	}
}
