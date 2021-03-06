package ast;

import java.util.ArrayList;
import java.util.List;

import lib.FOOLLib;
import lib.FOOLLib.MethodInheritanceType;

public class MethodNode implements DecNode {

	private final String id;
	private final Node type;
	private final List<Node> parlist;
	private final List<Node> decList;
	private Node exp;
	private Node symType;
	private String label;
	private int offset; // Informazione ridondante perchè già presente nella class table come indice della lista dei metodi
	private final MethodInheritanceType mit; // Aggiunto per ulteriore ottimizzazione di typecheck e di code generation

	public MethodNode(final String id, final Node type, final List<Node> parlist, final List<Node> decList, final MethodInheritanceType mit) {
		super();
		this.id = id;
		this.type = type;
		this.parlist = parlist;
		this.decList = decList;
		this.mit = mit;
	}

	public void addBody(final Node exp) {
		this.exp = exp;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getId() {
		return this.id;
	}

	public int getMethodOffset() {
		return this.offset;
	}

	public void setMethodOffset(final int offset) {
		this.offset = offset;
	}

	public MethodInheritanceType getMit() {
		return this.mit;
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
		return indent + "Method:" + this.id + "\n"
				+ this.type.toPrint(indent + "  ")
				+ parStr
				+ declrStr
				+ this.exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Chiamare il type check delle la lista delle dichiarazioni interne al metodo
		for (final Node dec:decList) {
			dec.typeCheck();
		}
		// Controlliamo che il corpo della funzione sia sottotipo del tipo del metodo
		if (!(FOOLLib.isSubtype(this.exp.typeCheck(), this.type))) {
			System.out.println("Incompatible value for variable!");
			System.exit(0);
		}
		return null; // Come VarNode è una dichiarazione, quindi non si ha valore di ritorno
	}

	@Override
	public String codeGeneration() {
		/* Passi:
		 * - generare indirizzo (label) dove mettere il codice del metodo
		 * - creare realmente il codice del metodo
		 */
		String declCode = "";
		for (final Node dec:this.decList) {
			declCode += dec.codeGeneration();
		}
		String popDecl = "";
		for (int i = 0; i < this.decList.size(); i++) {
			popDecl += "pop\n";
		}
		String popParList = "";
		for (int i = 0; i < this.parlist.size(); i++) {
			popParList += "pop\n";
		}
		// Crea realmente il codice della funzione (compreso di dichiarazioni interne e corpo (exp))
		FOOLLib.putCode(this.label + ":\n"
				+ "cfp\n" // Setta FP allo SP
				+ "lra\n" // Prende il valore del Return Address e lo mette sullo stack
				+ declCode
				+ this.exp.codeGeneration() //codice del corpo della funzione
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
		return null;
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
		final List<Node> params = new ArrayList<>();
		for (final Node n: this.parlist) {
			params.add(n.cloneNode());
		}
		final List<Node> decs = new ArrayList<>();
		for (final Node n: this.decList) {
			decs.add(n.cloneNode());
		}
		// MethodInheritanceType.INHERIT = perché rientra nel caso di un metodo ereditato da una classe figlio
		final MethodNode tmp = new MethodNode(this.id, this.type.cloneNode(), params, decs, MethodInheritanceType.INHERIT);
		tmp.setSymType(this.symType.cloneNode());
		tmp.addBody(this.exp);
		return tmp;
	}
}
