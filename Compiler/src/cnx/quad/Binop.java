package cnx.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import cnx.temp.*;
import cnx.env.Constants;

public class Binop extends Quad {
	private Addr dest,x,y;
	private int opStr;
	
	public Binop(Addr dest, Addr x, int opStr, Addr y){
		this.dest = dest;
		this.x = x;
		this.opStr = opStr;
		this.y = y;
	}
	public String toString(){
		return dest.toString() + " = " + x.toString() + " " + Constants.bopStr[opStr] + " " + y.toString();
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
		if(y instanceof Temp)set.add((Temp)y);
		return set;
	}
	
	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (x.equals(old)) {
			x = t;
		}
		if (y.equals(old)) {
			y = t;
		}
	}
}
