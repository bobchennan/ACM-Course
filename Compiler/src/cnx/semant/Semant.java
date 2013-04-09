package cnx.semant;

import cnx.type.*;
import cnx.env.*;
import cnx.ast.*;

public final class Semant {
	private Env env = null;
	private int errors = 0;
	public Semant(){
		this(new Env());
	}
	public Semant(Env x){
		env = x;
	}
	private void error(){
		++errors;
	}
	public boolean hasError(){
		return errors > 0;
	}
	private boolean checkInt(Type ty){
		return ty.equals(INT.getInstance());
	}
	private boolean CheckChar(Type ty){
		return ty.equals(CHAR.getInstance());
	}
	private boolean CheckAssign(Type l, Type r){
		if(r.equals(VOID.getInstance())){
			error();
			return false;
		}
		return l.equals(r);
	}
	
	private int loopCount = 0;
	public void checkProgram(Program x){
		loopCount = 0;
		if(x._link!=null)checkProgram(x._link);
		if(x._v instanceof Declaration)
			checkProg((Declaration)x._v);
		if(x._v instanceof Function_definition)
			checkProg((Function_definition)x._v);
	}
	public void checkProg(Declaration x){
		Type l = x._ty.toType(env);
		if(x._v!=null){
			Declarators t = x._v;
			while(t._link != null){
				checkDeclarator(t._x);
				t = t._link;
			}
		}
		else{
			Init_declarators t = x._init;
		}
	}
	public void checkProg(Function_definition x){
	}
}

