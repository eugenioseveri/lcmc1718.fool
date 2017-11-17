package ast;

public class VarNode implements Node {

	private String id;
	private Node type;
	private Node exp;

	public VarNode(String id, Node type, Node exp) {
		super();
		this.id = id;
		this.type = type;
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Var:" + this.id + "\n" +
				type.toPrint(indent + "  ") +
				this.exp.toPrint(indent + "  ");
	}

}
