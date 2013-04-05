package javac.absyn;

import javac.util.Position;

public class ContinueStmt extends Stmt {

	public ContinueStmt(Position pos) {
		super(pos);
	}
	
	@Override
	public String toString() {
		return "(continue)";
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
