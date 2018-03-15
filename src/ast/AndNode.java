package ast;

import lib.FOOLLib;

public class AndNode implements Node {

	private Node left;
	private Node right;
	
	public AndNode(Node left, Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "And\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controllare che l'operazione "||" sia tra booleani
		if(!(FOOLLib.isSubtype(this.left.typeCheck(), new BoolTypeNode()) && FOOLLib.isSubtype(this.right.typeCheck(), new BoolTypeNode()))) {
			System.out.println("Non boolean in and!");
			System.exit(0);
		};
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		final String l1 = FOOLLib.freshLabel();
		final String l2 = FOOLLib.freshLabel();
		/* Quando ho AND tra due elementi: * e1 && e2 allora: salto all'errore (push 0) se almeno una delle due espressioni valuta falso (0):
		   controllo e1, pusho 0 sullo stack e salto se sono uguali;
		   se non sono uguali controllo e2, pusho 0 sullo stack e salto se sono uguali;
		   se sono uguali metto 1 sullo stack ed esco. */
		return this.left.codeGeneration()
				+ "push 0\n"
				+ "beq " + l1 + "\n"
				+ this.right.codeGeneration()
				+ "push 0\n"
				+ "beq " + l1 + "\n"
				+ "push 1\n"
				+ "b " + l2 + "\n"
				+ l1 + ": \n"
				+ "push 0\n"
				+ l2 + ": \n";

	}

}
