package cnx.ast;

import cnx.symbol.Symbol;

public class Initializer {
	public Assignment_expression _x = null;
	public Initializers _y = null;
	
	public Initializer(Assignment_expression x){
		_x = x;
	}
	public Initializer(Initializers y){
		_y = y;
	}
}