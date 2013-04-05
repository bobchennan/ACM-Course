package cnx.ast;

import cnx.symbol.Symbol;

public class Arguments {
	public Assignment_expression _x = null;
	public Arguments _link = null;
	
	public Arguments(Assignment_expression x){
		_x = x;
	}
	public Arguments(Arguments y, Assignment_expression x){
		_x = x;
		_link = y;
	}
}