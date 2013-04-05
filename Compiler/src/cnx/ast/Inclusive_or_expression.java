package cnx.ast;

import cnx.symbol.Symbol;

public class Inclusive_or_expression {
	public Inclusive_or_expression _link = null;
	public Exclusive_or_expression _x = null;
	
	public Inclusive_or_expression(Exclusive_or_expression x){
		_x = x;
	}
	public Inclusive_or_expression(Inclusive_or_expression y, Exclusive_or_expression x){
		_x = x;
		_link = y;
	}
}