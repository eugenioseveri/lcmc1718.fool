package ast;

import java.util.ArrayList;
import java.util.List;

public class NewNode implements Node {

	private String classId; // Id della classe che sto istanziando
	private List<Node> parList; // Argomenti che vengono passati alla new
	private STEntry entry; // STEntry che descrive la classe istanziata (sia campi che metodi tramite classTypeNode)
	
	public NewNode(final String classId, final STEntry entry, final List<Node> parList) {
		super();
		this.classId = classId;
		this.parList = parList;
		this.entry = entry;
	}

	@Override
	public String toPrint(String indent) {
		// TODO Auto-generated method stub
		return null;
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
		List<Node> params = new ArrayList<>();
		for (Node n: this.parList) {
			params.add(n.cloneNode());
		}
		return new NewNode(this.classId, this.entry, params); //TODO verificare se si deve fare il clone dell'TEntry
	}

}
