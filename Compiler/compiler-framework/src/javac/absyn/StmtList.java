package javac.absyn;

import java.util.LinkedList;
import java.util.List;

import javac.util.Position;

public class StmtList extends Node {
	
	public List<Stmt> statements = new LinkedList<Stmt>();

	public StmtList(Position pos) {
		super(pos);
	}
	
	public void add(Stmt stmt) {
		statements.add(stmt);
	}

	@Override
	public void accept(NodeVisitor visitor) {
		for (Stmt stmt : statements) {
			stmt.accept(visitor);
		}
		visitor.visit(this);
	}
}
