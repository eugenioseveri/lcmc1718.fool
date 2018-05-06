package ast;

public class BoolNode implements Node {

	private boolean bool;

	public BoolNode(final boolean bool) {
		super();
		this.bool = bool;
	}

	@Override
	public String toPrint(final String indent) {
		return this.bool ? indent + "Bool:true\n" : indent + "Bool:false\n";
	}

	@Override
	public Node typeCheck() {
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		return "push " + (this.bool ? 1 : 0) + "\n"; // Mette sullo stack 1 per "true" o 0 per "false"
	}

	@Override
	public Node cloneNode() {
		return new BoolNode(this.bool);
	}

}
