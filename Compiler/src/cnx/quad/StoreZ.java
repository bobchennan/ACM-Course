package cnx.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import cnx.assem.*;
import cnx.env.Constants;
import cnx.temp.*;

public class StoreZ extends Quad{

	public Temp x;
	public Const y;

	/**
	 * x[y] := 0
	 * x.y := 0
	 */
	public StoreZ(Temp x, Const y) {
		this.x = x;
		this.y = y;
	}

	public StoreZ(Temp x, Const y, String c) {
		this(x, y);
	}
	
	public String toString() {
		return x + "[" + y + "] = 0";
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add(x);
		return set;
	}

	@Override
	public AssemList gen() {
		return L(new Assem("sw $zero, %(%) # StoreZ", y.value * Constants.pointerSize, x));
	}

	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (x.equals(old)) {
			x = t;
		}
	}
}
