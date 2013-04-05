package javac.absyn;

import javac.env.Env;
import javac.type.Type;
import javac.util.Position;

public abstract class TypeSpecifier extends Node {

	protected TypeSpecifier(Position pos) {
		super(pos);
	}

	public abstract Type toType(Env env);

}
