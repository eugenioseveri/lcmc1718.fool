package ast;

import java.util.ArrayList;

import lib.FOOLLib;

public class CallNode implements Node {

	private String id;
	private STEntry entry;
	private ArrayList<Node> parlist = new ArrayList<Node>();
	
	public CallNode(String id, STEntry entry, ArrayList<Node> parlist) {
		super();
		this.id = id;
		this.entry = entry;
		this.parlist = parlist;
	}

	@Override
	public String toPrint(String indent) {
		String parStr = "";
		for(Node par:this.parlist) {
			parStr += par.toPrint(indent + "  ");
		}
		return indent + "Call:" + id + "\n"
				+ this.entry.toPrint(indent +  "  ")
				+ parStr;
	}

	@Override
	public Node typeCheck() {
		ArrowTypeNode atn = null;
		//controllo che l'identificatore faccia riferiemento ad una funzione
		if(this.entry.getType() instanceof ArrowTypeNode) {
			atn = (ArrowTypeNode) entry.getType(); 
		} else {
			//errore perchè sto usando l'identificatore di una variabile come se fosse una funzione
			System.out.println("Invocation of a non-function " + this.id);
			System.exit(0);
		}
		ArrayList<Node> atnParList = atn.getParlist(); 
		// Controllo che il numero dei parametri sia uguale alla dichiarazione
		if (!(atnParList.size() == this.parlist.size())) {
			System.out.println("Wrong number of parameters in the invocation of " + this.id);
			System.exit(0);
		}
		
		// Controllo che il tipo di ogni parametro attuale dentro parlist sia sottotipo dei parametri della dichiarazione)
		for(int i=0; i<this.parlist.size(); i++) {
			if(!(FOOLLib.isSubtype((this.parlist.get(i)).typeCheck(), atnParList.get(i)))) {
				System.out.println("Wrong type for " + (i+1) + "-th parameter in the invocation of " + this.id);
				System.exit(0);
			}
		}
		return atn.getRet();
	}

}
