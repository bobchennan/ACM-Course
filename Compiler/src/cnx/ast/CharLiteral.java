package cnx.ast;

import cnx.symbol.Symbol;

public class CharLiteral extends Constant {
	public char _x;
	
	public CharLiteral(char x){
		_x = x;
	}
}