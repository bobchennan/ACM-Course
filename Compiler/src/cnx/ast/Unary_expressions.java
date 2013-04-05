package cnx.ast;

import cnx.symbol.Symbol;

public class Unary_expressions extends Unary_expression {
	public Unary_expressions_operator _uop = null;
	public Unary_expression _link = null;
	
	public Unary_expressions(Unary_expressions_operator op, Unary_expression y){
		_uop = op;
		_link = y;
	}
}