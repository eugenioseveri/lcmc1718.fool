package ast;

public class PrintNode implements Node {

	private Node exp;
	
	public PrintNode(Node exp) {
		super();
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Print\n" + exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		return this.exp.typeCheck(); // Ritorna il tipo di se stesso
	}

	@Override
	public String codeGeneration() {
		return this.exp.codeGeneration()
				+ "print\n";
	}

}
