package ast;

import java.util.ArrayList;

public class ProgLetInNode implements Node {

	private ArrayList<Node> decList;
	private Node exp;
	
	public ProgLetInNode(ArrayList<Node> declist, Node exp) {
		super();
		this.decList = declist;
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		String declStr = "";
		for(Node  dec:this.decList) {
			declStr += dec.toPrint(indent + "  ");
		}
		return indent + "ProgLetIn\n" + declStr + this.exp.toPrint("  ");
	}

}
