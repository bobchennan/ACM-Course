package javac.absyn;

import javac.util.Position;

public class BreakStmt extends Stmt {

	public BreakStmt(Position pos) {
		super(pos);
	}
	
	@Override
	public String toString() {
		return "(break)";
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
