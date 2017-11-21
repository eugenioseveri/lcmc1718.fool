package ast;

import lib.FOOLLib;

public class VarNode implements Node {

	private String id;
	private Node type;
	private Node exp;

	public VarNode(String id, Node type, Node exp) {
		super();
		this.id = id;
		this.type = type;
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Var:" + this.id + "\n" +
				type.toPrint(indent + "  ") +
				this.exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		//controlliamo che il tipo della espressione sia sottotipo del tipo della var (variabile)
		if(!(FOOLLib.isSubtype(this.exp.typeCheck(), this.type))) {
			System.out.println("Incompatible value for variable " + this.id + "!");
			System.exit(0);
		}
		return null; // Per le dichiarazioni possiamo lasciare a null, perché non ha senso il tipo di ritorno per le variabili
	}

}
