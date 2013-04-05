package javac.absyn;

import javac.symbol.Symbol;
import javac.util.Position;

public class Id extends Expr {
	
	public Symbol sym;

	public Id(Position pos, Symbol sym) {
		super(pos);
		this.sym = sym;
	}
	
	@Override
	public String toString() {
		return sym.toString();
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
