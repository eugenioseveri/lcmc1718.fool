package ast;

import java.util.ArrayList;

import lib.FOOLLib;

public class CallNode implements Node {

	private String id;
	private int nestingLevel;
	private STEntry entry;
	private ArrayList<Node> parlist = new ArrayList<Node>();
	
	public CallNode(String id, STEntry entry, ArrayList<Node> parlist, int nestingLevel) {
		super();
		this.id = id;
		this.entry = entry;
		this.parlist = parlist;
		this.nestingLevel = nestingLevel;
	}

	@Override
	public String toPrint(String indent) {
		String parStr = "";
		for(Node par:this.parlist) {
			parStr += par.toPrint(indent + "  ");
		}
		return indent + "Call:" + this.id + " at nestingLevel " + this.nestingLevel + "\n"
				+ this.entry.toPrint(indent +  "  ")
				+ parStr;
	}

	@Override
	public Node typeCheck() {
		ArrowTypeNode atn = null;
		// Controllo che l'identificatore faccia riferiemento ad una funzione
		if(this.entry.getType() instanceof ArrowTypeNode) {
			atn = (ArrowTypeNode) entry.getType(); 
		} else {
			// Errore perchè sto usando l'identificatore di una variabile come se fosse una funzione
			System.out.println("Invocation of a non-function " + this.id);
			System.exit(0);
		}
		
		// Controllo che il numero dei parametri sia uguale alla dichiarazione
		ArrayList<Node> atnParList = atn.getParlist();
		if (!(atnParList.size() == this.parlist.size())) {
			System.out.println("Wrong number of parameters in the invocation of " + this.id);
			System.exit(0);
		}
		
		// Controllo che il tipo di ogni parametro attuale dentro parlist sia sottotipo dei parametri della dichiarazione
		for(int i=0; i<this.parlist.size(); i++) {
			if(!(FOOLLib.isSubtype((this.parlist.get(i)).typeCheck(), atnParList.get(i)))) {
				System.out.println("Wrong type for " + (i+1) + "-th parameter in the invocation of " + this.id);
				System.exit(0);
			}
		}
		return atn.getRet();
	}

	@Override
	public String codeGeneration() {
		String parCode = ""; // Stringa per allocare i parametri (si parte dall'ultimo, come da struttura della memoria)
		for(int i=this.parlist.size()-1; i>=0; i--) {
			parCode += this.parlist.get(i).codeGeneration();
		}
		String getAR = "";
		for(int i=0; i<this.nestingLevel-this.entry.getNestingLevel(); i++) {
			getAR += "lw\n";
		}
		return // Allocazione della parte corrente dell'AR della funzione che sta venendo chiamata
				"lfp\n" // Control Link
				+ parCode // Allocazione parametri
				+ "lfp\n" // Access Link (anche prossima riga)
				+ getAR
				// Da qui, codice per recuperare l'indirizzo a cui saltare (uguale a IdNode)
				+ "push " + this.entry.getOffset() + "\n" // Mette l'offset sullo stack
				+ "lfp\n" // Mette l'indirizzo puntato dal registro FP sullo stack (l'indirizzo dell'AR della variabile)
				+ getAR // Risalgo la catena statica
				+ "add\n" // Li somma
				+ "lw\n" // Carica sullo stack il valore all'indirizzo ottenuto
				// Salto
				+ "js\n";
	}

}
