import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import ast.Node;

public class TestFOOL {
	public static void main(final String[] args) throws Exception {

		final String fileName = "test/bankloan.fool";

		final CharStream chars = CharStreams.fromFileName(fileName);
		final FOOLLexer lexer = new FOOLLexer(chars);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final FOOLParser parser = new FOOLParser(tokens);

		final Node ast = parser.prog().ast; // Generazione AST con Id associate a relative entry symbol table.

		System.out.println("You had: " + lexer.lexicalErrors + " lexical errors and "
							+ parser.getNumberOfSyntaxErrors() + " syntax errors.");

		System.out.println("Visualizing AST...");
		System.out.print(ast.toPrint(""));

		final Node type = ast.typeCheck(); // Type-checking bottom-up
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));

	}
}
