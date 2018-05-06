package ast;

import java.util.ArrayList;
import java.util.List;

public class ClassTypeNode implements Node {

	private List<Node> allFields; // Tipi dei campi, inclusi quelli ereditati
	private List<Node> allMethods; // Tipi funzionali dei metodi, inclusi quelli ereditati

	public ClassTypeNode(final List<Node> fieldsList, final List<Node> methodsList) {
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
	public String toPrint(final String indent) {
		String fields = "";
		String methods = "";
		for (Node f:this.allFields) {
			fields += f.toPrint(indent + "  ");
		}
		for (Node m:this.allMethods) {
			methods += m.toPrint(indent + "  ");
		}
		return indent + "ClassType\n"
				+ fields
				+ methods;
	}

	@Override
	public Node typeCheck() {
		throw new UnsupportedOperationException("Metodo typeCheck() in ClassTypeNode richiamato erroneamente.");
	}

	@Override
	public String codeGeneration() {
		throw new UnsupportedOperationException("Metodo codeGeneration() in ClassTypeNode richiamato erroneamente.");
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
