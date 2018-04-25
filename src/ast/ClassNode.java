package ast;

import java.util.ArrayList;
import java.util.List;

public class ClassNode implements DecNode {

	private String id;
	private List<Node> fields = new ArrayList<>();
	private List<Node> methods = new ArrayList<>();
	private STEntry classEntry;
	private STEntry superEntry;
	private Node symType;
	
	public ClassNode(final String id, final STEntry classEntry, final STEntry superEntry, final List<Node> fields, final List<Node> methods) {
		super();
		this.id = id;
		this.classEntry = classEntry;
		this.superEntry = superEntry;
		this.fields = fields;
		this.methods = methods;
	}
	
	@Override
	public String toPrint(String indent) {
		String fields= "";
		String methods = "";
		for(Node f:this.fields) {
			fields += f.toPrint(indent + "  ");
		}
		for(Node m:this.methods) {
			methods += m.toPrint(indent + "  ");
		}
//		String superEntryStr = "";
//		if (this.superEntry != null) { 
//			superEntryStr = this.superEntry.toPrint(indent +  "  ");
//		}
		return indent + "Class " + this.id + "\n" +
			//this.classEntry.toPrint(indent +  "  ");
			//superEntryStr + //TODO decidere se è da stampare
			fields + //TODO decidere se è da stampare
			methods; //TODO decidere se è da stampare
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
		return null;
	}
}
