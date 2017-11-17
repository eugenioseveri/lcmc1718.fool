package ast;

public class STEntry {

	public Integer nestingLevel;
	
	public STEntry(int nestingLevel) {
		super();
		this.nestingLevel = nestingLevel;
	}

	public String toPrint(String indent) { 
		return indent + "STEntry: nestlev " + this.nestingLevel + "\n";
	}
}
