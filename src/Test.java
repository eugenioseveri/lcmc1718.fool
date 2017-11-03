import org.antlr.v4.runtime.*;

public class Test {
    public static void main(String[] args) throws Exception {
     
        String fileName = "prova.asm";
                
        CharStream chars = CharStreams.fromFileName(fileName);
        SVMLexer lexer = new SVMLexer(chars);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SVMParser parser = new SVMParser(tokens);
        
        
        parser.assembly();
        
        System.out.println("You had: "+lexer.lexicalErrors+" lexical errors and "+parser.getNumberOfSyntaxErrors()+" syntax errors.");
        if (lexer.lexicalErrors>0 || parser.getNumberOfSyntaxErrors()>0) System.exit(1);

        System.out.println("Starting Virtual Machine...");
        ExecuteVM vm = new ExecuteVM(parser.code);
        vm.cpu();
        
      
    }
}
