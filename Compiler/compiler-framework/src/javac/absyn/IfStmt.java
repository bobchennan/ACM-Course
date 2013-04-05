package javac.absyn;

import javac.util.Position;

public class IfStmt extends Stmt {
	
	public Expr cond;
	public Stmt thenPart, elsePart;

	public IfStmt(Position pos, Expr cond, Stmt thenPart, Stmt elsePart) {
		super(pos);
		this.cond = cond;
		this.thenPart = thenPart;
		this.elsePart = elsePart;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		cond.accept(visitor);
		thenPart.accept(visitor);
		if (elsePart != null) {
			elsePart.accept(visitor);
		}
		visitor.visit(this);
	}
}
