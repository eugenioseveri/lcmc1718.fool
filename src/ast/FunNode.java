package ast;

import java.util.ArrayList;
import java.util.List;

import lib.FOOLLib;

public class FunNode implements DecNode {

	private final String id;
	private final Node type;
	private final List<Node> parlist = new ArrayList<Node>();
	private List<Node> decList = new ArrayList<Node>();
	private Node exp;
	private Node symType;

	public FunNode(final String id, final Node type) {
		super();
		this.id = id;
		this.type = type;
	}

	public void addPar(final Node par) {
		this.parlist.add(par);
	}

	public void addDec(final ArrayList<Node> declist) {
		this.decList = declist;
	}

	public void addBody(final Node exp) {
		this.exp = exp;
	}

	@Override
	public String toPrint(final String indent) {
		String declrStr = "";
		String parStr = "";
		for (final Node dec:this.decList) {
			declrStr += dec.toPrint(indent + "  ");
		}
		for (final Node par:this.parlist) {
			parStr += par.toPrint(indent + "  ");
		}
		return indent + "Fun:" + this.id + "\n"
				+ this.type.toPrint(indent + "  ")
				+ parStr
				+ declrStr
				+ this.exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Chiamare il type check delle la lista delle dichiarazioni interne alla funzione
		for (final Node dec:decList) {
			dec.typeCheck();
		}
		// Controlliamo che il corpo della funzione sia sottotipo del tipo della funzione
		if (!(FOOLLib.isSubtype(this.exp.typeCheck(), this.type))) {
			System.out.println("Incompatible value for variable!");
			System.exit(0);
		}
		return null; // Come VarNode è una dichiarazione, quindi non si ha valore di ritorno
	}

	@Override
	public String codeGeneration() {
		/* Passi:
		 * - generare indirizzo (label) dove mettere il codice della funzione
		 * - creare realmente il codice della funzione
		 */
		final String funl = FOOLLib.freshFunLabel();
		String declCode = "";
		for (final Node dec:this.decList) {
			declCode += dec.codeGeneration();
		}
		String popDecl = "";
		for (final Node dec:this.decList) {
			if (((DecNode) dec).getSymType() instanceof ArrowTypeNode) {
				popDecl += "pop\n";
			}
			popDecl += "pop\n";
		}
		String popParList = "";
		for (final Node par:this.parlist) {
			if (((DecNode) par).getSymType() instanceof ArrowTypeNode) {
				popParList += "pop\n";
			}
			popParList += "pop\n";
		}
		// Crea realmente il codice della funzione (compreso di dichiarazioni interne e corpo (exp))
		FOOLLib.putCode(funl + ":\n"
				+ "cfp\n" // Setta FP allo SP
				+ "lra\n" // Prende il valore del Return Address e lo mette sullo stack
				+ declCode
				+ this.exp.codeGeneration() // Codice del corpo della funzione
				// Da qui, deallocazione AR (activation record)
				+ "srv\n" // Pop del return value della funzione precedente e memorizzazione in RV (registro temporaneo per il valore di ritorno)
				+ popDecl // Aggiunge 'n' "pop" per ogni dichiarazione
				+ "sra\n" // Pop del Return Address e memorizzazione in RA
				+ "pop\n" // Pop dell'Access Link (che non serve più)
				+ popParList // Pop di tutti i parametri
				+ "sfp\n" // Ripristino il FP al valore del Control Link
				+ "lrv\n" // Ora che l'AR è stato deallocato, si lascia sullo stack il valore di ritorno della funzione
				+ "lra\n" // Ritornare al chiamante (indirizzo temporaneamente messo in RA)
				+ "js\n" // Salta a RA (chi ha chiamato la funzione)
				);
		return "lfp\n" // Push nello stack indirizzo FP a questo AR
				+ "push " + funl + "\n"; // Push indirizzo (etichetta) della funzione [finisce a offset - 1]
	}

	@Override
	public Node getSymType() {
		return this.symType;
	}

	public void setSymType(final Node symType) {
		this.symType = symType;
	}

	@Override
	public Node cloneNode() {
		throw new UnsupportedOperationException("Metodo cloneNode() in FunNode richiamato erroneamente.");
	}
}
