package cnx.ast;

import cnx.symbol.Symbol;

public class Cast_expression {
	public Unary_expression _x = null;
	public Type_name _ty = null;
	public Cast_expression _link = null;
	
	public Cast_expression(Unary_expression x){
		_x = x;
	}
	public Cast_expression(Type_name ty, Cast_expression y){
		_link = y;
		_ty = ty;
	}
}