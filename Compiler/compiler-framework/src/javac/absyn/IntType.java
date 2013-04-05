package javac.absyn;

import javac.env.Env;
import javac.type.INT;
import javac.type.Type;
import javac.util.Position;

public class IntType extends TypeSpecifier {

	public IntType(Position pos) {
		super(pos);
	}
	
	@Override
	public String toString() {
		return "(int)";
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Type toType(Env env) {
		return INT.getInstance();
	}
}
