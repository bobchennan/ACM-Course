package javac.absyn;

import javac.util.Position;

public class ExprStmt extends Stmt {

	public Expr expr;
	
	public ExprStmt(Position pos, Expr expr) {
		super(pos);
		this.expr = expr;
	}
	
	public Expr getExpr() {
		return expr;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		expr.accept(visitor);
		visitor.visit(this);
	}
}
