package cnx.ast;

import cnx.symbol.Symbol;

public class IntLiteral extends Constant {
	public int _x = 0;
	
	public IntLiteral(int x){
		_x = x;
	}
}