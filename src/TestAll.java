import java.io.BufferedWriter;
import java.io.FileWriter;

import org.antlr.v4.runtime.*;
import ast.Node;

public class TestAll {
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

		// Code generation (prova.fool.asm)
		String code=ast.codeGeneration(); // Ggenero il codice assembly che poi la SVM utilizzerà
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName + ".asm"));
		out.write(code);
		out.close();
		System.out.println("Code generated! Assembling and running generated code.");

		// Da qui in poi è copiato da TestSVM.java
		CharStream charsSVM = CharStreams.fromFileName(fileName + ".asm");
		SVMLexer lexerSVM = new SVMLexer(charsSVM);
		CommonTokenStream tokensSVM = new CommonTokenStream(lexerSVM);
		SVMParser parserSVM = new SVMParser(tokensSVM);

		parserSVM.assembly();

		System.out.println("You had: " + lexerSVM.lexicalErrors + " lexical errors and " + parserSVM.getNumberOfSyntaxErrors() + " syntax errors.");
		if (lexerSVM.lexicalErrors > 0 || parserSVM.getNumberOfSyntaxErrors() > 0) {
			System.exit(1);
		}

		System.out.println("Starting Virtual Machine...");
		ExecuteVM vm = new ExecuteVM(parserSVM.code);
		vm.cpu();
	}
}
