package cnx.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import cnx.temp.*;
import cnx.env.Constants;
import cnx.assem.*;

public class Binop extends Quad {
	private Addr dest,x,y;
	private int opStr;
	
	public Binop(Addr dest, Addr x, int opStr, Addr y){
		this.dest = dest;
		this.x = x;
		this.opStr = opStr;
		this.y = y;
	}
	public String toString(){
		return dest.toString() + " = " + x.toString() + " " + Constants.bopStr[opStr] + " " + y.toString();
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.add((Temp)dest);
		return set;
	}

	@Override
	public Set<Temp> use() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		if(x instanceof Temp)set.add((Temp)x);
		if(y instanceof Temp)set.add((Temp)y);
		return set;
	}
	
	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (x.equals(old)) {
			x = t;
		}
		if (y.equals(old)) {
			y = t;
		}
	}
	
	String getOp(int op){
		switch(op){
			case 0: return "addu";
			case 1: return "subu";
			case 2: return "mul";
			case 3: return "divu";
			case 4: return "remu";
			case 5: return "or";
			case 6: return "xor";
			case 7: return "and";
			case 8: return "seq";
			case 9: return "sne";
			case 10: return "slt";
			case 11: return "sle";
			case 12: return "sgt";
			case 13: return "sge";
			case 16: return "sll";
			case 17: return "srl";
			default: return null;
		}
	}
	
	String getOpi(int op){
		switch(op){
			case 0: return "addiu";
			case 1: return "subu";
			case 2: return "mul";
			case 3: return "divu";
			case 4: return "remu";
			case 5: return "or";
			case 6: return "xor";
			case 7: return "and";
			case 8: return "seq";
			case 9: return "sne";
			case 10: return "slt";
			case 11: return "sle";
			case 12: return "sgt";
			case 13: return "sge";
			case 16: return "sll";
			case 17: return "srl";
			default: return null;
		}
	}
	
	@Override
	public AssemList gen() {
		if(y instanceof Temp)return L(new Assem(getOp(opStr)+" @, %, %", dest, x, y));
		else return L(new Assem(getOpi(opStr)+" @, %, %", dest, x, y));
	}
}
