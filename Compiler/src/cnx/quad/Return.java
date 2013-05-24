package cnx.quad;

import cnx.temp.*;
import cnx.assem.*;
import cnx.env.Constants;
import cnx.translate.Level;

import java.util.LinkedHashSet;
import java.util.Set;


public class Return extends Quad {

	private Addr addr = null;
	private Level level = null;
	private Label exit = null;
	
	public Return(Addr a) {
		addr = a.clone();
		level = Constants.now;
		exit = Constants.exit;
	}

	public String toString() {
		return "return " + addr;
	}
	
	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if(addr instanceof Temp)set.add((Temp)addr);
		return set;
	}
	
	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (addr.equals(old)) {
			addr = t;
		}
	}
	
	@Override
	public AssemList gen() {
		if(addr instanceof Temp)return L(new Assem("move $v0, %", addr),L(new Assem("j %", exit)));
		else return L(new Assem("li $v0, %", addr), L(new Assem("j %", exit)));
	}
}
