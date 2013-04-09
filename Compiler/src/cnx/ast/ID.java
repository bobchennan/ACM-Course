package cnx.ast;

import cnx.symbol.Symbol;

public class ID extends Primary_expression {
	public Symbol _name;
	
	public ID(Symbol name){
		_name = name;
	}
}
