package ast;

public class BoolTypeNode implements Node {

	public BoolTypeNode() { }

	@Override
	public String toPrint(final String indent) {
		return indent + "BoolType\n";
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in BoolTypeNode richiamato erroneamente.");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in BoolTypeNode richiamato erroneamente.");
	}

	@Override
	public Node cloneNode() {
		return new BoolTypeNode();
	}
}
