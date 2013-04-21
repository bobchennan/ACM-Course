package cnx.quad;

import cnx.temp.*;

public class Param extends Quad {
	private Addr x;
	
	public Param(Addr x){
		this.x = x;
	}
	public String toString(){
		return "param " + x;
	}
}
