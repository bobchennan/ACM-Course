package cnx.ast;

import cnx.symbol.Symbol;

public class Parameters {
	public Plain_declaration _x = null;
	public Plain_declarations _xs = null;
	public Boolean _ellipsis = false;
	
	public Parameters(Plain_declaration x, Plain_declarations xs, Boolean elli){
		_x = x;
		_xs = xs;
		_ellipsis = elli;
	}
	
	public Parameters(Plain_declaration x, Boolean elli){
		_x = x;
		_ellipsis = elli;
	}
}