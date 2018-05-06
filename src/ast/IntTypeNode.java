package ast;

public class IntTypeNode implements Node {

	public IntTypeNode() { }

	@Override
	public String toPrint(final String indent) {
		return indent + "IntType\n";
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in IntTypeNode richiamato erroneamente.");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in IntTypeNode richiamato erroneamente.");
	}

	@Override
	public Node cloneNode() {
		return new IntTypeNode();
	}
}
