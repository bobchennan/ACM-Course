package cnx.ast;

import cnx.symbol.Symbol;

public class Unary_cast_expression extends Unary_expression {
	public Unary_operator _uop = null;
	public Cast_expression _x = null;
	
	public Unary_cast_expression(Unary_operator op, Cast_expression x){
		_uop = op;
		_x = x;
	}
}