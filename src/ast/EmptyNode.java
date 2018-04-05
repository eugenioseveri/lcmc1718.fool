package ast;

public class EmptyNode implements Node {

	@Override
	public String toPrint(String indent) {
		return indent + "Null\n";
	}

	@Override
	public Node typeCheck() {
		return new EmptyTypeNode();
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

}
