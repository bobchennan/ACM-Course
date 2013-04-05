package javac.absyn;

import javac.env.Env;
import javac.env.TypeEntry;
import javac.symbol.Symbol;
import javac.type.Type;
import javac.util.Position;

public class IdType extends TypeSpecifier {
	
	public Symbol sym;
	
	public IdType(Position pos, Symbol sym) {
		super(pos);
		this.sym = sym;
	}
	
	@Override
	public String toString() {
		return String.format("IdType(%s)", sym.toString());
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Type toType(Env env) {
		return TypeEntry.transferTypeEntry(env.getEntry(sym)).type;
	}
}
