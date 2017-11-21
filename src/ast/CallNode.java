package ast;

import java.util.ArrayList;

public class CallNode implements Node {

	private String id;
	private STEntry entry;
	private ArrayList<Node> parlist = new ArrayList<Node>();
	
	public CallNode(String id, STEntry entry, ArrayList<Node> parlist) {
		super();
		this.id = id;
		this.entry = entry;
		this.parlist = parlist;
	}

	@Override
	public String toPrint(String indent) {
		String parStr = "";
		for(Node par:this.parlist) {
			parStr += par.toPrint(indent + "  ");
		}
		return indent + "Call:" + id + "\n"
				+ this.entry.toPrint(indent +  "  ")
				+ parStr;
	}

}
