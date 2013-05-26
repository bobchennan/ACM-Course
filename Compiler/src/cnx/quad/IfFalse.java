package cnx.quad;

import cnx.temp.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cnx.assem.*;

public class IfFalse extends Quad {
	private Addr x;
	private Label l;
	
	public IfFalse(Addr x, Label l){
		this.x = x.clone();
		this.l = l;
	}
	public String toString(){
		return "ifFalse " + x + " goto " + l;
	}
	
	@Override
	public boolean isJump() {
		return true;
	}

	@Override
	public Label jumpLabel() {
		return l;
	}

	@Override
	public void replaceLabelOf(Label old, Label l) {
		if (this.l.equals(old)) {
			this.l = l;
		}
	}

	@Override
	public Quad jumpTargetIn(List<Quad> quads) {
		return findTargetIn(quads, l);
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if(x instanceof Temp)set.add((Temp)x);
		return set;
	}
	
	@Override
	public AssemList gen() {
		if(x instanceof Temp)return L(new Assem("beqz %, %", x, l));
		else if(((Const)x).value == 0)return L(new Assem("j %", l));
		else return null;
	}
}
