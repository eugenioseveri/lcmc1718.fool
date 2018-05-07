import java.io.BufferedWriter;
import java.io.FileWriter;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import ast.Node;

public class TestAll {
	public static void main(final String[] args) throws Exception {

		final String fileName = "test/bankloan.fool";

		final CharStream chars = CharStreams.fromFileName(fileName);
		final FOOLLexer lexer = new FOOLLexer(chars);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final FOOLParser parser = new FOOLParser(tokens);

		final Node ast = parser.prog().ast; // Generazione AST con Id associate a relative entry symbol table.

		System.out.println("You had: " + lexer.lexicalErrors + " lexical errors and " + parser.getNumberOfSyntaxErrors() + " syntax errors.");

		System.out.println("Visualizing AST...");
		System.out.print(ast.toPrint(""));

		final Node type = ast.typeCheck(); // Type-checking bottom-up
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));

		// Code generation
		final String code = ast.codeGeneration(); // Genero il codice assembly che poi la SVM utilizzerà
		final BufferedWriter out = new BufferedWriter(new FileWriter(fileName + ".asm"));
		out.write(code);
		out.close();
		System.out.println("Code generated! Assembling and running generated code.");

		// Da qui in poi è copiato da TestSVM.java
		final CharStream charsSVM = CharStreams.fromFileName(fileName + ".asm");
		final SVMLexer lexerSVM = new SVMLexer(charsSVM);
		final CommonTokenStream tokensSVM = new CommonTokenStream(lexerSVM);
		final SVMParser parserSVM = new SVMParser(tokensSVM);

		parserSVM.assembly();

		System.out.println("You had: " + lexerSVM.lexicalErrors + " lexical errors and " + parserSVM.getNumberOfSyntaxErrors() + " syntax errors.");
		if (lexerSVM.lexicalErrors > 0 || parserSVM.getNumberOfSyntaxErrors() > 0) {
			System.exit(1);
		}

		System.out.println("Starting Virtual Machine...");
		final ExecuteVM vm = new ExecuteVM(parserSVM.code);
		vm.cpu();
	}
}
