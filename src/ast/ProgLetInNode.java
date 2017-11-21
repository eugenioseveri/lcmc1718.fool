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
		for(Node dec:this.decList) {
			declStr += dec.toPrint(indent + "  ");
		}
		return indent + "ProgLetIn\n" + declStr + this.exp.toPrint("  ");
	}

	@Override
	public Node typeCheck() {
		//bisogna lanciare il type checking anche sui figli che sono le dichiarazioni (che possono essere funzioni o variabili). 
		//Il loro valore di ritorno lo ignoro ma l'importante è controllare che internamente siano consistenti
		for(Node dec:decList) {
			dec.typeCheck();
		}
		return this.exp.typeCheck();
	}

}
