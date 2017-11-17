package ast;

import java.util.ArrayList;

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

}