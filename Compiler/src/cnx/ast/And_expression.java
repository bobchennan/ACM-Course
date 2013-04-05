package cnx.ast;

import cnx.symbol.Symbol;

public class And_expression {
	public And_expression _link = null;
	public Equality_expression _x = null;
	
	public And_expression(Equality_expression x){
		_x = x;
	}
	public And_expression(And_expression y, Equality_expression x){
		_x = x;
		_link = y;
	}
}