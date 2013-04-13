package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Constant_expressions {
	public List<Constant_expression> _l;
	
	public Constant_expressions(Constant_expression x){
		_l = new ArrayList<Constant_expression>();
		_l.add(x);
	}
	public Constant_expressions add(Constant_expression x){
		_l.add(x);
		return this;
	}
}