package cnx.ast;

import cnx.symbol.Symbol;

public class Return_statement extends Jump_statement {
	public Expression _exp = null;
	public Return_statement(Expression exp){
		_exp = exp;
	}
}