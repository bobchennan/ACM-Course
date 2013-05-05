package cnx.quad;

import cnx.temp.*;

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
}
