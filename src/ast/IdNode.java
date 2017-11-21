package ast;

public class IdNode implements Node {

	public String id;
	private STEntry entry;
	
	public IdNode(String id, STEntry entry) {
		super();
		this.id = id;
		this.entry = entry;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Id:" + this.id + "\n" + entry.toPrint(indent + "  ");
	}

	@Override
	public Node typeCheck() {
		// Controllare il caso in cui non è una variabile ma una funzione, erroneamente usata come variabile (senza parentesi tonde)
		if(this.entry.getType() instanceof ArrowTypeNode) {
			//errore perchè sto usando l'identificatore di una funzione come se fosse una variabile
			System.out.println("Wrong usage of function identifier!");
			System.exit(0);
		}
		return this.entry.getType();
	}
}
