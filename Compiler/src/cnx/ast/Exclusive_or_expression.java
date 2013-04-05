package cnx.ast;

import cnx.symbol.Symbol;

public class Exclusive_or_expression {
	public Exclusive_or_expression _link = null;
	public And_expression _x = null;
	
	public Exclusive_or_expression(And_expression x){
		_x = x;
	}
	public Exclusive_or_expression(Exclusive_or_expression y, And_expression x){
		_x = x;
		_link = y;
	}
}