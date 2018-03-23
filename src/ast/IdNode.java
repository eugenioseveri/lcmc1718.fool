package ast;

public class IdNode implements Node {

	private String id;
	private STEntry entry;
	private int nestingLevel;

	public IdNode(String id,  STEntry entry, int nestingLevel) {
		super();
		this.id = id;
		this.entry = entry;
		this.nestingLevel = nestingLevel;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Id:" + this.id + "at nestingLevel " + this.nestingLevel + "\n"
				+ this.entry.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controllare il caso in cui non è una variabile ma una funzione, erroneamente usata come variabile (senza parentesi tonde)
		/*if(this.entry.getType() instanceof ArrowTypeNode) {
			// Errore perché sto usando l'identificatore di una funzione come se fosse una variabile
			System.out.println("Wrong usage of function identifier!");
			System.exit(0);
		}*/
		return this.entry.getType();
	}

	@Override
	public String codeGeneration() {
		String getAR = ""; // Recupero l'AR in cui è dichiarata la variabile che sto usando
		for(int i=0; i<this.nestingLevel - this.entry.getNestingLevel(); i++) {
			// Differenza di nesting level tra la posizione corrente e la dichiarazione di "id".
			// Se è una variabile locale si esegue la differenza è 0, altrimenti si deve risalire la catena statica
			getAR += "lw\n";
		}
		
		String functionalAdditionalCode = ""; // TODO refactor nome variabile
		// Se il nodo è di tipo funzionale deve essere recuperato anche l'indirizzo della funzione
		if (this.entry.getType() instanceof ArrowTypeNode) {
			functionalAdditionalCode =  "push " + (this.entry.getOffset()-1) + "\n" // Mette l'offset sullo stack
						+ "lfp\n" // Mette l'indirizzo puntato dal registro FP sullo stack (l'indirizzo dell'AR della variabile)
						+ getAR // Risalgo la catena statica
						+ "add\n" // Li somma (aggiunge l'offset al registro FP)
						+ "lw\n"; // Carica sullo stack il valore all'indirizzo ottenuto
		}
		
		return "push " + this.entry.getOffset() + "\n" // Mette l'offset sullo stack
				+ "lfp\n" // Mette l'indirizzo puntato dal registro FP sullo stack (l'indirizzo dell'AR della variabile)
				+ getAR // Risalgo la catena statica
				+ "add\n" // Li somma (aggiunge l'offset al registro FP)
				+ "lw\n" // Carica sullo stack il valore all'indirizzo ottenuto
				+ functionalAdditionalCode;
	}
}
