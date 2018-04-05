package ast;

import java.util.ArrayList;

public class ClassTypeNode implements Node {

	//(tipi dei campi, inclusi quelli ereditati)
	private ArrayList<Node> allFields;
	
	//(tipi funzionali dei metodi, inclusi quelli ereditati)
	private ArrayList<Node> allMethods;

	public ClassTypeNode(ArrayList<Node> allFields, ArrayList<Node> allMethods) {
		super();
		this.allFields = allFields;
		this.allMethods = allMethods;
	}

	@Override
	public String toPrint(String indent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node typeCheck() {
		return null;
	}

	@Override
	public String codeGeneration() {
		return null;
	}

}
