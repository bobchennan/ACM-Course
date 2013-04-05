package cnx.ast;

import cnx.symbol.Symbol;

public class Init_declarator {
	public Declarator _x = null;
	public Initializer _y = null;
	
	public Init_declarator(Declarator x){
		_x = x;
	}
	
	public Init_declarator(Declarator x, Initializer y){
		_x = x;
		_y = y;
	}
}