package cnx.ast;

import cnx.symbol.Symbol;

public class Dot_expression extends Postfix_expression {
	public Postfix_expression _x = null;
	public Symbol _sym = null;
	public Dot_expression(Postfix_expression x, Symbol sym){
		_x = x;
		_sym = sym;
	}
}
