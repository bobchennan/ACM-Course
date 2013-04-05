package cnx.ast;

import cnx.symbol.Symbol;

public class Additive_expression {
	public Additive_expression _link = null;
	public Additive_operator _aop = null;
	public Multiplicative_expression _x = null;
	
	public Additive_expression(Multiplicative_expression x){
		_x = x;
	}
	public Additive_expression(Additive_expression y, Additive_operator op, Multiplicative_expression x){
		_x = x;
		_aop = op;
		_link = y;
	}
}