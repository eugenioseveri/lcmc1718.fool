package ast;

import java.util.ArrayList;

import lib.FOOLLib;

public class FunNode implements Node {

	private String id;
	private Node type;
	private ArrayList<Node> parlist = new ArrayList<Node>();
	private ArrayList<Node> decList = new ArrayList<Node>();
	private Node exp;
	
	public FunNode(String id, Node type) {
		super();
		this.id = id;
		this.type = type;
	}
	
	public void addPar(Node par) {
		this.parlist.add(par);
	}
	
	public void addDec(ArrayList<Node> declist) {
		this.decList = declist;
	}
	
	public void addBody (Node exp) {
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		String declrStr= "";
		String parStr = "";
		for(Node dec:this.decList) {
			declrStr += dec.toPrint(indent + "  ");
		}
		for(Node par:this.parlist) {
			parStr += par.toPrint(indent + "  ");
		}
		return indent + "Fun:" + this.id + "\n" +
				this.type.toPrint(indent + "  ") +
				parStr +
				declrStr +
				this.exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		//chiamare il type check delle la lista delle dichiarazioni interne alla funzione
		for(Node dec:decList) {
			dec.typeCheck();
		}
		//controlliamo che il corpo della funzione sia sottotipo del tipo della funzione
		if(!(FOOLLib.isSubtype(this.exp.typeCheck(), this.type))) {
			System.out.println("Incompatible value for variable!");
			System.exit(0);
		}
		return null; // Come VarNode è una dichiarazione, quindi non si ha valore di ritorno
	}

}
