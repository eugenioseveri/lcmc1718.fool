package ast;

public class ParNode implements DecNode {

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
				this.type.toPrint(indent + "  ");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in ParNode richiamato erroneamente.");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in ParNode richiamato erroneamente.");
	}

	@Override
	public Node getSymType() {
		return this.type;
	}
	
	@Override
	public Node cloneNode() {
		return new ParNode(this.id, this.type.cloneNode());
	}

}
