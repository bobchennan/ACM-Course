package cnx.ast;

import cnx.symbol.Symbol;

public class Program {

	public Prog _v = null;
	public Program _link = null;

	public Program(Program x, Prog y) {
		_link = x;
		_v = y;
	}

	public Program(Prog y){
		_link = null;
		_v = y;
	}
}