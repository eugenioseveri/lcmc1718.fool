public class ExecuteVM {

	public static final int CODESIZE = 10000;
	public static final int MEMSIZE = 10000;

	private final int[] code;
	private int[] memory = new int[MEMSIZE];

	private int ip = 0; // Instruction Pointer
	private int sp = MEMSIZE; // Stack Pointer (memoria dall'alto verso il basso) (N.B. se durante l'esecuzione SP è uguale a MEMSIZE allora lo stack è vuoto)
	private int hp = 0; // Heap Pointer (memoria dal basso verso l'alto)
	private int fp = MEMSIZE; // Frame Pointer
	private int ra; // Return Address
	private int rv; // Return Value

	public ExecuteVM(final int[] code) {
		this.code = code;
	}

	public final void cpu() { // Ciclo fetch-execute
		while (true) { // Si interrompe solo con l'istruzione halt
			final int bytecode = this.code[this.ip++]; // Fetch
			int v1, v2;
			int address;
			switch (bytecode) { // Execute
			case SVMParser.PUSH:
				push(this.code[this.ip++]);
				break;
			case SVMParser.POP:
				pop();
				break;
			case SVMParser.ADD:
				v1 = pop();
				v2 = pop();
				push(v2 + v1);
				break;
			case SVMParser.SUB:
				v1 = pop();
				v2 = pop();
				push(v2 - v1); // è importante l'ordine della sottrazione perchè il primo valore sullo stack è in realtà il secondo operando
				break;
			case SVMParser.MULT:
				v1 = pop();
				v2 = pop();
				push(v2 * v1);
				break;
			case SVMParser.DIV:
				v1 = pop();
				v2 = pop();
				push(v2 / v1);
				break;
			case SVMParser.STOREW:
				v1 = pop();
				v2 = pop();
				this.memory[v1] = v2;
				break;
			case SVMParser.LOADW:
				push(this.memory[pop()]);
				break;
			case SVMParser.BRANCH:
				address = this.code[this.ip];
				this.ip = address; // Assegno address all'instruction pointer così salto all'istruzione desiderata
				break;
			case SVMParser.BRANCHEQ:
				address = this.code[this.ip++];
				v1 = pop();
				v2 = pop();
				if (v2 == v1) {
					this.ip = address;
				}
				break;
			case SVMParser.BRANCHLESSEQ:
				address = this.code[this.ip++];
				v1 = pop();
				v2 = pop();
				if (v2 <= v1) {
					this.ip = address;
				}
				break;
			case SVMParser.JS:
				v1 = pop();
				this.ra = this.ip;
				this.ip = v1;
				break;
			case SVMParser.LOADRA:
				push(this.ra);
				break;
			case SVMParser.STORERA:
				this.ra = pop();
				break;
			case SVMParser.LOADRV:
				push(this.rv);
				break;
			case SVMParser.STORERV:
				this.rv = pop();
				break;
			case SVMParser.LOADFP:
				push(this.fp);
				break;
			case SVMParser.STOREFP:
				this.fp = pop();
				break;
			case SVMParser.COPYFP:
				this.fp = this.sp;
				break;
			case SVMParser.LOADHP:
				push(this.hp);
				break;
			case SVMParser.STOREHP:
				this.hp = pop();
				break;
			case SVMParser.PRINT: // Controllare che lo stack non sia vuoto, altrimenti dà eccezione.
				System.out.println(this.sp < MEMSIZE ? this.memory[this.sp] : "Empty stack!");
				break;
			case SVMParser.HALT:
				return;
			}
		}
	}

	private int pop() {
		return this.memory[this.sp++];
	}

	private void push(final int v) {
		this.memory[--this.sp] = v;
	}

}
