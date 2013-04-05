package javac.absyn;

import javac.util.Position;

public class SubscriptPostfix extends Expr {
	
	public Expr expr, subscript;

	public SubscriptPostfix(Position pos, Expr expr, Expr subscript) {
		super(pos);
		this.expr = expr;
		this.subscript = subscript;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		expr.accept(visitor);
		subscript.accept(visitor);
		visitor.visit(this);
	}
}
