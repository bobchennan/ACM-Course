package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Init_declarators {
	public List<Init_declarator> _l;
	
	public Init_declarators(Init_declarator x){
		_l = new ArrayList<Init_declarator>();
		_l.add(x);
	}
	public Init_declarators add(Init_declarator x){
		_l.add(x);
		return this;
	}
}