package cnx.ast;

import cnx.symbol.Symbol;

public class Logical_and_expression {
	public Logical_and_expression _link = null;
	public Inclusive_or_expression _x = null;
	
	public Logical_and_expression(Inclusive_or_expression x){
		_x = x;
	}
	public Logical_and_expression(Logical_and_expression y, Inclusive_or_expression x){
		_x = x;
		_link = y;
	}
}