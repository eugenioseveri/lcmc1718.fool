package ast;

public class BoolNode implements Node {

	private boolean bool;
	
	public BoolNode(boolean bool) {
		super();
		this.bool = bool;
	}

	@Override
	public String toPrint(String indent) {
		return this.bool ? indent + "Bool:true\n" : indent + "Bool:false\n";
	}

	@Override
	public Node typeCheck() {
		return new BoolTypeNode();
	}

}
