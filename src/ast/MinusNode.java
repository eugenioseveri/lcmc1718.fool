package ast;

import lib.FOOLLib;

public class MinusNode implements Node {

	private Node left;
	private Node right;
	
	public MinusNode(Node left, Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Minus\n"
				+ this.left.toPrint(indent + "  ")
				+ this.right.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controllare che l'operazione "-" sia tra interi o sottotipi di interi: consideriamo anche Bool come sottotipo di Int (valuteremo "false" come 0 e "true" come 1).
		if(!(FOOLLib.isSubtype(this.left.typeCheck(), new IntTypeNode()) && FOOLLib.isSubtype(this.right.typeCheck(), new IntTypeNode()))) {
			System.out.println("Non integers in sub!");
			System.exit(0);
		};
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		return this.left.codeGeneration()
				+ this.right.codeGeneration()
				+ "sub\n";
	}

}
