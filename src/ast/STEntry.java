package ast;

public class STEntry {

	// "Pallina" dell'albero che contiene il livello dell'albero e il tipo dell'identificatore
	private final int nestingLevel;
	private Node type;
	private int offset; // Serve per identificare la posizione della variabile dentro l'AR (activation record)
	private boolean isMethod = false;

	public STEntry(final int nestingLevel, final int offset) {
		super();
		this.nestingLevel = nestingLevel;
		this.offset = offset;
	}

	public STEntry(final int nestingLevel, final Node type) {
		super();
		this.nestingLevel = nestingLevel;
		this.type = type;
	}

	public STEntry(final int nestingLevel, final Node type, final int offset) {
		super();
		this.nestingLevel = nestingLevel;
		this.type = type;
		this.offset = offset;
	}

	public STEntry(final int nestingLevel, final int offset, final boolean isMethod) {
		super();
		this.nestingLevel = nestingLevel;
		this.type = null;
		this.offset = offset;
		this.isMethod = isMethod;
	}

	public STEntry(final int nestingLevel, final Node type, final int offset, final boolean isMethod) {
		super();
		this.nestingLevel = nestingLevel;
		this.type = type;
		this.offset = offset;
		this.isMethod = isMethod;
	}

	public void addType(final Node type) {
		this.type = type;
	}

	public int getNestingLevel() {
		return this.nestingLevel;
	}

	public Node getType() {
		return this.type;
	}

	public int getOffset() {
		return this.offset;
	}

	public boolean isMethod() {
		return this.isMethod;
	}

	public String toPrint(final String indent) {
		return indent + "STEntry: nestingLevel " + this.nestingLevel + "\n"
				+ indent + "STEntry: type\n" + this.type.toPrint(indent + "  ")
				+ indent + "STEntry: offset " + this.offset + ("\n")
				+ indent + "STEntry: isMethod " + this.isMethod + ("\n");
	}

	public STEntry cloneEntry() {
		return new STEntry(this.nestingLevel, this.type.cloneNode(), this.offset, this.isMethod);
	}
}
