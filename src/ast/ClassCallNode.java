package ast;

import java.util.List;

import lib.FOOLLib;
public class ClassCallNode implements Node {

	private final String id;
	private final String method;
	private final STEntry entry; // STEntry dell'oggetto di tipo classe
	private final STEntry methodEntry;
	private List<Node> argList;
	private final int nestingLevel;

	public ClassCallNode(final String id, final String method, final STEntry entry, final STEntry methodEntry, final List<Node> argList, final int nestingLevel) {
		this.id = id;
		this.method = method;
		this.entry = entry;
		this.methodEntry = methodEntry;
		this.argList = argList;
		this.nestingLevel = nestingLevel;
	}

	@Override
	public String toPrint(final String indent) {
		String argStr = "";
		for (final Node arg:this.argList) {
			argStr += arg.toPrint(indent + "  ");
		}
		return indent + "ClassCall: " + this.id + "." + this.method + " at nestingLevel " + this.nestingLevel + "\n"
				+ this.entry.toPrint(indent +  "  ")
				+ this.methodEntry.toPrint(indent +  "    ")
				+ argStr;
	}

	@Override
	public Node typeCheck() {
		// Recupero il tipo di ritorno e i parametri (nel parser è già stato controllato che esista la classe e che contenga il metodo)
		final ArrowTypeNode atn = (ArrowTypeNode) this.methodEntry.getType();
		// Controllo che il numero dei parametri sia uguale alla dichiarazione
		final List<Node> atnParList = atn.getParlist();
		if (atnParList.size() != this.argList.size()) {
			System.out.println("Wrong number of parameters in the invocation of " + this.id + "." + this.method + "()");
			System.exit(0);
		}
		// Controllo che il tipo di ogni parametro attuale dentro parlist sia sottotipo dei parametri della dichiarazione
		for (int i = 0; i < this.argList.size(); i++) {
			if (!(FOOLLib.isSubtype(this.argList.get(i).typeCheck(), atnParList.get(i)))) {
				System.out.println("Wrong type for " + (i + 1) + "-th parameter in the invocation of " + this.id + "." + this.method + "()");
				System.exit(0);
			}
		}
		return atn.getRet();
	}

	@Override
	public String codeGeneration() {
		String argCode = ""; // Stringa per allocare i parametri (si parte dall'ultimo, come da struttura della memoria)
		for (int i = this.argList.size() - 1; i >= 0; i--) {
			argCode += this.argList.get(i).codeGeneration();
		}
		String getAR = ""; // Recupero l'AR in cui è dichiarata la variabile che sto usando
		for (int i = 0; i < this.nestingLevel - this.entry.getNestingLevel(); i++) {
			// Differenza di nesting level tra la posizione corrente e la dichiarazione di "id".
			// Se è una variabile locale si esegue la differenza è 0, altrimenti si deve risalire la catena statica
			getAR += "lw\n";
		}
		return  "lfp\n" // Setto il Control link (CL) settando il fp ad esso, praticamente dove punta attualmente SP
				+ argCode

				// Ora devo settare AL recuperando prima Obj-pointer
				+ "push " + (this.entry.getOffset()) + "\n"// Pusho l'offset della dichiarazione dell'oggetto nel suo AR
				+ "lfp\n" // Pusho FP (che punta all'Access Link)
				+ getAR // Risalgo la catena statica
				+ "add\n" // Sommando mi posiziono sull'OB della classe
				+ "lw\n" // Vado a prendere il valore e lo metto sullo stack, in questo modo setto l'AL per il chiamato cioè vado a dirgli che si riferisce a questo AR

				// Fino a quì ho settato la prima parte dell AR fino all' AL; ora vado a recuperare l'indirizzo del metodo nella dispatch table a cui saltare
				// Recupero prima l'object-pointer
				+ "push " + (this.entry.getOffset()) + "\n" // Pusho l'offset della dichiarazione dell'oggetto nel suo AR
				+ "lfp\n" // Pusho fp
				+ getAR // Risalgo la catena statica e raggiungo l'oggetto
				+ "add\n" // Sommando mi ci posiziono sopra con l'object pointer
				+ "lw\n" // Prendo il valore e lo metto sullo stack
				+ "lw\n" // Prendo il valore e lo metto sullo stack

				// Poi pusho l'offset del metodo
				+ "push " + this.methodEntry.getOffset() + "\n"
				+ "add\n" // Mi ci posiziono
				+ "lw\n"
				+ "js\n"; // Salto all'indirizzo che c'è sulla cima dello stack (e se lo mangia)

	}

	// Non utilizzato. Serve implementarlo solo per via dell'interfaccia
	@Override
	public Node cloneNode() {
		throw new UnsupportedOperationException("Metodo cloneNode() in ClassCallNode richiamato erroneamente.");
	}

}
