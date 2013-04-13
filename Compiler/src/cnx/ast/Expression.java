package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Expression extends Primary_expression {
	public List<Assignment_expression> _l;
	
	public Expression(Assignment_expression x){
		_l = new ArrayList<Assignment_expression>();
                _l.add(x);
	}
	public Expression add(Assignment_expression x){
		_l.add(x);
		return this;
	}
}