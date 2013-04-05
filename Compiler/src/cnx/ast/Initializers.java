package cnx.ast;

import cnx.symbol.Symbol;

public class Initializers {
	public Initializer _x = null;
	public Initializers _link = null;
	
	public Initializers(Initializer x){
		_x = x;
	}
	
	public Initializers(Initializers y, Initializer x){
		_x = x;
		_link = y;
	}
}