package ast;

public class FieldNode implements DecNode {

	private String id;
	private Node type;
	private int offset; // Aggiunto per l'ottimizzazione OO
	
	public FieldNode(final String id, final Node type, final int offset) {
		super();
		this.id = id;
		this.type = type;
		this.offset = offset;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getOffset() {
		return this.offset;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Field: " + this.id + "\n"
				+ this.type.toPrint(indent + "  ");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in FieldNode richiamato erroneamente.");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in FieldNode richiamato erroneamente.");
	}

	@Override
	public Node getSymType() {
		return this.type;
	}
	
	@Override
	public Node cloneNode() {
		return new FieldNode(this.id, this.type.cloneNode(),this.offset);
	}
}
