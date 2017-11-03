public class ExecuteVM {
    
    public static final int CODESIZE = 10000;
    public static final int MEMSIZE = 10000;
    
    private int[] code;
    private int[] memory = new int[MEMSIZE];
    
    private int ip = 0; // instruction pointer
    private int sp = MEMSIZE; // stack pointer
    private int hp = 0; // heap pointer
    private int fp = 0; // frame pointer
    private int ra; // return address
    private int rv; // return value
        
    public ExecuteVM(int[] code) {
      this.code = code;
    }
    
    public void cpu() {
      while ( true ) {
        int bytecode = code[ip++]; // fetch
        int v1,v2;
        int address;
        switch ( bytecode ) {
          case SVMParser.PUSH:
        	  push(code[ip++]);
        	  break;
          case SVMParser.POP:
        	  pop();
        	  break;
          case SVMParser.ADD:
        	  v1 = pop();
        	  v2 = pop();
        	  push(v2+v1);
        	  break;
          case SVMParser.SUB:
        	  v1 = pop();
        	  v2 = pop();
        	  push (v2-v1);
        	  break;
          case SVMParser.MULT:
        	  v1 = pop();
        	  v2 = pop();
        	  push (v2*v1);
        	  break;
          case SVMParser.DIV:
        	  v1 = pop();
        	  v2 = pop();
        	  push (v2/v1);
        	  break;
          case SVMParser.STOREW:
        	  //TODO
        	  break;
          case SVMParser.LOADW:
        	  //TODO
        	  break;
          case SVMParser.BRANCH:
        	  address = code[ip];
        	  ip = address;
        	  break;
          case SVMParser.BRANCHEQ:
        	  //TODO
        	  break;
          case SVMParser.BRANCHLESSEQ:
        	  address = code[ip++];
        	  v1 = pop();
        	  v2 = pop();
        	  if(v2 <= v1) {
        		  ip = address;
        	  }
        	  break;
          case SVMParser.JS:
        	  //TODO
        	  break;
          case SVMParser.LOADRA:
        	  //TODO
        	  break;
          case SVMParser.STORERA:
        	  //TODO
        	  break;
          case SVMParser.LOADRV:
        	  //TODO
        	  break;
          case SVMParser.STORERV:
        	  //TODO
        	  break;
          case SVMParser.LOADFP:
        	  //TODO
        	  break;
          case SVMParser.STOREFP:
        	  //TODO
        	  break;
          case SVMParser.COPYFP:
        	  //TODO
        	  break;
          case SVMParser.LOADHP:
        	  //TODO
        	  break;
          case SVMParser.STOREHP:
        	  //TODO
        	  break;
          case SVMParser.PRINT: // Controllare che lo stack non sia vuoto, altrimenti dà eccezione.
        	  System.out.println((sp<MEMSIZE) ? memory[sp] : "Empty stack!");
        	  break;
          case SVMParser.HALT:
        	  return;
        }
      }
    } 
    
    private int pop() {
      return memory[sp++];
    }
    
    private void push(int v) {
      memory[--sp] = v;
    }
    
}