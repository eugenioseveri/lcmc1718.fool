import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
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
		} catch (Exception e) {
			fail("Exception in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		final String[] lines = this.lpsOut.buffer.toString().replace("\r","").split("\\n");
		assertTrue(lines[0].compareTo("[SVM] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[2].compareTo("2")==0);
	}
	
	@Test
	public void testSvmOnlyQuicksort() {
		/* Test della SVM con il file "quicksort.fool.asm" (già compilato). Output atteso: "1 2 2 3 4 5". */
		try {
			runSvm("test/quicksort.fool");
		} catch (Exception e) {
			fail("Exception in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		String[] lines = this.lpsOut.buffer.toString().replace("\r","").split("\\n");
		assertTrue(lines[0].compareTo("[SVM] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[2].compareTo("1")==0);
		assertTrue(lines[3].compareTo("2")==0);
		assertTrue(lines[4].compareTo("2")==0);
		assertTrue(lines[5].compareTo("3")==0);
		assertTrue(lines[6].compareTo("4")==0);
		assertTrue(lines[7].compareTo("5")==0);
	}
	
	@Test
	public void testFoolBaseOperators() {
		/* Test della prima estensione del progetto (operatori ">=", ">=", "||", "&&", "/", "-", "!"). (baseoperators.fool) */
		try {
			runAll("test/baseoperators.fool");
		} catch (Exception e) {
			fail("Exception in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		String[] lines = this.lpsOut.buffer.toString().replace("\r","").split("\\n");
		assertTrue(lines[0].compareTo("[FOOL] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[301].compareTo("Type checking ok! Type of the program is: IntType")==0);
		assertTrue(lines[303].compareTo("Code generated! Assembling and running generated code.")==0);
		assertTrue(lines[304].compareTo("[SVM] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[306].compareTo("0")==0);
	}
	
	@Test
	public void testFoolHigherOrder() {
		/* Test dell'estensione higher-order ("linsum.fool"). */
		try {
			runAll("test/linsum.fool");
		} catch (Exception e) {
			fail("Exception in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		String[] lines = this.lpsOut.buffer.toString().replace("\r","").split("\\n");
		assertTrue(lines[0].compareTo("[FOOL] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[81].compareTo("Type checking ok! Type of the program is: IntType")==0);
		assertTrue(lines[83].compareTo("Code generated! Assembling and running generated code.")==0);
		assertTrue(lines[84].compareTo("[SVM] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[86].compareTo("24")==0);
	}
	
	@Test
	public void testFoolObjectOrientedNoInheritance() {
		/* Test dell'estensione object-oriented senza ereditarietà ("quicksort.fool"). */
		try {
			runAll("test/quicksort.fool");
		} catch (Exception e) {
			fail("Exception in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		String[] lines = this.lpsOut.buffer.toString().replace("\r","").split("\\n");
		assertTrue(lines[0].compareTo("[FOOL] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[476].compareTo("Type checking ok! Type of the program is: RefType List")==0);
		assertTrue(lines[478].compareTo("Code generated! Assembling and running generated code.")==0);
		assertTrue(lines[479].compareTo("[SVM] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[481].compareTo("1")==0);
		assertTrue(lines[482].compareTo("2")==0);
		assertTrue(lines[483].compareTo("2")==0);
		assertTrue(lines[484].compareTo("3")==0);
		assertTrue(lines[485].compareTo("4")==0);
		assertTrue(lines[486].compareTo("5")==0);
	}
	
	@Test
	public void testFoolObjectOrientedWithInheritance() {
		/* Test dell'estensione object-oriented con ereditarietà ("bankloan.fool"). */
		try {
			runAll("test/bankloan.fool");
		} catch (Exception e) {
			fail("Exception in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		String[] lines = this.lpsOut.buffer.toString().replace("\r","").split("\\n");
		assertTrue(lines[0].compareTo("[FOOL] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[203].compareTo("Type checking ok! Type of the program is: IntType")==0);
		assertTrue(lines[205].compareTo("Code generated! Assembling and running generated code.")==0);
		assertTrue(lines[206].compareTo("[SVM] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[208].compareTo("50000")==0);
	}
	
	@Test
	public void testFoolHigherOrderAndObjectOrientedNoInheritance() {
		/* Test dell'estensione higher-order e object-oriented senza ereditarietà integrate ("quicksort_ho.fool"). */
		try {
			runAll("test/quicksort_ho.fool");
		} catch (Exception e) {
			fail("Exception in runSvm()");
			e.printStackTrace();
		}
		System.out.flush();
		String[] lines = this.lpsOut.buffer.toString().replace("\r","").split("\\n");
		assertTrue(lines[0].compareTo("[FOOL] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[576].compareTo("Type checking ok! Type of the program is: RefType List")==0);
		assertTrue(lines[578].compareTo("Code generated! Assembling and running generated code.")==0);
		assertTrue(lines[579].compareTo("[SVM] You had: 0 lexical errors and 0 syntax errors.")==0);
		assertTrue(lines[581].compareTo("5")==0);
		assertTrue(lines[582].compareTo("4")==0);
		assertTrue(lines[583].compareTo("3")==0);
		assertTrue(lines[584].compareTo("2")==0);
		assertTrue(lines[585].compareTo("2")==0);
		assertTrue(lines[586].compareTo("1")==0);
	}
	
	/**
	 * Metodo di utilità che compila un file sorgente FOOL e lo esegue.
	 * @param fileName Il file sorgente con estensione ".fool".
	 * @throws IOException
	 */
	private void runAll(final String fileName) throws IOException {
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

		System.out.println("[SVM] You had: " + lexerSVM.lexicalErrors + " lexical errors and " + parserSVM.getNumberOfSyntaxErrors() + " syntax errors.");
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

		System.out.println("[FOOL] You had: " + lexer.lexicalErrors + " lexical errors and "
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
	 * Classe che consente il redirezionamento di un'output stream. Decorator di "PrintStream".
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
