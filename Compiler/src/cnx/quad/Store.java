package cnx.quad;

import cnx.temp.*;
import cnx.assem.*;
import cnx.env.Constants;

import java.util.LinkedHashSet;
import java.util.Set;

public final class Store extends Quad {

	public Addr x, z;
	public Const y;
	
	/**
	 * x[y] := z
	 * x.y := z
	 */
	public Store(Addr x, Const y, Addr z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return x + "[" + y + "] = " + z;
	}
	
	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add((Temp)x);
		if(z instanceof Temp)set.add((Temp)z);
		return set;
	}
	
	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (x.equals(old)) {
			x = t;
		}
		if (z.equals(old)) {
			z = t;
		}
	}
	
	@Override
	public AssemList gen() {
		return L(new Assem("sw %, %(%)", z, y.value * Constants.pointerSize, x));
	}
}
