package cnx.env;

import cnx.type.*;

public class VarEntry implements Entry {
	public Type ty;
	public Object p = null;

	public VarEntry(Type t) {
		ty = t;
	}
	public VarEntry(Type t, Object tt){
		ty = t;
		p = tt;
	}
}
