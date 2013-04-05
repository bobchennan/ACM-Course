package javac.absyn;

import javac.util.Position;

public abstract class Stmt extends Node {

	protected Stmt(Position pos) {
		super(pos);
	}
}
