package cnx.quad;

import cnx.temp.*;
import cnx.assem.*;
import cnx.env.Constants;

import java.util.List;
import java.util.Set;

public class Malloc extends Quad {
	Addr ret = null;
	Addr size = null;
	boolean needEmpty = false;
	
	public Malloc(Addr ret, Addr size){
		this.ret = ret;
		this.size = size.clone();
		if(Constants.now.label.toString() == Constants.top_level)
			needEmpty = true;
	}
	
	public String toString(){
		return ret + " = malloc " + size;
	}
	
	@Override
	public Set<Temp> def() {
		Set<Temp> set = super.def();
		set.add((Temp)ret);
		return set;
	}
	
	@Override
	public Set<Temp> use() {
		Set<Temp> set = super.def();
		if(size instanceof Temp)set.add((Temp)size);
		return set;
	}
	
	@Override
	public AssemList gen() {
		if(!needEmpty)
			if(size instanceof Temp)return L(new Assem("move $a0, %", size), L(new Assem("li $v0, 9"),L(new Assem("syscall"), L(new Assem("move @, $v0", ret)))));
			else return L(new Assem("li $a0, %", size), L(new Assem("li $v0, 9"),L(new Assem("syscall"), L(new Assem("move @, $v0", ret)))));
		else
			if(size instanceof Temp)return L(new Assem("move $a0, %", size), L(new Assem("li $a1, 0"), L(new Assem("jal initArray"), L(new Assem("move @, $v0", ret)))));
			else return L(new Assem("li $a0, %", size), L(new Assem("li $a1, 0"), L(new Assem("jal initArray"), L(new Assem("move @, $v0", ret)))));
	}
}
