package ast;

import lib.FOOLLib;

public class VarNode implements DecNode {

	private String id;
	private Node type;
	private Node exp;

	public VarNode(final String id, final Node type, final Node exp) {
		super();
		this.id = id;
		this.type = type;
		this.exp = exp;
	}

	@Override
	public String toPrint(final String indent) {
		return indent + "Var:" + this.id + "\n"
				+ this.type.toPrint(indent + "  ")
				+ this.exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controlliamo che il tipo della espressione sia sottotipo del tipo della var (variabile)
		if (!(FOOLLib.isSubtype(this.exp.typeCheck(), this.type))) {
			System.out.println("Incompatible value for variable " + this.id + "!");
			System.exit(0);
		}
		return null; // Per le dichiarazioni possiamo lasciare a null, perché non ha senso il tipo di ritorno per le variabili
	}

	@Override
	public String codeGeneration() {
		// Nel caso varNode sia di tipo funzionale => mette due cose nello stack (vedere IdNode)
		return this.exp.codeGeneration();
	}

	@Override
	public Node getSymType() {
		return this.type;
	}

	@Override
	public Node cloneNode() {
		return new VarNode(this.id, this.type.cloneNode(), this.exp.cloneNode());
	}
}
