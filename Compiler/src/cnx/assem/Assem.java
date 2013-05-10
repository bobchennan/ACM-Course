package cnx.assem;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import cnx.temp.*;
import cnx.regalloc.*;
import cnx.env.Constants;

public class Assem {
	
	public String format;
	public Object[] params;
	
	public Assem(String format, Object... params) {
		this.format = format;
		this.params = params;
	}
	
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		for (int i = 0, j = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '@' || c == '%') {
				if (c == '@' && params[j] instanceof Temp) {
					set.add((Temp) params[j]);
				}
				++j;
			}
		}
		return set;
	}
	
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		for (int i = 0, j = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '@' || c == '%') {
				if (c == '%' && params[j] instanceof Temp) {
					set.add((Temp) params[j]);
				}
				++j;
			}
		}
		return set;
	}
	
	public String toString() {
		return toString(DefaultMap.getSingleton());
	}
	
	public String toString(RegAlloc allocator) {
		return isSpill() ? doSpill(allocator) : doNormal(allocator);
	}
	
	private String doNormal(RegAlloc allocator) {
		StringBuffer buf = new StringBuffer();
		
		if (format.charAt(0) == '!') {
			format = format.substring(1);
		}
		else {
			buf.append('\t');
		}
		
		for (int i = 0, j = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '@' || c == '%') {
				if (params[j] instanceof Temp) {
					buf.append(allocator.map((Temp) params[j]));
				}
				else {
					buf.append(params[j]);
				}
				++j;
			}
			else {
				buf.append(c);
			}
		}
		return buf.toString();
	}
	
	private boolean isSpill() {
		for (Object param : params) {
			if (param instanceof Temp && ((Temp) param).getLiveInterval().spilled) {
				return true;
			}
		}
		return false;
	}
	
	private String doSpill(RegAlloc allocator) {
		StringBuffer before = new StringBuffer();
		StringBuffer after = new StringBuffer();
		
		TreeSet<Integer> freeRegs = new TreeSet<Integer>();
		freeRegs.add(26);	// $k0
		freeRegs.add(27);	// $k1
		
		for (Temp t : use()) {
			if (t.getLiveInterval().register == Constants.spillReg) {
				int r = freeRegs.pollFirst();
				t.getLiveInterval().register = r;
				// lw $r, Constants.pointerSize*t.home.depth($gp)
				// lw $r, Constants.pointerSize*t.index($r)
				before.append("\t\t\tlw $" + Constants.regNames[r] + ", " + Constants.pointerSize * t.home.depth + "($gp)\t# load display for spilling\n");
				before.append("\t\t\tlw $" + Constants.regNames[r] + ", " + Constants.pointerSize * t.index + "($" + Constants.regNames[r] + ")\t# load for spilling\n");
			}
		}
		
		for (Temp t : def()) {
			if (t.getLiveInterval().register == Constants.spillReg) {
				int r = freeRegs.pollFirst();
				t.getLiveInterval().register = r;
			}
		}
		
		// possibly a function call
		String normal = doNormal(allocator);

		for (Temp t : def()) {
			if (t.getLiveInterval().spilled && t.getLiveInterval().register != Constants.spillReg) {
				int r = t.getLiveInterval().register;
				t.getLiveInterval().register = Constants.spillReg;
				int d = 26+27 - r;	// another $k?
				// lw $d, Constants.pointerSize*t.home.depth($gp)
				// sw $r, Constants.pointerSize*t.index($d)
				after.append("\n\t\t\tlw $" + Constants.regNames[d] + ", " + Constants.pointerSize * t.home.depth + "($gp)\t# load display for spilling");
				after.append("\n\t\t\tsw $" + Constants.regNames[r] + ", " + Constants.pointerSize * t.index + "($" + Constants.regNames[d] + ")\t# write back for spilling");
			}
		}
		
		for (Temp t : use()) {
			if (t.getLiveInterval().spilled && t.getLiveInterval().register != Constants.spillReg) {
				t.getLiveInterval().register = Constants.spillReg;
			}
		}
		
		return before + normal + after;
	}
}
