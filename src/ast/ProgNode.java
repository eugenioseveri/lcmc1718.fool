package ast;

public class ProgNode implements Node {

	private Node exp;
	
	public ProgNode(Node exp) {
		super();
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Prog\n" + this.exp.toPrint("  ");
	}

	@Override
	public Node typeCheck() {
		return this.exp.typeCheck();
	}

}
