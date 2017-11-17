package ast;

public class IdNode implements Node {

	public String id;
	private STEntry entry;
	
	public IdNode(String id, STEntry entry) {
		super();
		this.id = id;
		this.entry = entry;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Id:" + this.id + "\n" + entry.toPrint(indent + "  ");
	}
}
