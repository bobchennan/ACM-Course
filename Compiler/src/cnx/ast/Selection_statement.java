package cnx.ast;

import cnx.symbol.Symbol;

public class Selection_statement extends Statement {
	public Expression _exp = null;
	public Statement _st1 = null;
	public Statement _st2 = null;
	
	public Selection_statement(Expression exp, Statement st1){
		_exp = exp;
		_st1 = st1;
	}
	public Selection_statement(Expression exp, Statement st1, Statement st2){
		_exp = exp;
		_st1 = st1;
		_st2 = st2;
	}
}