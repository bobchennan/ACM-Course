package cnx.ast;

import cnx.symbol.Symbol;

public class Declarations {
	public Declaration _x = null;
	public Declarations _link= null;
	
	public Declarations(){
	}
	public Declarations(Declaration x){
		_x = x;
	}
	public Declarations(Declarations y, Declaration x){
		_x = x;
		_link = y;
	}
}