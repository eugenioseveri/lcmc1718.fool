package ast;

public class FieldNode implements DecNode {

	private String id;
	private Node type;
	
	public FieldNode(String id, Node type) {
		super();
		this.id = id;
		this.type = type;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Field: " + this.id + "\n"
				+ this.type.toPrint(indent + "  ");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		return null;
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		return null;
	}

	@Override
	public Node getSymType() {
		return this.type;
	}
	
	@Override
	public Node cloneNode() {
		return new FieldNode(this.id, this.type.cloneNode());
	}

}
