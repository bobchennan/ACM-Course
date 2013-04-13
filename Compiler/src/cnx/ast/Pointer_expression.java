package cnx.ast;

import cnx.symbol.Symbol;

public class Pointer_expression extends Postfix_expression {
	public Postfix_expression _x = null;
	public Symbol _y = null;
	
	public Pointer_expression(Postfix_expression x, Symbol y){
		_x = x;
		_y = y;
	}
}
