package ast;

import java.util.List;

import lib.FOOLLib;
public class ClassCallNode implements Node {
	
	private String id;
	private String method;
	private STEntry classEntry;
	private STEntry methodEntry;
	private List<Node> argList;
	private int nestingLevel;
	
	public ClassCallNode(final String id, final String method, final STEntry classEntry, final STEntry methodEntry, final List<Node> argList, final int nestingLevel) {
		this.id = id;
		this.method = method;
		this.classEntry = classEntry;
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
				this.classEntry.toPrint(indent +  "  ") + 
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Node cloneNode() {
		return null;
	}

}
