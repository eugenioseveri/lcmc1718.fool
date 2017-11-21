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
		Node t = this.thenStatement.typeCheck();
		Node e = this.elseStatement.typeCheck();
		if(FOOLLib.isSubtype(t, e)) return e;
		if(FOOLLib.isSubtype(e, t)) return t;
		System.out.println("Incompatible types in then-else branches!");
		System.exit(0);
		return null; // Irraggiungibile, ma è un problema non decidibile
	}

}
