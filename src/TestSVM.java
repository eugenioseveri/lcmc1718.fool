import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class TestSVM {
	public static void main(final String[] args) throws Exception {

		final String fileName = "test/bankloan_loro.fool.asm";

		final CharStream charsSVM = CharStreams.fromFileName(fileName);
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
