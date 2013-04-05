package cnx.ast;

import cnx.symbol.Symbol;

public class Structs {
	public Type_specifier _ty = null;
	public Declarators _x = null;
	public Structs _link = null;
	
	public Structs(Type_specifier x, Declarators y){
		_ty = x;
		_x = y;
	}
	public Structs(Structs x, Type_specifier y, Declarators z){
		_link = x;
		_ty = y;
		_x = z;
	}
}