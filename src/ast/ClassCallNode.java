package ast;

import java.util.List;
public class ClassCallNode implements Node {
	
	private String id;
	private String method;
	private STEntry classEntry;
	private STEntry methodEntry;
	private List<Node> argList;
	private int nestingLevel;
	
	public ClassCallNode(final String id, final String method, final STEntry classEntry, final STEntry methodEntry, final List<Node> argList, final int nestingLevel) {
		this.id = id;
		this.method = method;
		this.classEntry = classEntry;
		this.methodEntry = methodEntry;
		this.argList = argList;
		this.nestingLevel = nestingLevel;
	}

	@Override
	public String toPrint(String indent) {
		String argStr = "";
		for(Node arg:this.argList) {
			argStr += arg.toPrint(indent + "  ");
		}
		return indent + "ClassCall: " + this.id + "." + this.method + " at nestingLevel " + this.nestingLevel + "\n"+
				this.classEntry.toPrint(indent +  "  ") + 
				this.methodEntry.toPrint(indent +  "  ") +
				argStr;
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
	public Node cloneNode() {
		return null;
	}

}
