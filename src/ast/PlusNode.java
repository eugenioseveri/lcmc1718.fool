package ast;

import lib.FOOLLib;

public class PlusNode implements Node {

	private Node left;
	private Node right;

	public PlusNode(final Node left, final Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "Plus\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controllare che l'operazione "+" sia tra interi o sottotipi di interi: consideriamo anche Bool come sottotipo di Int (valuteremo "false" come 0 e "true" come 1).
		if (!(FOOLLib.isSubtype(this.left.typeCheck(), new IntTypeNode()) && FOOLLib.isSubtype(this.right.typeCheck(), new IntTypeNode()))) {
			System.out.println("Non integers in sum!");
			System.exit(0);
		}
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		return this.left.codeGeneration()
				+ this.right.codeGeneration()
				+ "add\n";
	}

	@Override
	public Node cloneNode() {
		return new PlusNode(this.left.cloneNode(), this.right.cloneNode());
	}
}
