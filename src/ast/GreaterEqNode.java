package ast;

import lib.FOOLLib;

public class GreaterEqNode implements Node {

	private Node left;
	private Node right;
	
	public GreaterEqNode(Node left, Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "GreaterOrEqual\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Serve tenere conto dell'ereditarietà (singola)
		Node l = this.left.typeCheck();
		Node r = this.right.typeCheck();
		if(!(FOOLLib.isSubtype(l, r) || FOOLLib.isSubtype(r, l))) {
			System.out.println("Incompatible types in greater or equal!");
			System.exit(0);
		}
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		final String l1 = FOOLLib.freshLabel();
		final String l2 = FOOLLib.freshLabel();
		/* Essendoci in assembly solo l'opzione "<=" e non ">=", sfruttiamo "<=" "al contrario"
		 * quindi pusho prima il sottoalbero di destra e successivamente il sottoalbero di sinistra
		 * se 'bleq' è vero allora il secondo valore è minore o uguale del primo quindi salto a l1 e pusho 1 (TRUE)
		 * altrimenti pusho 0 (FALSE) salto a l2. */
		return this.right.codeGeneration()
				+ this.left.codeGeneration()
				+ "bleq " + l1 + "\n" 
				+ "push 0\n"
				+ "b " + l2 + "\n"
				+ l1 + ": \n"
				+ "push 1\n"
				+ l2 + ": \n";
	}
	
	@Override
	public Node cloneNode() {
		return new GreaterEqNode(this.left.cloneNode(), this.right.cloneNode());
	}

}
