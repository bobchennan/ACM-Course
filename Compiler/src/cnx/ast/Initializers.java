package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Initializers {
	public List<Initializer> _l;
	
	public Initializers(Initializer x){
		_l = new ArrayList<Initializer>();
		_l.add(x);
	}
	
	public Initializers add(Initializer x){
		_l.add(x);
		return this;
	}
}