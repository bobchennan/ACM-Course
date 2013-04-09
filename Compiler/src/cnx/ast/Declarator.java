package cnx.ast;

import cnx.symbol.Symbol;

public class Declarator {
	public Plain_declarator _x = null;
	public Parameters _arg = null;
	public Constant_expressions _cexp = null;
	public boolean _isFunc = false;
	
	public Declarator(Plain_declarator x, boolean isFunc){
		_x = x;
		_isFunc = isFunc;
	}
	public Declarator(Plain_declarator x, Parameters y){
		_x = x;
		_arg = y;
		_isFunc = true;
	}
	public Declarator(Plain_declarator x, Constant_expressions y){
		_x = x;
		_cexp = y;
	}
}