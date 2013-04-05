package cnx.ast;

import cnx.symbol.Symbol;

public class Expression_statement extends Statement {
	public Expression _x = null;
	
	public Expression_statement(Expression x){
		_x = x;
	}
}