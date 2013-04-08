package cnx.ast;

import cnx.symbol.Symbol;

public class Pointer_expression extends Postfix_expression {
	public Postfix_expression _x = null;
	public Id _y = null;
	
	public Pointer_expression(Postfix_expression x, Id y){
		_x = x;
		_y = y;
	}
}
