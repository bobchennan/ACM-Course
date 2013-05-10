package cnx.quad;

import cnx.temp.*;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Call extends Quad {
	private Addr x = null;
	private Label p = null;
	private List<Temp> params = null;
	
	public Call(Label p, List<Temp> params){
		this.p = p;
		this.params = params;
	}
	public Call(Addr x, Label p, List<Temp> params){
		this.x = x;
		this.p = p;
		this.params = params;
	}
	public String toString(){
		if(x != null)return x+" = call " + p + ", " + params;
		else return "call " + p + ", " + params;
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if(x != null)
			set.add((Temp)x);
		return set;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.addAll(params);
		return set;
	}
}
