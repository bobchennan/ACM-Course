package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Plain_declarations {
	public List<Plain_declaration> _l;
	
	public Plain_declarations(Plain_declaration x){
		_l = new ArrayList<Plain_declaration>();
		_l.add(x);
	}
	public Plain_declarations add(Plain_declaration x){
		_l.add(x);
		return this;
	}
}