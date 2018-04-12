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
		return null;
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		return null;
	}
	
	@Override
	public Node cloneNode() {
		return new EmptyTypeNode();
	}

}
