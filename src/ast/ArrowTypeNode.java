package ast;

import java.util.ArrayList;

public class ArrowTypeNode implements Node {

	private ArrayList<Node> parlist; // Lista con i tipi dei parametri
	private Node ret; // Tipo di ritorno della funzione
	
	public ArrowTypeNode(ArrayList<Node> parlist, Node ret) {
		super();
		this.parlist = parlist;
		this.ret = ret;
	}

	@Override
	public String toPrint(String indent) {
		String parlStr = "";
		for (Node par:parlist) {
			parlStr += par.toPrint(indent + "  ");
		}
		return indent + "ArrowTypeNode\n"
				+ parlStr
				+ this.ret.toPrint(indent + "  ->");
	}

}
