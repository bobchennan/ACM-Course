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
	}
	
	private void beginScope(){
		tEnv.beginScope();
		vEnv.endScope();
	}
	
	private void endScope(){
		tEnv.endScope();
		vEnv.endScope();
	}
}