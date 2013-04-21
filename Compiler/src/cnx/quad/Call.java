package cnx.quad;

import cnx.temp.*;

public class Call extends Quad {
	private Label p = null;
	private int n;
	
	public Call(Label p, int n){
		this.p = p;
		this.n = n;
	}
	public String toString(){
		return "call " + p + ", " + n;
	}
}
