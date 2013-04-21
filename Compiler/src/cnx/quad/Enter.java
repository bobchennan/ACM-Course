package cnx.quad;

import cnx.temp.*;

public class Enter extends Quad{

	Label label = null;
	
	public Enter(Label l) {
		label = l;
	}
	
	public String toString() {
		return "enter " + label;
	}
}
