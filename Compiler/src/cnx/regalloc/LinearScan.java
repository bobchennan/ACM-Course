package cnx.regalloc;

import java.util.*;
import cnx.analysis.*;
import cnx.env.Constants;
import cnx.translate.*;
import cnx.temp.Temp;

public final class LinearScan implements RegAlloc, Comparator<LiveInterval> {

	private static final int R = Constants.numOfSavedRegisters;
	private TreeSet<Integer> freeRegs = new TreeSet<Integer>();
	private ArrayList<LiveInterval> active = new ArrayList<LiveInterval>();

	public LinearScan(CompilationUnit cu, Analyzer analyzer) {
		for (int i = 0; i < R; i++) {
			freeRegs.add(i + Constants.baseOfSavedRegisters);
		}
		
		cu.findLiveIntervals(analyzer);
		
		for (LiveInterval i : cu.getLiveIntervals()) {
			expireOldIntervals(i);
			if (active.size() == R || i.getTemp().allArea || i.getTemp().haveAddress) {
				spillAtInterval(i);
			}
			else {
				i.register = getFreeRegister();
				active.add(i);
			}
		}
		
		for (LiveInterval i : cu.getLiveIntervals()) {
			if (!i.spilled) {
				cu.getLevel().useRegister(i.register);
			}
		}
	}
	
	public void expireOldIntervals(LiveInterval i) {
		Collections.sort(active, this);
		ListIterator<LiveInterval> iter = active.listIterator();
		while (iter.hasNext()) {
			LiveInterval j = iter.next();
			if (j.getEndPoint() >= i.getStartPoint()) {
				return;
			}
			putFreeRegister(j.register);
			iter.remove();
		}
	}
	
	public void spillAtInterval(LiveInterval i) {
		if(i.getTemp().allArea){
			i.spilled = true;
			i.register = Constants.spillReg;
			return;
		}
		LiveInterval spill = active.get(active.size() - 1);	// last interval in active
		if (spill.getEndPoint() > i.getEndPoint()) {
			i.register = spill.register;
			spill.spilled = true;
			spill.register = Constants.spillReg;
			active.remove(spill);
			active.add(i);
		}
		else {
			i.spilled = true;
			i.register = Constants.spillReg;
		}
	}
	
	private int getFreeRegister() {
		return freeRegs.pollFirst();
	}
	
	private void putFreeRegister(int r) {
		freeRegs.add(r);
	}

	@Override
	public String map(Temp temp) {
		LiveInterval i = temp.getLiveInterval();
		return "$" + Constants.regNames[i.register];
	}
	
	public String map(Set<Temp> t){
		Set<String> ret = new LinkedHashSet<String>();
		for(Temp i : t)
			ret.add(map(i));
		return ret.toString();
	}

	/**
	 * sort by increasing end point
	 */
	@Override
	public int compare(LiveInterval a, LiveInterval b) {
		return a.getEndPoint() - b.getEndPoint();
	}
}
