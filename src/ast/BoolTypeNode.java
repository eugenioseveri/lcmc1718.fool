package ast;

public class BoolTypeNode implements Node {

	public BoolTypeNode() {
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "BoolType\n";
	}
}
