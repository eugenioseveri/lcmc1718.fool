package ast;

public class IntTypeNode implements Node {

	public IntTypeNode() {
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "IntType\n";
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
}
