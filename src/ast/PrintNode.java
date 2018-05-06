package ast;

public class PrintNode implements Node {

	private Node exp;

	public PrintNode(final Node exp) {
		super();
		this.exp = exp;
	}

	@Override
	public String toPrint(final String indent) {
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

	@Override
	public Node cloneNode() {
		throw new UnsupportedOperationException("Metodo cloneNode() in PrintNode richiamato erroneamente.");
	}

}
