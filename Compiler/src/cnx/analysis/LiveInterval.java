package cnx.analysis;

import cnx.temp.*;
import cnx.env.Constants;

public class LiveInterval implements Comparable<LiveInterval> {

	public LiveInterval(Temp temp, int pos) {
		this(temp, pos, pos);
	}
	
	private Temp t = null;
	private int sp = 0;
	private int ep = 0;
	public int register = Constants.spillReg;
	public boolean spilled = false;
	
	public LiveInterval(Temp temp, int startpoint, int endpoint) {
		t = temp;
		sp = startpoint;
		ep = endpoint;
	}

	public void insert(int pos) {
		if (pos < sp) sp = pos;
		if (pos > ep) ep = pos;
	}
	
	public Temp getTemp() {
		return t;
	}
	
	/**
	 * Inclusive
	 */
	public int getStartPoint() {
		return sp;
	}

	/**
	 * Exclusive
	 */
	public int getEndPoint() {
		return ep + 1;
	}

	public boolean intersectWith(LiveInterval other) {
		return (sp <= other.sp && other.sp <= ep) 
			|| (sp <= other.ep && other.ep <= ep);
	}

	@Override
	public String toString() {
		return "{interval:[" + sp + ", " + ep + "],register:" + register + "}";
	}
	
	@Override
	public int compareTo(LiveInterval other) {
		return sp - other.sp;
	}
}
