package cnx.ast;

import cnx.symbol.Symbol;

public class Function_definition extends Prog {
	public Type_specifier _ty = null;
	public Plain_declarator _x = null;
	public Parameters _ar = null;
	public Compound_statement _st = null;
	
	public Function_definition(Type_specifier x, Plain_declarator y, Parameters z, Compound_statement s){
		_ty = x;
		_x = y;
		_ar = z;
		_st = s;
	}
	
	public Function_definition(Type_specifier x, Plain_declarator y, Compound_statement s){
		_ty = x;
		_x = y;
		_st = s;
	}
}