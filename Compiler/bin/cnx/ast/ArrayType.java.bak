package cnx.ast;

import cnx.type.ARRAY;
import cnx.env.Env;
import cnx.type.Type;

public class ArrayType extends PointerType {
	public int capacity;

	public ArrayType(Type_specifier elementType, int cap) {
		super(elementType);
		capacity = cap;
	}

	@Override
	public Type toType(Env env) {
		return new ARRAY(elementType.toType(env),capacity);
	}
}
