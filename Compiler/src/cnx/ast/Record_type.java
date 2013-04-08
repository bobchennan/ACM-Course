package cnx.ast;

import cnx.env.Env;
import cnx.type.Type;

public class Record_type extends Type_specifier {
	public boolean _isSt = false;
	public Id _sym = null;
	public Structs _x = null;
	
	public Record_type(boolean isSt, Id sym){
		_isSt = isSt;
		_sym = sym;
	}
	public Record_type(boolean isSt, Structs x){
		_isSt = isSt;
		_x = x;
	}
	public Record_type(boolean isSt, Id sym, Structs x){
		_isSt = isSt;
		_sym = sym;
		_x = x;
	}

	@Override
	public Type toType(Env env) {
		return null;
	}

}
