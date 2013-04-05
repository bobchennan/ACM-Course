package cnx.ast;

import cnx.symbol.Symbol;

public class Multiplicative_expression {
	public Cast_expression _x = null;
	public Multiplicative_operator _mop = null;
	public Multiplicative_expression _link = null;
	
	public Multiplicative_expression(Cast_expression x){
		_x = x;
	}
	public Multiplicative_expression(Multiplicative_expression y, Multiplicative_operator op, Cast_expression x){
		_x = x;
		_mop = op;
		_link = y;
	}
}