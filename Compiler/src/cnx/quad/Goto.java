package cnx.quad;

import cnx.temp.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Goto extends Quad {
	private Label l = null;
	
	public Goto(Label l){
		this.l = l;
	}
	public String toString(){
		return "goto " + l;
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
}
