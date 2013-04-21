package cnx.quad;

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
}
