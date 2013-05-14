package cnx.quad;

import cnx.assem.*;
import cnx.temp.*;
import cnx.env.Constants;
import cnx.translate.Level;

public class Leave extends Quad {

	private Label label = null;
	private Level level = null;
	
	public Leave(Label l) {
		label = l;
		level = Constants.now;
	}

	public String toString() {
		return "leave " + label;
	}
	
	@Override
	public AssemList gen() {
		int L = level.frameSize();
		return L(level.loadRegisters(),
				L(new Assem("lw $ra, %($sp)", L - Constants.pointerSize),            // restore $ra
				L(new Assem("addiu $sp, $sp, %", L),                    // restore $sp
				L(new Assem("jr $ra")))));                            // jump back
	}
	
}
