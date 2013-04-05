package cnx.ast;

import java.util.LinkedList;
import java.util.List;

import cnx.symbol.Symbol;

public class Id_list {
	
	public List<Symbol> ids = new LinkedList<Symbol>();

	public Id_list() {
	}
	
	public void add(Symbol id) {
		ids.add(id);
	}
}
