package ast;

import java.util.List;

public class ClassTypeNode implements Node {

	//(tipi dei campi, inclusi quelli ereditati)
	private List<Node> allFields;
	
	//(tipi funzionali dei metodi, inclusi quelli ereditati)
	private List<Node> allMethods;

	public ClassTypeNode(List<Node> fieldsList, List<Node> methodsList) {
		super();
		this.allFields = fieldsList;
		this.allMethods = methodsList;
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
