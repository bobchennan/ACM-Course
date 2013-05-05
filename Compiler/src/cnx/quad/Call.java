package cnx.quad;

import cnx.temp.*;

public class Call extends Quad {
	private Addr x = null;
	private Label p = null;
	private int n;
	
	public Call(Label p, int n){
		this.p = p;
		this.n = n;
	}
	public Call(Addr x, Label p, int n){
		this.x = x;
		this.p = p;
		this.n = n;
	}
	public String toString(){
		if(x != null)return x+" = call " + p + ", " + n;
		else return "call " + p + ", " + n;
	}
}
