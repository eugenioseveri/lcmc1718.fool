package ast;

public class BoolTypeNode implements Node {

	public BoolTypeNode() {
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "BoolType\n";
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		return null;
	}
}
