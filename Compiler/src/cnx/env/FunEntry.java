package cnx.env;

import cnx.type.*;

public class FunEntry implements Entry {
	public RECORD formals;
	public Type result;
	public boolean nc;

	public FunEntry(RECORD f, Type r, boolean co) {
		formals = f;
		result = r;
		nc = co;
	}
}
