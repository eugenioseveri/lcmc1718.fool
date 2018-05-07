package ast;

import lib.FOOLLib;

public class TimesNode implements Node {

	private final Node left;
	private final Node right;

	public TimesNode(final Node left, final Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "Times\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		if (!(FOOLLib.isSubtype(this.left.typeCheck(), new IntTypeNode()) && FOOLLib.isSubtype(this.right.typeCheck(), new IntTypeNode()))) {
			System.out.println("Non integers in multiplication!");
			System.exit(0);
		}
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		return this.left.codeGeneration()
				+ this.right.codeGeneration()
				 + "mult\n";
	}

	@Override
	public Node cloneNode() {
		return new TimesNode(this.left.cloneNode(), this.right.cloneNode());
	}
}
