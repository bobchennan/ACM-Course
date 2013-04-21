package cnx.quad;

import cnx.temp.*;

public class Return extends Quad {

	private Addr addr = null;
	
	public Return(Addr a) {
		addr = a;
	}

	public String toString() {
		return "return " + addr;
	}
}
