package ast;

import lib.FOOLLib;

public class IfNode implements Node {

	private Node condition;
	private Node thenStatement;
	private Node elseStatement;
	
	public IfNode(Node condition, Node thenStatement, Node elseStatement) {
		super();
		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "If\n"
				+ this.condition.toPrint(indent + "  ")
				+ this.thenStatement.toPrint(indent + "  ")
				+ this.elseStatement.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// La condizione è bool, lo then è "circle" e l'else "square". Avendo una biforcazione tra then ed else, in teoria si dovrebbe richiedere "shape" (il "lowest common ancestor").
		if(!(FOOLLib.isSubtype(this.condition.typeCheck(), new BoolTypeNode()))) {
			System.out.println("Non boolean condition in 'if'!");
			System.exit(0);
		}
		Node thenNode = this.thenStatement.typeCheck();
		Node elseNode = this.elseStatement.typeCheck();
		if(FOOLLib.isSubtype(thenNode, elseNode)) return elseNode;
		if(FOOLLib.isSubtype(elseNode, thenNode)) return thenNode;
		System.out.println("Incompatible types in then-else branches!");
		System.exit(0);
		return null; // Irraggiungibile, ma è un problema non decidibile
	}
	
	@Override
	public String codeGeneration() {
		final String l1 = FOOLLib.freshLabel();
		final String l2 = FOOLLib.freshLabel();
		return this.condition.codeGeneration()
				+ "push 1\n"
				+ "beq " + l1 + "\n"
				+ this.elseStatement.codeGeneration()
				+ "b " + l2 + "\n"
				+ l1 + ": \n"
				+ this.thenStatement.codeGeneration()
				+ l2 + ": \n";
	}

}
