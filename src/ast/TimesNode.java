package ast;

public class TimesNode implements Node {

	public Node left;
	public Node right;
	
	public TimesNode(Node left, Node right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public String toPrint(String indent) {
		return indent + "Times\n"
				+ this.left.toPrint(indent + "  ")
				+ right.toPrint(indent + "  ");
	}
}
