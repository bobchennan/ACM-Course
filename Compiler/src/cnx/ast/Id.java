package cnx.ast;

import cnx.symbol.Symbol;

public class Id extends Primary_expression {
	public Symbol _sym;
	
	public Id(Symbol sym){
		_sym = sym;
	}
}