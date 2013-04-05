package cnx.ast;

import cnx.type.STRING;
import cnx.env.Env;
import cnx.type.Type;

public class StringType extends Type_specifier {
	public StringType() {
	}

	@Override
	public Type toType(Env env) {
		return STRING.getInstance();
	}
}
