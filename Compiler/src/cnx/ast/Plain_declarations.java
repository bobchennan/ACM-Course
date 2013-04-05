package cnx.ast;

import cnx.symbol.Symbol;

public class Plain_declarations {
	public Plain_declaration _x = null;
	public Plain_declarations _link = null;
	
	public Plain_declarations(Plain_declaration x){
		_x = x;
	}
	public Plain_declarations(Plain_declarations y, Plain_declaration x){
		_link = y;
		_x = x;
	}
}