package ast;

import java.util.ArrayList;
import java.util.List;

public class ClassNode implements DecNode {

	private String id;
	private List<Node> fields = new ArrayList<>();
	private List<Node> methods = new ArrayList<>();
	private Node symType;
	
	public ClassNode(final String id, final List<Node> fields, final List<Node> methods) {
		super();
		this.id = id;
		this.fields = fields;
		this.methods = methods;
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
}
