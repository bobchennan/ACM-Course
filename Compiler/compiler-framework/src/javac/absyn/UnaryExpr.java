package javac.absyn;

import javac.util.Position;

public class UnaryExpr extends Expr {
	
	public UnaryOp op;
	public Expr expr;

	public UnaryExpr(Position pos, UnaryOp op, Expr expr) {
		super(pos);
		this.op = op;
		this.expr = expr;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		expr.accept(visitor);
		visitor.visit(this);
	}
}
