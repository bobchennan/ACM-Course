package javac.absyn;

import javac.util.Position;

public class CharLiteral extends Expr {
	
	public char c;

	public CharLiteral(Position pos, char c) {
		super(pos);
		this.c = c;
	}
	
	@Override
	public String toString() {
		return String.format("'%c'", c);
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
