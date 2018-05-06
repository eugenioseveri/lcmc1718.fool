package ast;

import lib.FOOLLib;

public class NotNode implements Node {

	private Node exp;
	
	public NotNode(Node exp) {
		super();
		this.exp = exp;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Not\n"
				+ this.exp.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controllo che l'operazione "!" (not) sia tra booleani
		if(!(FOOLLib.isSubtype(this.exp.typeCheck(), new BoolTypeNode()))) {
			System.out.println("Non boolean in not!");
			System.exit(0);
		};
		return new BoolTypeNode();
	}

	@Override
	public String codeGeneration() {
		final String l1 = FOOLLib.freshLabel();
		final String l2 = FOOLLib.freshLabel();
		/* Verifico se l'espressione è TRUE confrontadola tramite 'beq' (precedentemente pusho 1 (TRUE)):
		 * Se uguale a 1 salto e restituisco l'opposto facendo push 0 (FALSE),
		 * altrimenti se diverso da 1, quindi l'espressione è FALSE, restituisco l'opposto facendo push 1 (TRUE) */
		return this.exp.codeGeneration()
				+ "push 1\n"
				+ "beq " + l1 + "\n"
				+ "push 1\n"
				+ "b " + l2 + "\n"
				+ l1 + ": \n"
				+ "push 0\n"
				+ l2 + ": \n";
	}
	
	@Override
	public Node cloneNode() {
		return new NotNode(this.exp.cloneNode());
	}
}
