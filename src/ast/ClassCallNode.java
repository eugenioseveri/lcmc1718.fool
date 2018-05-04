package ast;

import java.util.List;

import lib.FOOLLib;
public class ClassCallNode implements Node {
	
	private String id;
	private String method;
	private STEntry entry; //STEntry dell'oggetto di tipo classe
	private STEntry methodEntry;
	private List<Node> argList;
	private int nestingLevel;
	
	public ClassCallNode(final String id, final String method, final STEntry entry, final STEntry methodEntry, final List<Node> argList, final int nestingLevel) {
		this.id = id;
		this.method = method;
		this.entry = entry;
		this.methodEntry = methodEntry;
		this.argList = argList;
		this.nestingLevel = nestingLevel;
	}

	@Override
	public String toPrint(String indent) {
		String argStr = "";
		for(Node arg:this.argList) {
			argStr += arg.toPrint(indent + "  ");
		}
		return indent + "ClassCall: " + this.id + "." + this.method + " at nestingLevel " + this.nestingLevel + "\n"+
				this.entry.toPrint(indent +  "  ") + 
				this.methodEntry.toPrint(indent +  "    ") +
				argStr;
	}

	@Override
	public Node typeCheck() {
		// Recupero il tipo di ritorno e i parametri (nel parser è già stato controllato che esista la classe e che contenga il metodo)
		ArrowTypeNode atn = (ArrowTypeNode) this.methodEntry.getType();
		
		// Controllo che il numero dei parametri sia uguale alla dichiarazione
		List<Node> atnParList = atn.getParlist();
		if (atnParList.size() != this.argList.size()) {
			System.out.println("Wrong number of parameters in the invocation of " + this.id + "." + this.method + "()");
			System.exit(0);
		}

		// Controllo che il tipo di ogni parametro attuale dentro parlist sia sottotipo dei parametri della dichiarazione
		for(int i=0; i<this.argList.size(); i++) {
			if(!(FOOLLib.isSubtype((this.argList.get(i)).typeCheck(), atnParList.get(i)))) {
				System.out.println("Wrong type for " + (i+1) + "-th parameter in the invocation of " + this.id + "." + this.method + "()");
				System.exit(0);
			}
		}
		return atn.getRet();
	}

	@Override
	public String codeGeneration() {
		String getAR = ""; // Recupero l'AR in cui è dichiarata la variabile che sto usando
		for(int i=0; i<this.nestingLevel - this.entry.getNestingLevel(); i++) {
			// Differenza di nesting level tra la posizione corrente e la dichiarazione di "id".
			// Se è una variabile locale si esegue la differenza è 0, altrimenti si deve risalire la catena statica
			getAR += "lw\n";
		}
		
		System.out.println("Code generation: ClassCallNode: " + this.id + "." + this.method + " methodEntryOffset:" + this.methodEntry.getOffset());
		
		return  "lfp\n" + //Setto il Control link (CL) settando il fp ad esso, praticamente dove punta attualmente sp
				/*------------Ora devo settare AL recuperando prima Obj-pointer---------------*/
			     "push " + (this.entry.getOffset()) + "\n"//pusho l'offset della dichiarazione dell'oggetto nel suo AR
			     + "lfp\n" // pusho FP (che punta all'Access Link)
			     + getAR // mi permette di risalire la catena statica
			     + "add\n" // sommando mi posiziono sull'OB della classe        
			     + "lw\n" // vado a prendere il valore e lo metto sullo stack, in questo modo setto l'AL per il chiamato cioè vado a dirgli che si riferisce a questo AR   
			     
			     /*----Fino a quì ho settato la prima parte dell AR fino all' AL----*/
			     //ora vado a recuperare l'indirizzo del metodo nella dispatch table a cui saltare
			     
			     //Recupero prima l'object-pointer
			     + "push " + (this.entry.getOffset()) + "\n" //pusho l'offset della dichiarazione dell'oggetto nel suo AR
			     + "lfp\n" //Pusho fp
			     + getAR // risalgo la catena statica e raggiungo l'oggetto
			     + "add\n" //Sommando mi ci posiziono sopra con l'object pointer
			     + "lw\n" //Prendo il valore e lo metto sullo stack
			  
			     + "lw\n" //TODO capire perchè?
			     
			     //Poi pusho l'offset del metodo
			     + "push " + this.methodEntry.getOffset() +"\n"
			     + "add\n" //Mi ci posiziono
			     + "lw\n"
			     + "js\n"; //salto all'indirizzo che c'è sulla cima dello stack (e se lo mangia)

	}
	
	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node cloneNode() {
		System.err.println("CloneNotSupportedException! " + this.id + "." + this.method + "() methodEntryOffset:" + this.methodEntry.getOffset());
		return null;
	}

}
