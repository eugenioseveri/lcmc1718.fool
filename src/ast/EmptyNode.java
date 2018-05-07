package ast;

public class EmptyNode implements Node {
	
	public EmptyNode() { }

	@Override
	public String toPrint(final String indent) {
		return indent + "Null\n";
	}

	@Override
	public Node typeCheck() {
		return new EmptyTypeNode();
	}

	@Override
	public String codeGeneration() {
		return "push -1\n";
	}

	@Override
	public Node cloneNode() {
		return new EmptyNode();
	}

}
