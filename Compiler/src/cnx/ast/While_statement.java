package cnx.ast;

import cnx.symbol.Symbol;

public class While_statement extends Iteration_statement {
	public Expression _exp = null;
	public Statement _st = null;
	public While_statement(Expression x, Statement y){
		_exp = x;
		_st = y;
	}
}