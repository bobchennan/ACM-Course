package javac.absyn;

import javac.util.Position;

public class BinaryExpr extends Expr {
	
	public Expr l, r;
	public BinaryOp op;

	public BinaryExpr(Position pos, Expr lhs, BinaryOp op, Expr rhs) {
		super(pos);
		l = lhs;
		r = rhs;
		this.op = op;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		l.accept(visitor);
		r.accept(visitor);
		visitor.visit(this);
	}
}
