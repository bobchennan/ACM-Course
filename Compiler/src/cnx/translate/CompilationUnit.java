package cnx.translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import cnx.analysis.*;
import cnx.temp.*;
import cnx.quad.*;

public class CompilationUnit {
	
	private List<Quad> quads = null;
	private Label label = null;

	public CompilationUnit(List<Quad> quads, Label label) {
		this.quads = quads;
		this.label = label;
	}

	public List<Quad> getQuads() {
		return quads;
	}
	
	public void setQuads(List<Quad> quads) {
		if (!(quads instanceof LinkedList)) {
			quads = new LinkedList<Quad>(quads);
		}
		// keep it a list
		this.quads = quads;
	}
	
	public Label getLabel() {
		return label;
	}
	
	private LinkedList<Block> blocks = null;
	
	public void findBlocks(Analyzer analyzer) {
		blocks = analyzer.getBlocks(quads);
	}
	
	public void findLiveness(Analyzer analyzer) {
		analyzer.findLiveness(blocks);
	}

	public LinkedList<Block> getBlocks() {
		return blocks;
	}
	
	private ArrayList<LiveInterval> liveIntervals;
	
	public void findLiveIntervals(Analyzer analyzer) {
		liveIntervals = new ArrayList<LiveInterval>(analyzer.findLiveIntervals(quads).values());
		Collections.sort(liveIntervals);	// sort by start point
	}
	
	public ArrayList<LiveInterval> getLiveIntervals() {
		return liveIntervals;
	}
	
	private TreeSet<Integer> usedRegisters = new TreeSet<Integer>();

	public void useRegister(int r) {
		usedRegisters.add(r);
	}
}