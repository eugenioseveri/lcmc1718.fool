package ast;

public class IntNode implements Node {

	public int integer;
	
	public IntNode(int integer) {
		super();
		this.integer = integer;
	}

	@Override
	public String toPrint(String indent) { // "indent" è una stringa di spazi corrispondenti all'indentazione corrente 
		return indent + "Int:" + this.integer + "\n";
	}

	@Override
	public Node typeCheck() {
		return new IntTypeNode();
	}

	@Override
	public String codeGeneration() {
		return "push " + this.integer + "\n";
	}
	
	@Override
	public Node cloneNode() {
		return new IntNode(this.integer);
	}
}
