package cnx.quad;

import java.util.LinkedHashSet;
import java.util.Set;

import cnx.env.Constants;
import cnx.temp.*;
import cnx.ast.*;
import cnx.assem.*;

public class Unary extends Quad {
	private Addr dest,x;
	private int opStr;
	private Type_name ty = null;
	
	public Unary(Addr dest, int opStr, Addr x){
		this.dest = dest;
		this.opStr = opStr;
		this.x = x;
	}
	public Unary(Addr dest, Type_name ty, Addr x){
		this.dest = dest;
		this.ty = ty;
		this.x = x;
	}
	private String getStr(Type_specifier x){
		if(x instanceof IntType)
			return "int";
		if(x instanceof CharType)
			return "char";
		if(x instanceof VoidType)
			return "void";
		return "POINTER";
	}
	private String getStr(Type_name x){
		String s = getStr(x._ty)+" ";
		for(int i = 0; i < x.cnt; ++i)
			s+="*";
		return "("+s+")";
	}
	public String toString(){
		if(ty == null)
			return dest.toString() + " = " + " " + Constants.sopStr[opStr] + " " + x.toString(); 
		else
			return dest.toString() + " = " + " " + getStr(ty) + " " +x.toString();
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
		return set;
	}
	
	@Override
	public void replaceUseOf(Temp old, Temp t) {
		if (x.equals(old)) {
			x = t;
		}
	}
	
	@Override
	public AssemList gen(){
		switch(opStr){
			case 0: return L(new Assem("neg @, %", dest, x));
			case 3: return L(new Assem("not @, %", dest, x));
			case 4: return L(new Assem("seq @, %, $zero", dest, x));
			case 5: return L(new Assem("addi @, %, %", dest, x, new Const(1)));
			case 6: return L(new Assem("subi @, %, %", dest, x, new Const(1)));
		}
		return null;
	}
}
