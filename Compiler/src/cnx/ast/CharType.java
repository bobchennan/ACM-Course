package cnx.ast;

import cnx.env.Env;
import cnx.type.CHAR;
import cnx.type.Type;

public class CharType extends Type_specifier {
	public CharType() {
	}

	@Override
	public Type toType(Env env) {
		return CHAR.getInstance();
	}
}
