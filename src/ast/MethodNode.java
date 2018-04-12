package ast;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public String toPrint(String indent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node typeCheck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getSymType() {
		return symType;
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
