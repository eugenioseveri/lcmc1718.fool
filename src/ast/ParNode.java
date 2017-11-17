package ast;

public class ParNode implements Node {

	private String id;
	private Node type;

	public ParNode(String id, Node type) {
		super();
		this.id = id;
		this.type = type;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Par:" + this.id + "\n" +
				type.toPrint(indent + "  ");
	}

}
