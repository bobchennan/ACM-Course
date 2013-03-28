package appetizer.ast;

import appetizer.symbol.Symbol;

public class Program {

	public Prog v = null;
	public Program link = null;

	public Program(Program x, Prog y) {
		link = x;
		v = y;
	}

	public Program(Prog y){
		link = null;
		v = y;
	}
}