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
		
		if (this.superEntry != null) {
			
			//Campi e metodi della classe padre
			List<Node> superFields = ((ClassTypeNode)this.superEntry.getType()).getAllFields();
			List<Node> superMethods = ((ClassTypeNode)this.superEntry.getType()).getAllMethods();
			
			//Campi e metodi della classe figlio
			List<Node> localFields = ((ClassTypeNode)this.symType).getAllFields();
			List<Node> localMethods = ((ClassTypeNode)this.symType).getAllMethods();
			
			//Type Checking più efficiente per ClassNode
			for (int i = 0; i < localFields.size(); i++) {
				int offset = ((FieldNode)localFields.get(i)).getOffset();
				if ((-offset-1) < superFields.size()) {
					if (!FOOLLib.isSubtype(((FieldNode)localFields.get((-offset-1))).getSymType(),((FieldNode)superFields.get((-offset-1))).getSymType())){
						System.out.println("Error subtyping field " + ((FieldNode)localFields.get((-offset-1))).getId() + " on Class: " + this.id + " at index " + (-offset-1) + " !");
						System.exit(0);
					}
				}
			}
			
			for (int i = 0; i < localMethods.size(); i++) {
				int offset = ((MethodNode)localMethods.get(i)).getMethodOffset();
				if ((offset) < superMethods.size()) {
					if (!FOOLLib.isSubtype(((MethodNode)localMethods.get(offset)).getSymType(),((MethodNode)superMethods.get(offset)).getSymType())){
						System.out.println("Error subtyping method " + ((MethodNode)localMethods.get(offset)).getId() + " on Class: " + this.id + " at index " + offset + " !");
						System.exit(0);
					}
				}
			}
			
			
			/*
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
			*/
		}
		return null;
	}

	@Override
	public String codeGeneration() {
		// aggiorno la Dispatch Table creata settando la posizione data dall’offset del metodo alla sua etichetta
		List<String> dispatchTable = FOOLLib.getDispatchTable().get(-this.classEntry.getOffset()-2);
		String methodLabel;
		for (int i = 0; i < this.methods.size(); i++) {
			MethodNode mn = (MethodNode) this.methods.get(i);
			methodLabel = FOOLLib.freshMethodLabel();
			mn.setLabel(methodLabel);
			dispatchTable.remove(mn.getMethodOffset());
			dispatchTable.add(mn.getMethodOffset(), methodLabel);
			mn.codeGeneration();
		}
		// creo sullo heap la Dispatch Table che ho costruito: per ciascuna etichetta, la memorizzo a indirizzo in $hp ed incremento $hp
		String dispatchTableCode = "";
		for (String label: dispatchTable) {
			dispatchTableCode +=	"push " + label + "\n"+
									"lhp\n"+
									"sw\n"+
									"lhp\n"+
									"push 1\n"+
									"add\n"+
									"shp\n";
		}
		// metto valore di $hp sullo stack: sarà il dispatch pointer da ritornare alla fine
		return "lhp\n"+
				dispatchTableCode;
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
