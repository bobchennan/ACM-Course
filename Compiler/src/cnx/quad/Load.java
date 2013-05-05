package cnx.quad;

import cnx.temp.*;

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
}
