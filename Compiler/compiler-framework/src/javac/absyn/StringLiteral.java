package javac.absyn;

import javac.util.Position;

public class StringLiteral extends Expr {
	
	public String s;

	public StringLiteral(Position pos, String s) {
		super(pos);
		this.s = s;
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\"", s);
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
