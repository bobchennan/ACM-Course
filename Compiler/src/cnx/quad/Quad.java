package cnx.quad;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cnx.temp.*;

public abstract class Quad {
private boolean leader = false;
	
	public void setLeader() {
		leader = true;
	}
	
	public boolean isLeader() {
		return leader;
	}

	public boolean isJump() {
		return false;
	}
	
	public Label jumpLabel() {
		return null;
	}
	
	public void replaceLabelOf(Label old, Label l) {
	}

	public Quad jumpTargetIn(List<Quad> quads) {
		return null;
	}
	
	protected Quad findTargetIn(List<Quad> quads, Label target) {
		for (Quad q : quads) {
			if (q instanceof LABEL && ((LABEL) q).label.equals(target)) {
				return q;
			}
		}
		return null;
	}
	
	public Set<Temp> def() {
		return new LinkedHashSet<Temp>();
	}
	
	public Set<Temp> use() {
		return new LinkedHashSet<Temp>();
	}

	public void replaceUseOf(Temp old, Temp t) {
	}
	
	private List<Quad> successors = new LinkedList<Quad>();

	public void addSuccessor(Quad q) {
		successors.add(q);
	}
	
	public List<Quad> getSuccessors() {
		return successors;
	}
}
