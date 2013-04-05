package cnx.ast;

import cnx.symbol.Symbol;

public class Equality_expression {
	public Relational_expression _x = null;
	public Equality_operator _eop = null;
	public Equality_expression _link = null;
	
	public Equality_expression(Relational_expression x){
		_x = x;
	}
	public Equality_expression(Equality_expression y, Equality_operator op, Relational_expression x){
		_x = x;
		_eop = op;
		_link = y;
	}
}