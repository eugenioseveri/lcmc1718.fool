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
		// Chiamare il type check delle la lista delle dichiarazioni interne alla funzione
		for(Node dec:decList) {
			dec.typeCheck();
		}
		// Controlliamo che il corpo della funzione sia sottotipo del tipo della funzione
		if(!(FOOLLib.isSubtype(this.exp.typeCheck(), this.type))) {
			System.out.println("Incompatible value for variable!");
			System.exit(0);
		}
		return null; // Come VarNode è una dichiarazione, quindi non si ha valore di ritorno
	}

	@Override
	public String codeGeneration() {
		final String funl = FOOLLib.freshFunLabel();
		String declCode = "";
		for(Node dec:this.decList) {
			declCode += dec.codeGeneration();
		}
		String popDecl = "";
		for(Node dec:this.decList) {
			popDecl += "pop\n";
		}
		String popParList = "";
		for(Node par:this.parlist) {
			popParList += "pop\n";
		}
		FOOLLib.putCode(funl + ":\n"
				+ "cfp\n" // Setta FP allo SP
				+ "lra\n" // Prende il valore del Return Address e lo mette sullo stack
				+ declCode
				+ this.exp.codeGeneration()
				// Da qui, deallocazione AR
				+ "srv\n" // Pop del return value della funzione precedente e memorizzazione in RV (registro temporaneo per il valore di ritorno)
				+  popDecl // Aggiunge una "pop" per ogni dichiarazione
				+ "sra\n" // Pop del Return Address e memorizzazione in RA
				+ "pop\n" // Pop dell'Access Link (che non serve più)
				+ popParList // Pop di tutti i parametri
				+ "sfp\n" // Ripristino il FP al valore del Control Link
				+ "lrv\n" // Ora che l'AR è stato deallocato, si lascia sullo stack il valore di ritorno della funzione
				+ "lra\n" // Ritornare al chiamante (indirizzo temporaneamente messo in RA)
				+ "js\n" // Salta a RA
				); 
		return "push " + funl + "\n";
	}

}
