package ast;

public class EmptyTypeNode implements Node {

	public EmptyTypeNode() {}
	
	@Override
	public String toPrint(String indent) {
		return indent + "EmptyType\n";
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in EmptyTypeNode richiamato erroneamente.");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in EmptyTypeNode richiamato erroneamente.");
	}
	
	@Override
	public Node cloneNode() {
		return new EmptyTypeNode();
	}

}
