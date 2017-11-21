package ast;

public class STEntry {

	private int nestingLevel;
	private Node type;
	
	public STEntry(int nestingLevel) {
		super();
		this.nestingLevel = nestingLevel;
	}
	
	public STEntry(int nestingLevel, Node type) {
		super();
		this.nestingLevel = nestingLevel;
		this.type = type;
	}
	
	public void addType(Node type) {
		this.type = type;
	}
	
	public Node getType() {
		return this.type;
	}

	public String toPrint(String indent) { 
		return indent + "STEntry: nestlev " + this.nestingLevel + "\n"
				+ indent + "STEntry: type\n " + this.type.toPrint(indent + "  ");
	}
}
