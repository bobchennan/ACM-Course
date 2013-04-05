package cnx.ast;

import cnx.symbol.Symbol;

public class Declarators {
	public Declarators _link = null;
	public Declarator _x = null;
	
	public Declarators(Declarator x){
		_x = x;
	}
	public Declarators(Declarators y, Declarator x){
		_x = x;
		_link = y;
	}
}