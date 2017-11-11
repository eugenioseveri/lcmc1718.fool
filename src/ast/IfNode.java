package ast;

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

}
