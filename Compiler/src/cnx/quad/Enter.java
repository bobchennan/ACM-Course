package cnx.quad;

import cnx.temp.*;
import cnx.assem.*;
import cnx.env.Constants;
import cnx.translate.Level;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class Enter extends Quad{

	Label label = null;
	LinkedList<Temp> params = null;
	Level level = null;
	
	public Enter(Label l, LinkedList<Temp> p) {
		label = l;
		params = p;
		level = Constants.now;
	}
	
	public String toString() {
		return "enter " + label;
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = new LinkedHashSet<Temp>();
		set.addAll(params);
		return set;
	}
	
	@Override
	public AssemList gen() {
		return  L(GC_SP_LIMIT(),L(enterMain(),L(new Assem("addiu $sp, $sp, -%", level.frameSize()),                     // allocate stack frame
				L(new Assem("sw $ra, %($sp)", level.frameSize() - Constants.pointerSize),            // save old $ra
				L(level.saveRegisters(), L(loadArguments(), addAllArea()))))));
	}
	
	private AssemList addAllArea(){
		if (label.toString().equals("main")) {
			return  L(new Assem("jal " + Constants.top_level));
		}
		return null;
	}
	
	private AssemList enterMain() {
		if (label.toString().equals("main")) {
			return  L(new Assem("la $gp, disp"),
					L(new Assem("la $v1, args")));
		}
		return null;
	}
	
	private AssemList GC_SP_LIMIT() {
		if (label.toString().equals("main")) {
			return L(new Assem("sw $sp, gc_sp_limit"));
		}
		return null;
	}
	
	private AssemList loadArguments() {
		AssemList loads = null;
		int i = 0;
		while (i < Constants.paramRegNum && i < params.size()) {
			loads = L(loads, L(new Assem("move @, $%", params.get(i), Constants.regNames[Constants.paramRegBase + i])));
			++i;
		}
		if (i < params.size()) {	// param reg is not enough
			while (i < params.size()) {
				loads = L(loads, L(new Assem("lw @, %($v1)", params.get(i), i * Constants.pointerSize)));
				++i;
			}
		}
		return loads;
	}
}
