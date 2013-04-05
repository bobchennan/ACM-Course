package cnx.ast;

import cnx.symbol.Symbol;

public class Type_name {
	public Type_specifier _ty = null;
	public int cnt = 0;
	
	public Type_name(Type_specifier ty){
		_ty = ty;
	}
	public Type_name(Type_name x){
		_ty = x._ty;
		cnt=x.cnt+1;
	}
}