package ast;

public class RefTypeNode implements Node {

	private String id; //contiene l'ID della classe
	
	public RefTypeNode(final String id) {
		super();
		this.id = id;
	}
	
	@Override
	public String toPrint(String indent) {
		return indent + "RefType "+ this.id + "\n";
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node typeCheck() {
		return null;
	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public String codeGeneration() {
		return null;
	}
}
