package cnx.env;

import cnx.symbol.*;
import cnx.type.*;

public class Env {
	public Table tEnv = null;
	public Table vEnv = null;
	
	public Env(){
		initTEnv();
		initVEnv();
	}
	
	private static Symbol sym(String n) {
		return Symbol.symbol(n);
	}
	
	private void initTEnv(){
		tEnv = new Table();
		tEnv.put(sym("int"),INT.getInstance());
		tEnv.put(sym("char"),CHAR.getInstance());
		tEnv.put(sym("VOID"),VOID.getInstance());
	}
	
	private void initVEnv(){
		vEnv = new Table();
		RECORD tmp = new RECORD(sym(""));
		tmp.fields.add(new RECORD.RecordField(new POINTER(CHAR.getInstance()), sym("arg"), 0));
		vEnv.put(sym("printf"), new FunEntry(tmp, INT.getInstance(), true));
		vEnv.put(sym("scanf"), new FunEntry(tmp, INT.getInstance(), true));
		RECORD tmp2 = new RECORD(sym(""));
		tmp2.fields.add(new RECORD.RecordField(INT.getInstance(), sym("arg"), 0));
		vEnv.put(sym("malloc"), new FunEntry(tmp2, new POINTER(VOID.getInstance()), false));
	}
	
	public void beginScope(){
		tEnv.beginScope();
		vEnv.beginScope();
	}
	
	public void endScope(){
		tEnv.endScope();
		vEnv.endScope();
	}
}