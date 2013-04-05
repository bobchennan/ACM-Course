package cnx.ast;

import cnx.symbol.Symbol;

public class Assignment_expression {
	public Logical_or_expression _lexp = null;
	public Unary_expression _uexp = null;
	public Assignment_operator _aop = null;
	public Assignment_expression _link = null;
	
	public Assignment_expression(Logical_or_expression lexp){
		_lexp = lexp;
	}
	public Assignment_expression(Unary_expression uexp, Assignment_operator aop, Assignment_expression x){
		_uexp = uexp;
		_aop = aop;
		_link = x;
	}
}