package cnx.env;

import cnx.type.*;

public class FunEntry implements Entry {
	public RECORD formals;
	public Type result;

	public FunEntry(RECORD f, Type r) {
		formals = f;
		result = r;
	}
}
