
import java.io.*;
import org.antlr.v4.runtime.*;


public class TestSimpleExpLeft {
    public static void main(String[] args) throws Exception {
        String fileName = "prova.txt"; // Il sorgente che vogliamo compilare
    	CharStream chars = CharStreams.fromFileName(fileName);
    	SimpleExpLeftLexer lexer = new SimpleExpLeftLexer(chars);
    	CommonTokenStream tokens = new CommonTokenStream(lexer);
    	SimpleExpLeftParser parser = new SimpleExpLeftParser(tokens);
    	/* Si deve lanciare il parser (passandogli il simbolo d'inizio), che a sua volta chiama il lexer
    	 * (che gli passa i token), che a sua volta legge i caratteri. */
    	parser.prog();
    	System.out.println("Left! You had " + lexer.lexicalErrors + " lexical errors and " +
    						parser.getNumberOfSyntaxErrors() + " syntax errors.");
    }
}
