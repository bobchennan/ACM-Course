package javac.absyn;

import javac.symbol.Symbol;
import javac.util.Position;

public class FieldPostfix extends Expr {
	
	public Expr expr;
	public Symbol field;

	public FieldPostfix(Position pos, Expr expr, Symbol field) {
		super(pos);
		this.expr = expr;
		this.field = field;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		expr.accept(visitor);
		visitor.visit(this);
	}
}
