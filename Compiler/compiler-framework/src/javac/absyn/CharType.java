package javac.absyn;

import javac.env.Env;
import javac.type.CHAR;
import javac.type.Type;
import javac.util.Position;

public class CharType extends TypeSpecifier {

	public CharType(Position pos) {
		super(pos);
	}
	
	@Override
	public String toString() {
		return "(char)";
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Type toType(Env env) {
		return CHAR.getInstance();
	}
}
