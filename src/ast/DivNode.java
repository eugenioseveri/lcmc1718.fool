package ast;

import lib.FOOLLib;

public class DivNode implements Node {

	private Node left;
	private Node right;

	public DivNode(final Node left, final Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "Div\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// TODO Da gestire il caso della divisione per 0?
		if (!(FOOLLib.isSubtype(this.left.typeCheck(), new IntTypeNode()) && FOOLLib.isSubtype(this.right.typeCheck(), new IntTypeNode()))) {
			System.out.println("Non integers in division!");
			System.exit(0);
		}
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		return this.left.codeGeneration()
				+ this.right.codeGeneration()
				+ "div\n";
	}

	@Override
	public Node cloneNode() {
		return new DivNode(this.left.cloneNode(), this.right.cloneNode());
	}

}
