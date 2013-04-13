package javac.absyn;

import javac.util.Position;

public class ForStmt extends Stmt {
	
	public Expr init, cond, step;
	public Stmt body;

	public ForStmt(Position pos, Expr init, Expr cond, Expr step, Stmt body) {
		super(pos);
		this.init = init;
		this.cond = cond;
		this.step = step;
		this.body = body;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		init.accept(visitor);
		cond.accept(visitor);
		step.accept(visitor);
		body.accept(visitor);
		visitor.visit(this);
	}
}
