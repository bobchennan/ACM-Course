package cnx.ast;

import cnx.symbol.Symbol;

public class StringLiteral extends Primary_expression {
	public String _s = "";
	
	public StringLiteral(String s){
		_s = s;
	}
}