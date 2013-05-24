package cnx.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import cnx.temp.*;
import cnx.env.Constants;
import cnx.assem.*;

public class Address extends Quad {
	Temp dest;
	int offset;
	boolean isAllArea;
	
	public  Address(Temp dest, int offset, boolean isAllArea){
		this.dest = dest;
		this.offset = offset;
		this.isAllArea = isAllArea;
	}
	public String toString(){
		if(isAllArea)return dest.toString() + " = $gp + " + offset;
		else return dest + "= %sp + " +offset;
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
		return set;
	}

	@Override
	public AssemList gen(){
		if(isAllArea)return L(new Assem("addu @, $gp, %", dest, offset));
		else return L(new Assem("addu @, $sp, %", dest, offset));
	}
}
