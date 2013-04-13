package cnx.env;

import cnx.type.*;

public class VarEntry implements Entry {
	public Type ty;

	public VarEntry(Type t) {
		ty = t;
	}
}
