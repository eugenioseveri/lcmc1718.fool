package ast;

import lib.FOOLLib;

public class EqNode implements Node {

	private Node left;
	private Node right;
	
	public EqNode(Node left, Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Equal\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Serve tenere conto dell'ereditarietà (singola)
		Node l = this.left.typeCheck();
		Node r = this.right.typeCheck();
		if(!(FOOLLib.isSubtype(l, r) || FOOLLib.isSubtype(r, l))) {
			System.out.println("Incompatible types in equal!");
			System.exit(0);
		}
		return new BoolTypeNode();
	}

}
