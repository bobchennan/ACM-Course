package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Declarators {
	public List<Declarator> _l;
	
	public Declarators(Declarator x){
		_l = new ArrayList<Declarator>();
		_l.add(x);
	}
	public Declarators add(Declarator x){
		_l.add(x);
		return this;
	}
}