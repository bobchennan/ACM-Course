package javac.absyn;

import javac.util.Position;

public class CompoundStmt extends Stmt {
	
	public StmtList stmts;

	public CompoundStmt(Position pos, StmtList stmts) {
		super(pos);
		this.stmts = stmts;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		stmts.accept(visitor);
		visitor.visit(this);
	}
}
