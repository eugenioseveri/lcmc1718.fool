package ast;

import java.util.ArrayList;
import java.util.List;

import lib.FOOLLib;
import lib.FOOLLib.MethodInheritanceType;

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
			//Ottimizzazione, effettuato il type check solo in caso di metodo nuovo o riscritto
			if (((MethodNode)n).getMit() != MethodInheritanceType.INHERIT) {
				n.typeCheck();
			}
		}
		
		if (this.superEntry != null) {
			
			//Campi e metodi della classe padre
			List<Node> superFields = ((ClassTypeNode)this.superEntry.getType()).getAllFields();
			List<Node> superMethods = ((ClassTypeNode)this.superEntry.getType()).getAllMethods();
			
			//Campi e metodi della classe figlio
			List<Node> localFields = ((ClassTypeNode)this.symType).getAllFields();
			List<Node> localMethods = ((ClassTypeNode)this.symType).getAllMethods();
			
			//Ottimizzazione, effettuato il subtyping solo in caso di metodo riscritto
			for (int i = 0; i < localMethods.size(); i++) {
				if (((MethodNode)localMethods.get(i)).getMit() == MethodInheritanceType.OVERRIDE) {
					if (!FOOLLib.isSubtype(((MethodNode)localMethods.get(i)).getSymType(),((MethodNode)superMethods.get(i)).getSymType())){
						System.out.println("Error subtyping method " + ((MethodNode)localMethods.get(i)).getId() + " on Class: " + this.id + " at index " + i + " !");
						System.exit(0);
					}
				}
			}
			
			//Type Checking più efficiente per ClassNode (Soluzione richiesta nella parte di ottimizzazione)
			for (int i = 0; i < localFields.size(); i++) {
				int offset = ((FieldNode)localFields.get(i)).getOffset();
				if ((-offset-1) < superFields.size()) {
					if (!FOOLLib.isSubtype(((FieldNode)localFields.get((-offset-1))).getSymType(),((FieldNode)superFields.get((-offset-1))).getSymType())){
						System.out.println("Error subtyping field " + ((FieldNode)localFields.get((-offset-1))).getId() + " on Class: " + this.id + " at index " + (-offset-1) + " !");
						System.exit(0);
					}
				}
			}
			
			/*
			for (int i = 0; i < localMethods.size(); i++) {
				int offset = ((MethodNode)localMethods.get(i)).getMethodOffset();
				if ((offset) < superMethods.size()) {
					if (!FOOLLib.isSubtype(((MethodNode)localMethods.get(offset)).getSymType(),((MethodNode)superMethods.get(offset)).getSymType())){
						System.out.println("Error subtyping method " + ((MethodNode)localMethods.get(offset)).getId() + " on Class: " + this.id + " at index " + offset + " !");
						System.exit(0);
					}
				}
			}
			*/
			
			
			/* Soluzione utilizzata prima dell'ottimizzazione 
			//Controllo che i campi della classe figlio siano sottotipo della classe padre
			for(int i = 0; i < superFields.size(); i++) {
				if (!FOOLLib.isSubtype(((FieldNode)localFields.get(i)).getSymType(),((FieldNode)superFields.get(i)).getSymType())){
					System.out.println("Error subtyping field " + ((FieldNode)localFields.get(i)).getId() + " on Class: " + this.id + " at index " + i + " !");
					System.exit(0);
				}
			}
			
			//Controllo che i metodi della classe figlio siano sottotipo della classe padre
			for(int i = 0; i < superMethods.size(); i++) {
				if (!FOOLLib.isSubtype(((MethodNode)localMethods.get(i)).getSymType(),((MethodNode)superMethods.get(i)).getSymType())){
					System.out.println("Error subtyping method " + ((MethodNode)localMethods.get(i)).getId() + " on Class: " + this.id + " at index " + i + " !");
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
			//Ottimizzazione, generazione di codice effettuata solo in caso di metodo nuovo o riscritto
			if (mn.getMit() == MethodInheritanceType.INHERIT) {
				//prelevo la label del metodo nella dispatch table della classe padre, cosi da saltare allo stesso indirizzo del metodo padre
				methodLabel = FOOLLib.getDispatchTable().get(-this.superEntry.getOffset()-2).get(mn.getMethodOffset());
				mn.setLabel(methodLabel);
				dispatchTable.set(mn.getMethodOffset(), methodLabel);
			} else {
				//Metodo nuovo o riscritto, procedo normalmente alla generazione di label e codice
				methodLabel = FOOLLib.freshMethodLabel();
				mn.setLabel(methodLabel);
				dispatchTable.set(mn.getMethodOffset(), methodLabel);
				mn.codeGeneration();
			}
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
