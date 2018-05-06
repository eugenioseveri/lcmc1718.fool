package ast;

public class ProgNode implements Node {

	private Node exp;

	public ProgNode(final Node exp) {
		super();
		this.exp = exp;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "Prog\n" + this.exp.toPrint("  ");
	}

	@Override
	public Node typeCheck() {
		return this.exp.typeCheck();
	}

	@Override
	public String codeGeneration() {
		return this.exp.codeGeneration()
				+ "halt\n";
	}

	@Override
	public Node cloneNode() {
		throw new UnsupportedOperationException("Metodo cloneNode() in ProgNode richiamato erroneamente.");
	}
}
