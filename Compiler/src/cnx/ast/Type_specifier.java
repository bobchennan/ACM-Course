package cnx.ast;

import cnx.symbol.Symbol;
import cnx.env.Env;
import cnx.type.Type;

public abstract class Type_specifier {
	public abstract Type toType(Env env);
}