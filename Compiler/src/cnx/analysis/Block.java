package cnx.analysis;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import cnx.temp.*;
import cnx.quad.*;

public class Block {
	public static int count = 0;
	private int number = 0;
	
	public Block() {
		number = ++count;
	}
	
	private LinkedList<Quad> quads = new LinkedList<Quad>();

	public void addQuad(Quad q) {
		quads.add(q);
	}

	public LinkedList<Quad> getQuads() {
		return quads;
	}

	public Quad getLastQuad() {
		return quads.descendingIterator().next();
	}

	public Object getFirstQuad() {
		return quads.get(0);
	}
	
	private LinkedList<Block> successors = new LinkedList<Block>();

	public void addSuccessor(Block succ) {
		successors.add(succ);
	}
	
	public LinkedList<Block> getSuccessors() {
		return successors;
	}
	
	private LinkedList<Block> predecessors = new LinkedList<Block>();

	public void addPredecessor(Block pred) {
		predecessors.add(pred);
	}
	
	public LinkedList<Block> getPredecessors() {
		return predecessors;
	}
	
	public String toString() {
		String ret=new String("B"+number+"\n");
		for(Quad i:quads){
			ret+=i;
			ret+="\n";
		}
		return ret;
	}
	
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		Iterator<Quad> iter = quads.descendingIterator();
		while (iter.hasNext()) {
			Quad q = iter.next();
			set.addAll(q.def());
			set.removeAll(q.use());
		}
		return set;
	}
	
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		Iterator<Quad> iter = quads.descendingIterator();
		while (iter.hasNext()) {
			Quad q = iter.next();
			set.removeAll(q.def());
			set.addAll(q.use());
		}
		return set;
	}
	
	public Set<Temp> IN = new LinkedHashSet<Temp>();
	public Set<Temp> OUT = new LinkedHashSet<Temp>();
}
