package ast;

import java.util.ArrayList;
import java.util.List;

import lib.FOOLLib;

public class MethodNode implements DecNode {

	private String id;
	private Node type;
	private List<Node> parlist = new ArrayList<>();
	private List<Node> decList = new ArrayList<>();
	private Node exp;
	private Node symType;
	
	public MethodNode(final String id, final Node type, final List<Node> parlist, final List<Node> decList) {
		super();
		this.id = id;
		this.type = type;
		this.parlist = parlist;
		this.decList = decList;
	}
	
	public void addBody (Node exp) {
		this.exp = exp;
	}
	
	public String getId() {
		return this.id;
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
		return indent + "Method:" + this.id + "\n" +
				this.type.toPrint(indent + "  ") +
				parStr +
				declrStr +
				this.exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Chiamare il type check delle la lista delle dichiarazioni interne al metodo
		for(Node dec:decList) {
			dec.typeCheck();
		}
		// Controlliamo che il corpo della funzione sia sottotipo del tipo del metodo
		if(!(FOOLLib.isSubtype(this.exp.typeCheck(), this.type))) {
			System.out.println("Incompatible value for variable!");
			System.exit(0);
		}
		return null; // Come VarNode è una dichiarazione, quindi non si ha valore di ritorno
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getSymType() {
		return this.symType;
	}

	public void setSymType(Node symType) {
		this.symType = symType;
	}

	@Override
	public Node cloneNode() {
		List<Node> params = new ArrayList<>();
		for (Node n: this.parlist) {
			params.add(n.cloneNode());
		}
		List<Node> decs = new ArrayList<>();
		for (Node n: this.decList) {
			decs.add(n.cloneNode());
		}
		MethodNode tmp = new MethodNode(this.id, this.type.cloneNode(), params, decs);
		tmp.setSymType(this.symType.cloneNode());
		tmp.addBody(this.exp.cloneNode());
		return tmp;
	}
}
