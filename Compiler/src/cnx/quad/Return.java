package cnx.quad;

import cnx.temp.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class Return extends Quad {

	private Addr addr = null;
	
	public Return(Addr a) {
		addr = a;
	}

	public String toString() {
		return "return " + addr;
	}
	
	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add((Temp)addr);
		return set;
	}
	
	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (addr.equals(old)) {
			addr = t;
		}
	}
}
