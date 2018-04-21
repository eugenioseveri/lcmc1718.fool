package ast;

import java.util.ArrayList;
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
	
	public List<Node> getAllMethods() {
		return this.allMethods;
	}
	
	public List<Node> getAllFields() {
		return this.allFields;
	}

	@Override
	public String toPrint(String indent) {
		String fields= "";
		String methods = "";
		for(Node f:this.allFields) {
			fields += f.toPrint(indent + "  ");
		}
		for(Node m:this.allMethods) {
			methods += m.toPrint(indent + "  ");
		}
		return indent + "ClassType\n" +
				fields +
				methods;
	}

	@Override
	public Node typeCheck() {
		return null;
	}

	@Override
	public String codeGeneration() {
		return null;
	}
	
	@Override
	public Node cloneNode() {
		List<Node> fields = new ArrayList<>();
		for (Node n: this.allFields) {
			fields.add(n.cloneNode());
		}
		List<Node> methods = new ArrayList<>();
		for (Node n: this.allMethods) {
			methods.add(n.cloneNode());
		}
		return new ClassTypeNode(fields, methods);
	}
}
