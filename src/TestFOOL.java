import org.antlr.v4.runtime.*;
import ast.Node;

public class TestFOOL {
	public static void main(String[] args) throws Exception {

		String fileName = "prova.fool";

		CharStream chars = CharStreams.fromFileName(fileName);
		FOOLLexer lexer = new FOOLLexer(chars);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FOOLParser parser = new FOOLParser(tokens);

		Node ast = parser.prog().ast; // Generazione AST con Id associate a relative entry symbol table.

		System.out.println("You had: " + lexer.lexicalErrors + " lexical errors and "
							+ parser.getNumberOfSyntaxErrors() + " syntax errors.");

		System.out.println("Visualizing AST...");
		System.out.print(ast.toPrint(""));
		
		Node type = ast.typeCheck(); // Type-checking bottom-up
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));

	}
}
