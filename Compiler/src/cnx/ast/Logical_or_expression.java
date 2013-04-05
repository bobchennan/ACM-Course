package cnx.ast;

import cnx.symbol.Symbol;

public class Logical_or_expression extends Constant_expression {
	public Logical_or_expression _link = null;
	public Logical_and_expression _x = null;
	
	public Logical_or_expression(Logical_and_expression x){
		_x = x;
	}
	public Logical_or_expression(Logical_or_expression y, Logical_and_expression x){
		_x = x;
		_link = y;
	}
}