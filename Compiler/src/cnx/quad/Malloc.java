package cnx.quad;

import cnx.temp.*;

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
}
