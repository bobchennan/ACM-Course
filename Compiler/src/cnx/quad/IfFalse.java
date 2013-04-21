package cnx.quad;

import cnx.temp.*;

public class IfFalse extends Quad {
	private Addr x;
	private Label l;
	
	public IfFalse(Addr x, Label l){
		this.x = x;
		this.l = l;
	}
	public String toString(){
		return "ifFalse " + x + " goto " + l;
	}
}
