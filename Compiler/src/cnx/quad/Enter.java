package cnx.quad;

import cnx.temp.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class Enter extends Quad{

	Label label = null;
	LinkedList<Temp> params = null;
	
	public Enter(Label l, LinkedList<Temp> p) {
		label = l;
		params = p;
	}
	
	public String toString() {
		return "enter " + label;
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.addAll(params);
		return set;
	}
}
