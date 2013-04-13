package cnx.ast;

import cnx.symbol.Symbol;

public class For_statement extends Iteration_statement {
	public Expression _exp1 = null;
	public Expression _exp2 = null;
	public Expression _exp3 = null;
	public Statement _st = null;
	
	public For_statement(Expression exp1, Expression exp2, Expression exp3, Statement x){
		_exp1 = exp1;
		_exp2 = exp2;
		_exp3 = exp3;
		_st = x;
	}
}