package cnx.ast;

import cnx.symbol.Symbol;
import java.util.*;

public class Structs {
	public List<Type_specifier> _l1;
	public List<Declarators> _l2;
	
	public Structs(Type_specifier x, Declarators y){
		_l1 = new ArrayList<Type_specifier>();
		_l2 = new ArrayList<Declarators>();
		_l1.add(x);
		_l2.add(y);
	}
	public Structs add(Type_specifier y, Declarators z){
		_l1.add(y);
		_l2.add(z);
		return this;
	}
}