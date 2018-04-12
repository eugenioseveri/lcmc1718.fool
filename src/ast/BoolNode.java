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

	@Override
	public String codeGeneration() {
		//true metto 1 nello stack altrimenti 0
		return "push " + (this.bool ? 1 : 0) + "\n";
	}

	@Override
	public Node cloneNode() {
		return new BoolNode(this.bool);
	}

}
