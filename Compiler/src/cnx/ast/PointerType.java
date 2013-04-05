package cnx.ast;

import cnx.type.POINTER;
import cnx.env.Env;
import cnx.type.Type;

public class PointerType extends Type_specifier {
	public Type_specifier elementType;

	public PointerType(Type_specifier element) {
		elementType=element;
	}

	@Override
	public Type toType(Env env) {
		return new POINTER(elementType.toType(env));
	}
}
