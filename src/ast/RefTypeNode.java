package ast;

public class RefTypeNode implements Node {

	private String id; // Contiene l'ID della classe

	public RefTypeNode(final String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "RefType " + this.id + "\n";
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in RefTypeNode richiamato erroneamente.");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in RefTypeNode richiamato erroneamente.");
	}

	@Override
	public Node cloneNode() {
		return new RefTypeNode(this.id);
	}
}
