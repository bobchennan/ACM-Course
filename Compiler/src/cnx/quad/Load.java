package cnx.quad;

import cnx.temp.*;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Load extends Quad {

	public Addr x, y;
	public Const z;
	
	/**
	 * x := y[z]
	 * x = y.z
	 */
	public Load(Addr x, Addr y, Const z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return x + " = " + y + "[" + z + "]";
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add((Temp)x);
		return set;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add((Temp)y);
		return set;
	}

	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (y.equals(old)) {
			y = t;
		}
	}
}
