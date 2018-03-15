package ast;

import lib.FOOLLib;

public class OrNode implements Node {

	private Node left;
	private Node right;
	
	
	public OrNode(Node left, Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Or\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controllare che l'operazione "||" sia tra booleani
		if(!(FOOLLib.isSubtype(this.left.typeCheck(), new BoolTypeNode()) && FOOLLib.isSubtype(this.right.typeCheck(), new BoolTypeNode()))) {
			System.out.println("Non boolean in or!");
			System.exit(0);
		};
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		final String l1 = FOOLLib.freshLabel();
		final String l2 = FOOLLib.freshLabel();
		/* Quando ho OR tra due elementi -> e1 || e2 allora: salto se almeno una delle due espressioni valuta vero (1):
		   controllo e1, pusho 1 sullo stack, tramite 'beq' verifico l'ugualianza e salto se sono uguali;
		   se non sono uguali controllo e2, pusho 1 sullo stack, tramite 'beq' verifico l'ugualianza e salto se sono uguali;
		   se non sono uguali metto 0 sullo stack ed esco. */
		return this.left.codeGeneration()
				+ "push 1\n"
				+ "beq " + l1 + "\n"
				+ this.right.codeGeneration()
				+ "push 1\n"
				+ "beq " + l1 + "\n"
				+ "push 0\n"
				+ "b " + l2 + "\n"
				+ l1 + ": \n"
				+ "push 1\n"
				+ l2 + ": \n";
	}

}
