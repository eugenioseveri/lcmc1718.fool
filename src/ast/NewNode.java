package ast;

import java.util.ArrayList;
import java.util.Map;

public class NewNode implements Node {

	private String classId; // Id della classe che sto istanziando
	private ArrayList<Node> parList; // Argomenti che vengono passati alla new
	private Map<String,STEntry> entry; // Class entry che descrive la classe istanziata (sia campi che metodi)
	
	public NewNode(String classId, ArrayList<Node> parList, Map<String,STEntry> entry) {
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

}
