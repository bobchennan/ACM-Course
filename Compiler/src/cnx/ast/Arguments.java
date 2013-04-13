package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Arguments {
	public List<Assignment_expression> _l;
	
	public Arguments(Assignment_expression x){
		_l = new ArrayList<Assignment_expression>();
		_l.add(x);
	}
	public Arguments add(Assignment_expression x){
		_l.add(x);
		return this;
	}
}