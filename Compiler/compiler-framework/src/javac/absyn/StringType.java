package javac.absyn;

import javac.env.Env;
import javac.type.STRING;
import javac.type.Type;
import javac.util.Position;

public class StringType extends TypeSpecifier {

	public StringType(Position pos) {
		super(pos);
	}
	
	@Override
	public String toString() {
		return "(string)";
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Type toType(Env env) {
		return STRING.getInstance();
	}
}
