package cnx.ast;

import cnx.symbol.Symbol;

public class Constant_expressions {
	public Constant_expressions _link = null;
	public Constant_expression _x = null;
	
	public Constant_expressions(Constant_expression x){
		_x = x;
	}
	public Constant_expressions(Constant_expressions y, Constant_expression x){
		_link = y;
		_x = x;
	}
}