package ast;

import java.util.ArrayList;

import lib.FOOLLib;

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
		for(Node dec:this.decList) {
			dec.typeCheck();
		}
		return this.exp.typeCheck();
	}

	@Override
	public String codeGeneration() {
		String declCode = "";
		for(Node dec:this.decList) {
			declCode += dec.codeGeneration();
		}
		return "push 0\n" // Spazio fittizio per uniformare la gestione della memoria
				+ declCode
				+ this.exp.codeGeneration()
				+ "halt\n"
				+ FOOLLib.getCode();
	}

}
