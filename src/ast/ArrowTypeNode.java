package ast;

import java.util.ArrayList;
import java.util.List;

public class ArrowTypeNode implements Node {

	private List<Node> parlist; // Lista con i tipi dei parametri
	private Node ret; // Tipo di ritorno della funzione

	public ArrowTypeNode(final List<Node> parlist, final Node ret) {
		super();
		this.parlist = parlist;
		this.ret = ret;
	}

	public List<Node> getParlist() {
		return this.parlist;
	}

	public Node getRet() {
		return this.ret;
	}

	@Override
	public String toPrint(final String indent) {
		String parlStr = "";
		for (Node par: this.parlist) {
			parlStr += par.toPrint(indent + "  ");
		}
		return indent + "ArrowType\n"
				+ parlStr
				+ this.ret.toPrint(indent + "  ->");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in ArrowTypeNode richiamato erroneamente.");
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in ArrowTypeNode richiamato erroneamente.");
	}

	@Override
	public Node cloneNode() {
		List<Node> params = new ArrayList<>();
		for (Node n: this.parlist) {
			params.add(n.cloneNode());
		}
		return new ArrowTypeNode(params, this.ret.cloneNode());
	}

}
