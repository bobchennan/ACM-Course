package cnx.quad;

import cnx.temp.*;

public class Goto extends Quad {
	private Label l = null;
	
	public Goto(Label l){
		this.l = l;
	}
	public String toString(){
		return "goto " + l;
	}
}
