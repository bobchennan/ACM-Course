package cnx.ast;

import cnx.symbol.Symbol;

public class Init_declarators {
	public Init_declarator _x = null;
	public Init_declarators _link = null;
	
	public Init_declarators(Init_declarator x){
		_x = x;
	}
	public Init_declarators(Init_declarators y, Init_declarator x){
		_x = x;
		_link = y;
	}
}