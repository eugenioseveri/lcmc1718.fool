package ast;

public class IntTypeNode implements Node {

	public IntTypeNode() {
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "IntType\n";
	}
}
