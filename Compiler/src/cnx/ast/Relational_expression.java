package cnx.ast;

import cnx.symbol.Symbol;

public class Relational_expression {
	public Relational_expression _link = null;
	public Relational_operator _rop = null;
	public Shift_expression _x = null;
	
	public Relational_expression(Shift_expression x){
		_x = x;
	}
	public Relational_expression(Relational_expression y, Relational_operator op, Shift_expression x){
		_x = x;
		_rop = op;
		_link = y;
	}
}