package cnx.ast;

import cnx.symbol.Symbol;

public class Type_size extends Unary_expression {
	public Type_name _ty = null;
	
	public Type_size(Type_name ty){
		_ty = ty;
	}
} 