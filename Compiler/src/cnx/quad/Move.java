package cnx.quad;

import cnx.temp.*;

public class Move extends Quad {
	private Addr dest, x;
	
	public Move(Addr dest, Addr x){
		this.dest = dest;
		this.x = x;
	}
	public String toString(){
		return dest + " = " + x;
	}
}
