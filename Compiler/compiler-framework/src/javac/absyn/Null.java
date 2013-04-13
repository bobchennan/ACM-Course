package javac.absyn;

import javac.util.Position;

public class Null extends Expr {

	public Null(Position pos) {
		super(pos);
	}
	
	@Override
	public String toString() {
		return "(null)";
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
