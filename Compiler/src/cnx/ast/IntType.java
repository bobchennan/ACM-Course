package cnx.ast;

import cnx.env.Env;
import cnx.type.INT;
import cnx.type.Type;

public class IntType extends Type_specifier {
	public IntType() {
	}

	@Override
	public Type toType(Env env) {
		return INT.getInstance();
	}
}
