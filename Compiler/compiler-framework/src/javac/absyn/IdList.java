package javac.absyn;

import java.util.LinkedList;
import java.util.List;

import javac.symbol.Symbol;
import javac.util.Position;

public class IdList extends Node {
	
	public List<Symbol> ids = new LinkedList<Symbol>();

	public IdList(Position pos) {
		super(pos);
	}
	
	public void add(Symbol id) {
		ids.add(id);
	}
	
	@Override
	public String toString() {
		final StringBuffer s = new StringBuffer();
		for (Symbol id : ids) {
			s.append(id);
			s.append(';');
		}
		return s.toString();
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
