package cnx.ast;

import cnx.symbol.Symbol;
import cnx.env.Env;
import cnx.type.Type;

public class Id_type extends Type_specifier {
	
	public Symbol _sym;
	
	public Id_type(Symbol sym) {
		_sym = sym;
	}
	
	@Override
	public Type toType(Env env) {
		return (Type)(env.tEnv.get(_sym));
	}
}
