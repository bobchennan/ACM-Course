package cnx.ast;

import cnx.symbol.Symbol;

public class Declaration extends Prog {
	public Type_specifier _ty = null;
	public Init_declarators _init = null;
	public Declarators _v = null;
	
	public Declaration(Type_specifier x){
		_ty = x;
	}
	public Declaration(Type_specifier x, Init_declarators y){
		_ty = x;
		_init = y;
	}
	public Declaration(Type_specifier x, Declarators y){
		_ty = x;
		_v = y;
	}
}