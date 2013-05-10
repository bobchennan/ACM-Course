package cnx.quad;

import cnx.temp.*;
import java.util.List;
import java.util.Set;

public class Malloc extends Quad {
	Addr ret = null;
	Addr size = null;
	
	public Malloc(Addr ret, Addr size){
		this.ret = ret;
		this.size = size;
	}
	
	public String toString(){
		return ret + " = malloc " + size;
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = super.def();
		set.add((Temp)ret);
		return set;
	}
	
	@Override
	public Set<Temp> use() {
		Set<Temp> set = super.def();
		set.add((Temp)size);
		return set;
	}
}
