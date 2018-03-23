package ast;

public class STEntry {

	//'pallina' dell'albero che contiene:
	// - il livello dell'albero
	// - tipo dell'identificatore
	private int nestingLevel;
	private Node type;
	private int offset; // Serve per identificare la posizione della variabile dentro l'AR (activation record)
	
	public STEntry(int nestingLevel, int offset) {
		super();
		this.nestingLevel = nestingLevel;
		this.offset = offset;
	}

	public STEntry(int nestingLevel, Node type) {
		super();
		this.nestingLevel = nestingLevel;
		this.type = type;
	}
	
	public STEntry(int nestingLevel, Node type, int offset) {
		super();
		this.nestingLevel = nestingLevel;
		this.type = type;
		this.offset = offset;
	}

	public void addType(Node type) {
		this.type = type;
	}
	
	public int getNestingLevel() {
		return nestingLevel;
	}

	public Node getType() {
		return this.type;
	}

	public int getOffset() {
		return this.offset;
	}

	public String toPrint(String indent) { 
		return indent + "STEntry: nestingLevel " + this.nestingLevel + "\n"
				+ indent + "STEntry: type\n " + this.type.toPrint(indent + "  ")
				+ indent + "STEntry: offset " + this.offset + ("\n");
	}
}
