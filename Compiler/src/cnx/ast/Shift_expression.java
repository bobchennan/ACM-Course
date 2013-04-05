package cnx.ast;

import cnx.symbol.Symbol;

public class Shift_expression {
	public Additive_expression _x = null;
	public Shift_operator _sop = null;
	public Shift_expression _link = null;
	
	public Shift_expression(Additive_expression x){
		_x = x;
	}
	public Shift_expression(Shift_expression y, Shift_operator op, Additive_expression x){
		_x = x;
		_sop = op;
		_link = y;
	}
}