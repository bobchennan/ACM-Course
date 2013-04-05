package javac.absyn;

import javac.env.Env;
import javac.type.ARRAY;
import javac.type.Type;
import javac.util.Position;

public class ArrayType extends TypeSpecifier {
	
	public TypeSpecifier elemType;

	public ArrayType(Position pos, TypeSpecifier elementType) {
		super(pos);
		elemType = elementType;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		elemType.accept(visitor);
		visitor.visit(this);
	}

	@Override
	public Type toType(Env env) {
		return new ARRAY(elemType.toType(env));
	}
}
