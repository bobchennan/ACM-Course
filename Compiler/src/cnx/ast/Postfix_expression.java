package cnx.ast;

import cnx.symbol.Symbol;

public class Postfix_expression extends Unary_expression {
	public Postfix_expression _link = null;
	public Postfix _y = null;
	public Primary_expression _x = null;
	
	public Postfix_expression(Primary_expression x){
		_x = x;
	}
	public Postfix_expression(Postfix_expression x, Postfix y){
		_link = x;
		_y = y;
	}
}