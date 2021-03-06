package cnx.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import cnx.assem.*;

import cnx.temp.*;

public class LABEL extends Quad {

	public Label label = null;
	
	public LABEL(Label l) {
		label = l;
	}

	public String toString() {
		return label + ":";
	}
	
	@Override
	public AssemList gen() {
		return L(new Assem("!%:", label));
	}
}
