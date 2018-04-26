package ast;

import java.util.ArrayList;
import java.util.List;

import lib.FOOLLib;

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
		String parStr = "";
		for(Node par:this.parList) {
			parStr += par.toPrint(indent + "  ");
		}
		return indent + "New: " + this.classId + "\n"+
				//this.entry.toPrint(indent +  "  ") + //TODO decidere se è da stampare
				parStr;
	}

	@Override
	public Node typeCheck() {
		List<Node> fields = ((ClassTypeNode) this.entry.getType()).getAllFields();
		// Controllo che il numero dei parametri sia uguale alla dichiarazione del costruttore
		if (fields.size() != this.parList.size()) {
			System.out.println("Wrong number of parameters in the instantiation of " + this.classId);
			System.exit(0);
		}
		
		// Controllo che il tipo di ogni parametro attuale dentro parlist sia sottotipo dei parametri della dichiarazione del costruttore
		for(int i=0; i<this.parList.size(); i++) {
			if(!(FOOLLib.isSubtype(this.parList.get(i).typeCheck(), ((FieldNode) fields.get(i)).getSymType()))) {
				System.out.println("Wrong type for " + (i+1) + "-th parameter in the instantiation of " + this.classId);
				System.exit(0);
			}
		}
		return new RefTypeNode(this.classId);
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
		return new NewNode(this.classId, this.entry.cloneEntry(), params);
	}

}
