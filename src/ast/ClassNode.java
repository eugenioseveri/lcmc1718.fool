package ast;

import java.util.ArrayList;
import java.util.List;

import lib.FOOLLib;

public class ClassNode implements DecNode {

	private String id;
	private List<Node> fields = new ArrayList<>();
	private List<Node> methods = new ArrayList<>();
	private STEntry classEntry;
	private STEntry superEntry;
	private Node symType;
	
	public ClassNode(final String id, final STEntry classEntry, final STEntry superEntry, final List<Node> fields, final List<Node> methods) {
		super();
		this.id = id;
		this.classEntry = classEntry;
		this.superEntry = superEntry;
		this.fields = fields;
		this.methods = methods;
	}
	
	@Override
	public String toPrint(String indent) {
		String fields= "";
		String methods = "";
		for(Node f:this.fields) {
			fields += f.toPrint(indent + "  ");
		}
		for(Node m:this.methods) {
			methods += m.toPrint(indent + "  ");
		}
		String superEntryStr = "";
		if (this.superEntry != null) { 
			superEntryStr = this.superEntry.toPrint(indent +  "  ");
		}
		//TODO decidere cosa stampare
		return indent + "Class " + this.id + "\n" +
			//this.classEntry.toPrint(indent +  "  ");
			//superEntryStr + 
			fields +
			methods;
	}

	@Override
	public Node typeCheck() {
		//richiama sui figli che sono metodi
		for(Node n: this.methods) {
			n.typeCheck();
		}
		
		//Campi e metodi della classe padre
		List<Node> superFields = ((ClassTypeNode)this.superEntry.getType()).getAllFields();
		List<Node> superMethods = ((ClassTypeNode)this.superEntry.getType()).getAllMethods();
		
		//Campi e metodi della classe locale
		List<Node> fields = ((ClassTypeNode)this.symType).getAllFields();
		List<Node> methods = ((ClassTypeNode)this.symType).getAllMethods();
		
		//Controllo che i campi della classe figlio siano sottotipo della classe padre
		for(int i = 0; i < superFields.size(); i++) {
			if (!FOOLLib.isSubtype(((FieldNode)fields.get(i)).getSymType(),((FieldNode)superFields.get(i)).getSymType())){
				System.out.println("Error subtyping field " + ((FieldNode)fields.get(i)).getId() + " on Class: " + this.id + " at index " + i + " !");
				System.exit(0);
			}
		}
		
		//Controllo che i metodi della classe figlio siano sottotipo della classe padre
		for(int i = 0; i < superMethods.size(); i++) {
			if (!FOOLLib.isSubtype(((MethodNode)methods.get(i)).getSymType(),((MethodNode)superMethods.get(i)).getSymType())){
				System.out.println("Error subtyping method " + ((MethodNode)methods.get(i)).getId() + " on Class: " + this.id + " at index " + i + " !");
				System.exit(0);
			}
		}
		return null;
	}

	@Override
	public String codeGeneration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getSymType() {
		return symType;
	}

	public void setSymType(Node symType) {
		this.symType = symType;
	}
	
	@Override
	public Node cloneNode() {
		return null;
	}
}
