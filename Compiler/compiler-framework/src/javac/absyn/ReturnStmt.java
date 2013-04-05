package javac.absyn;

import javac.util.Position;

public class ReturnStmt extends Stmt {
	
	public Expr expr;

	public ReturnStmt(Position pos, Expr expr) {
		super(pos);
		this.expr = expr;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		expr.accept(visitor);
		visitor.visit(this);
	}
}
