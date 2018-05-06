package ast;

import lib.FOOLLib;

public class EqNode implements Node {

	private Node left;
	private Node right;

	public EqNode(final Node left, final Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "Equal\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Serve tenere conto dell'ereditarietà (singola)
		Node l = this.left.typeCheck();
		Node r = this.right.typeCheck();
		if (l instanceof ArrowTypeNode || r instanceof ArrowTypeNode) {
			System.out.println("Cannot compare functions!");
			System.exit(0);
		}
		if (!(FOOLLib.isSubtype(l, r) || FOOLLib.isSubtype(r, l))) {
			System.out.println("Incompatible types in equal!");
			System.exit(0);
		}
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		final String l1 = FOOLLib.freshLabel();
		final String l2 = FOOLLib.freshLabel();
		// Codice generato dal figlio di sinistra seguito dal codice generato dal sottoalbero di destra + operazione di "confronto"
		return this.left.codeGeneration()
				+ this.right.codeGeneration()
				+ "beq " + l1 + "\n" // Se la condizione è vera salto alla label1 e push 1 (true) altrimenti push 0 (false) e salto a label2
				+ "push 0\n"
				+ "b " + l2 + "\n"
				+ l1 + ": \n"
				+ "push 1\n"
				+ l2 + ": \n";
	}

	@Override
	public Node cloneNode() {
		return new EqNode(this.left.cloneNode(), this.right.cloneNode());
	}
}
