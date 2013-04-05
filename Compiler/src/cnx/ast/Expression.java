package cnx.ast;

import cnx.symbol.Symbol;

public class Expression extends Primary_expression {
	public Assignment_expression _x = null;
	public Expression _link = null;
	
	public Expression(Assignment_expression x){
		_x = x;
	}
	public Expression(Expression y, Assignment_expression x){
		_x = x;
		_link = y;
	}
}