package ast;

import java.util.ArrayList;
import java.util.List;

import lib.FOOLLib;

public class ProgLetInNode implements Node {

	private List<Node> decList;
	private List<Node> cllist;
	private Node exp;

	public ProgLetInNode(final List<Node> declist, final Node exp) {
		super();
		this.decList = declist;
		this.cllist = new ArrayList<>();
		this.exp = exp;
	}

	public ProgLetInNode(final List<Node> cllist, final List<Node> declist, final Node exp) {
		super();
		this.decList = declist;
		this.cllist = cllist;
		this.exp = exp;
	}

	@Override
	public String toPrint(final String indent) {
		String declStr = "";
		String clStr = "";
		for (Node dec:this.decList) {
			declStr += dec.toPrint(indent + "  ");
		}
		for (Node cl:this.cllist) {
			clStr += cl.toPrint(indent + "  ");
		}
		return indent + "ProgLetIn\n"
				+ clStr
				+ declStr
				+ this.exp.toPrint("  ");
	}

	@Override
	public Node typeCheck() {
		// Bisogna lanciare il type checking anche sui figli che sono le dichiarazioni (che possono essere funzioni o variabili).
		// Il loro valore di ritorno lo ignoro ma l'importante è controllare che internamente siano consistenti
		for (Node dec:this.decList) {
			dec.typeCheck();
		}
		for (Node cl:this.cllist) {
			cl.typeCheck();
		}
		return this.exp.typeCheck();
	}

	@Override
	public String codeGeneration() {
		String classCode = "";
		for (Node cl:this.cllist) {
			classCode += cl.codeGeneration();
		}
		String declCode = "";
		for (Node dec:this.decList) {
			declCode += dec.codeGeneration();
		}
		return "push 0\n" // Spazio fittizio per uniformare la gestione della memoria
				+ classCode
				+ declCode
				+ this.exp.codeGeneration()
				+ "halt\n"
				+ FOOLLib.getCode(); // Codice assembly dei metodi e delle funzioni
	}

	@Override
	public Node cloneNode() {
		throw new UnsupportedOperationException("Metodo cloneNode() in ProgLetInNode richiamato erroneamente.");
	}
}
