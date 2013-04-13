package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Declarations {
	public List<Declaration> _l;
	
	public Declarations(){
		_l = new ArrayList<Declaration>();
	}
	public Declarations add(Declaration y){
		_l.add(y);
		return this;
	}
}