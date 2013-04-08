package cnx.ast;

import cnx.symbol.Symbol;

public class Plain_declaration {
	public Type_specifier _ty = null;
	public Declarator _x = null;
	
	public Plain_declaration(Type_specifier ty, Declarator x){
		_ty = ty;
		_x = x;
	}
}