package cnx.quad;

import cnx.temp.*;

public class Leave extends Quad {

	private Label label = null;
	
	public Leave(Label l) {
		label = l;
	}

	public String toString() {
		return "leave " + label;
	}
}
