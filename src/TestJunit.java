import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ast.Node;

/**
 * Classe che automatizza i test sul compilatore.
 */
public class TestJunit {

	private Node ast;
	private LoggedPrintStream lpsOut; // Redirect dello standard output per leggerlo da Java.
	
	@Before
	public void initStreams() {
		/* Prima dell'inizio di ogni test, intercetta lo standard output. */
		this.lpsOut = LoggedPrintStream.create(System.out);
		System.setOut(this.lpsOut);
	}
	
	@After
	public void restoreStandardStreams() {
		/* Alla fine di ogni test, ripristina lo standard output. */
		System.setOut(this.lpsOut.underlying);
	}
	
	@Test
	public void testSvmOnlyProva() {
		/* Test della SVM con il file "prova.fool.asm" (già compilato). Output atteso: "2". */
		try {
			runSvm("test/prova.fool");
		} catch (IOException e) {
			fail("IOException in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		final String[] lines = this.lpsOut.buffer.toString().split("\\n");
		assertTrue(lines[0].startsWith("You had: 0 lexical errors and 0 syntax errors.\r"));
		assertTrue(lines[2].startsWith("2\r"));
	}
	
	@Test
	public void testSvmOnlyQuicksort() {
		/* Test della SVM con il file "quicksort.fool.asm" (già compilato). Output atteso: "1 2 2 3 4 5". */
		try {
			runSvm("test/quicksort.fool");
		} catch (IOException e) {
			fail("IOException in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		final String[] lines = this.lpsOut.buffer.toString().split("\\n");
		assertTrue(lines[0].startsWith("You had: 0 lexical errors and 0 syntax errors.\r"));
		assertTrue(lines[2].startsWith("1\r"));
		assertTrue(lines[3].startsWith("2\r"));
		assertTrue(lines[4].startsWith("2\r"));
		assertTrue(lines[5].startsWith("3\r"));
		assertTrue(lines[6].startsWith("4\r"));
		assertTrue(lines[7].startsWith("5\r"));
	}
	
	@Test
	public void testFoolBaseOperators() {
		/* Test della prima estensione del progetto (operatori ">=", ">=", "||", "&&", "/", "-", "!". */
		fail("TODO");
	}
	
	@Test
	public void testFoolHigherOrder() {
		/* Test dell'estensione higher-order ("linsum.fool"). */
		fail("TODO");
	}
	
	@Test
	public void testFoolObjectOrientedNoInheritance() {
		/* Test dell'estensione object-oriented senza ereditarietà ("quicksort.fool"). */
		fail("TODO");
	}
	
	@Test
	public void testFoolObjectOrientedWithInheritance() {
		/* Test dell'estensione object-oriented con ereditarietà ("bankloan.fool"). */
		fail("TODO");
	}
	
	@Test
	public void testFoolHigherOrderAndObjectOrientedNoInheritance() {
		/* Test dell'estensione higher-order e object-oriented senza ereditarietà integrate ("quicksort_ho.fool"). */
		fail("TODO");
	}
	
	/**
	 * Metodo di utilità che compila un file sorgente FOOL e lo esegue.
	 * @param fileName Il file sorgente con estensione ".fool".
	 * @throws IOException
	 */
	public void runAll(final String fileName) throws IOException {
		runFool(fileName);
		generateCode(fileName);
		runSvm(fileName);
	}
	
	/* Esegue la SVM con il file assembly specificato. */
	private void runSvm(final String fileName) throws IOException {
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
	
	/* Esegue il parsing di un file sorgente FOOL specificato e ne esegue il type-checking. */
	private void runFool(final String fileName) throws IOException {
		CharStream chars = CharStreams.fromFileName(fileName);
		FOOLLexer lexer = new FOOLLexer(chars);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FOOLParser parser = new FOOLParser(tokens);

		this.ast = parser.prog().ast; // Generazione AST con Id associate a relative entry symbol table.

		System.out.println("You had: " + lexer.lexicalErrors + " lexical errors and "
							+ parser.getNumberOfSyntaxErrors() + " syntax errors.");

		System.out.println("Visualizing AST...");
		System.out.print(this.ast.toPrint(""));
		
		Node type = this.ast.typeCheck(); // Type-checking bottom-up
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));
	}
	
	/* Compila un file sorgente FOOL di cui è stato eseguito il parsing. */
	private void generateCode(final String fileName) throws IOException {
		// Code generation (prova.fool.asm)
		String code = this.ast.codeGeneration(); // Genero il codice assembly che poi la SVM utilizzerà
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName + ".asm"));
		out.write(code);
		out.close();
		System.out.println("Code generated! Assembling and running generated code.");
	}
	
	/**
	 * Classe che consente il redirezionamento di un'output stream.
	 */
	static class LoggedPrintStream extends PrintStream {
		final StringBuilder buffer;
		final PrintStream underlying;
		
		LoggedPrintStream(StringBuilder sb, OutputStream os, PrintStream ul) {
			super(os);
			this.buffer = sb;
			this.underlying = ul;
		}
		
		public static LoggedPrintStream create(PrintStream toLog) {
			try {
				final StringBuilder sb = new StringBuilder();
				Field f = FilterOutputStream.class.getDeclaredField("out");
				f.setAccessible(true);
				OutputStream psout = (OutputStream) f.get(toLog);
				return new LoggedPrintStream(sb, new FilterOutputStream(psout) {
					public void write(int b) throws IOException {
						super.write(b);
						sb.append((char) b);
					}
				}, toLog);
			} catch (NoSuchFieldException ex) { // Queste eccezioni non dovrebbero verificarsi mai
			} catch (IllegalArgumentException ex) {
			} catch (IllegalAccessException ex) {
			}
			return null;
		}
	}
}
