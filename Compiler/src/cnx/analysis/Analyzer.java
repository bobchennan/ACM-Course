package cnx.analysis;

import cnx.temp.*;
import cnx.quad.*;
import cnx.analysis.LiveInterval;
import java.util.*;

public class Analyzer {
	public LinkedList<Block> getBlocks(List<Quad> quads) {
		identifyLeaders(quads);
		return buildBlocks(quads);
	}

	private void identifyLeaders(List<Quad> quads) {
		quads = new ArrayList<Quad>(quads);

		// clean up
		for (int i = 0; i < quads.size(); i++) {
			quads.get(i).clearAll();
		}

		for (int i = 0; i < quads.size()-1; i++) {
			Quad q = quads.get(i);
			if (!(q instanceof Goto || q instanceof Leave)) {
				q.addSuccessor(quads.get(i+1));
			}
		}

		// gives the basic block `ENTRY'
		quads.get(0).setLeader();	// function label
		quads.get(2).setLeader();	// instruction after `enter'

		// gives the basic block `EXIT'
		for (Quad q : quads) {
			if (q instanceof Leave) {
				q.setLeader();
			}
		}

		for (int i = 0; i < quads.size()-1; i++) {
			Quad q = quads.get(i);
			if (q.isJump()) {
				Quad t = q.jumpTargetIn(quads);
				t.setLeader();
				q.addSuccessor(t);
				quads.get(i+1).setLeader();
			}
		}
	}

	private LinkedList<Block> buildBlocks(List<Quad> quads) {
		LinkedList<Block> blocks = new LinkedList<Block>();
		Block currentBlock = null;
		for (Quad q : quads) {
			if (q.isLeader()) {
				if (currentBlock != null) {
					blocks.add(currentBlock);
				}
				currentBlock = new Block();
			}
			currentBlock.addQuad(q);
		}
		if (currentBlock != null) {
			blocks.add(currentBlock);
		}

		for (Block bb : blocks) {
			Quad last = bb.getLastQuad();
			for (Quad succ : last.getSuccessors()) {
				addFlowEdge(bb, findBlockByQuad(blocks, succ));
			}
		}

		return blocks;
	}

	private void addFlowEdge(Block from, Block to) {
		from.addSuccessor(to);
		to.addPredecessor(from);
	}

	private Block findBlockByQuad(List<Block> blocks, Quad q) {
		for (Block bb : blocks) {
			if (bb.getFirstQuad().equals(q)) {
				return bb;
			}
		}
//		System.out.println("ERROR: can not find a block start with q = \"" + q + "\"");
		return null;
	}

	public void findLiveness(LinkedList<Block> blocks) {
		LinkedList<Block> bb = new LinkedList<Block>();
		Iterator<Block> iter = blocks.descendingIterator();
		iter.next();	// skip EXIT (the Block with `leave LLL')
		while (iter.hasNext()) {
			bb.add(iter.next());
		}

		/**
		 * Liveness may be calculated many times.
		 * So clear the sets every time.
		 */
		for (Block b : bb) {
			b.IN.clear();
			b.OUT.clear();
			for (Quad q : b.getQuads()) {
				q.IN.clear();
				q.OUT.clear();
			}
		}

		boolean changed = true;
		while (changed) {
			changed = false;
			for (Block B : bb) {
				B.OUT.clear();
				for (Block S : B.getSuccessors()) {
					B.OUT.addAll(S.IN);
				}

				Set<Temp> oldIN = new LinkedHashSet<Temp>(B.IN);

				B.IN.clear();
				B.IN.addAll(B.OUT);
				B.IN.removeAll(B.def());
				B.IN.addAll(B.use());

				if (!oldIN.equals(B.IN)) {
					changed = true;
				}
			}
		}

		for (Block B : bb) {
			findLiveness(B.getQuads(), B.OUT);
		}
	}

	private void findLiveness(LinkedList<Quad> quads, Set<Temp> OUT) {
		Iterator<Quad> iter = quads.descendingIterator();
		Quad last = null;
		while (iter.hasNext()) {
			Quad q = iter.next();
			q.OUT = (last == null) ? OUT : last.IN;
			q.IN.addAll(q.OUT);
			q.IN.removeAll(q.def());
			q.IN.addAll(q.use());
			last = q;
		}
	}
	
	public LinkedHashMap<Temp, LiveInterval> findLiveIntervals(List<Quad> quads) {
		for (Quad q : quads) {
			for (Temp t : q.IN) {
				t.clearLiveInterval();
			}
			for (Temp t : q.OUT) {
				t.clearLiveInterval();
			}
			for (Temp t : q.use()) {
				t.clearLiveInterval();
			}
			for (Temp t : q.def()) {
				t.clearLiveInterval();
			}
		}
		LinkedHashMap<Temp, LiveInterval> intervals = new LinkedHashMap<Temp, LiveInterval>();
		int qcount = 0;
		for (Quad q : quads) {
			++qcount;
			for (Temp t : q.IN) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
			for (Temp t : q.OUT) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
			for (Temp t : q.use()) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
			for (Temp t : q.def()) {
				t.expandInterval(qcount);
				intervals.put(t, t.getLiveInterval());
			}
		}
		return intervals;
	}
}
