package cnx.ast;

import cnx.env.Env;
import cnx.type.Type;
import cnx.type.VOID;

public class VoidType extends Type_specifier {
	public VoidType(){
	}
	
	@Override
	public Type toType(Env env) {
		return VOID.getInstance();
	}

}
