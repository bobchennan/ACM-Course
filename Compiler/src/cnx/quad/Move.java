package cnx.quad;

import cnx.temp.*;
import cnx.assem.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class Move extends Quad {
	private Addr dest, x;
	
	public Move(Addr dest, Addr x){
		this.dest = dest;
		this.x = x;
	}
	public String toString(){
		return dest + " = " + x;
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add((Temp)dest);
		return set;
	}
	
	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if(x instanceof Temp)set.add((Temp)x);
		return set;
	}
	
	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (x.equals(old)) {
			x = t;
		}
	}
	
	@Override
	public AssemList gen() {
		if(x instanceof Temp)return L(new Assem("move @, %", dest, x));
		if(x instanceof Const)return L(new Assem("li @, %", dest, x));
		return L(new Assem("la @, %", dest, x));
	}
}
