package cnx.semant;

import cnx.type.*;
import cnx.error.Error;
import cnx.env.*;
import cnx.ast.*;
import java.util.*;

public final class Semant {
	private Env env = null;
	private int errors = 0;
	private Type now;
	public Semant(){
		this(new Env());
	}
	public Semant(Env x){
		env = x;
	}
	private void error(){
		Error.error();
	}
	public boolean hasError(){
		return errors > 0;
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
		if(l == null)error();
		if(x._v != null)
			checkDeclarators(l,x._v);
		else if(x._init != null)
			checkInit_declarators(l,x._init);
		else if(l instanceof RECORD)
			if(env.tEnv.get(((RECORD)l).name) == null)
				if(!env.tEnv.put(((RECORD)l).name, ((RECORD)l)))
					error();
	}
	private Type return_ty = VOID.getInstance();
	public void checkProg(Function_definition x){
		Type ty = x._ty.toType(env);
		if(ty == null)error();
		RECORD formals = checkParameters(x._arg);
		Plain_declarator t = x._x;
		while(t._link != null){
			ty = new POINTER(ty);
			t = t._link;
		}
		if(!env.vEnv.put(t._sym,new FunEntry(formals, ty, (x._arg != null)?(x._arg._ellipsis):(false))))
			error();
		now = ty;
		if(x._st != null){
			env.beginScope();
			for(int i = 0; i < formals.fields.size(); ++i)
				if(!env.vEnv.put(formals.fields.get(i).name,new VarEntry(formals.fields.get(i).type)))
					error();
			if(x._st!=null)
				checkCompound_statement(x._st);
			env.endScope();
		}
	}
	public void checkDeclaration(Declaration x){
		Type ty = x._ty.toType(env);
		if(ty == null)error();
		if(x._init != null)
			checkInit_declarators(ty,x._init);
		if(x._v != null)
			checkDeclarators(ty,x._v);
	}
	public void checkDeclarators(Type l, Declarators x){
		for(int i = 0; x != null && i < x._l.size(); ++i)
			checkDeclarator(l, x._l.get(i), true);
	}
	public void checkInit_declarators(Type l, Init_declarators x){
		for(int i = 0; x != null && i < x._l.size(); ++i)
			checkInit_declarator(l, x._l.get(i));
	}
	public Type checkDeclarator(Type l, Declarator x, boolean isTypedef){
		Plain_declarator t = x._x;
		while(t._link != null){
			l = new POINTER(l);
			t = t._link;
		}
		if(x._isFunc){
			if(isTypedef)error();
			if(!env.vEnv.put(t._sym, new FunEntry(checkParameters(x._arg),l,x._arg._ellipsis)))
				error();
		}
		else {
			Constant_expressions s = x._cexp;
			for(int i = 0; s != null && i < s._l.size(); ++i){
				Type _ty = checkConstant_expression(s._l.get(i));
				if(!_ty.equals(INT.getInstance())&&!_ty.equals(CHAR.getInstance()))error();
				l = new ARRAY(l,0);
			}
			if(isTypedef){
				if(!env.tEnv.put(t._sym, l))
					error();
			}
			else{
				if(l.equals(VOID.getInstance()))error();
				if(!env.vEnv.put(t._sym, new VarEntry(l)))
					error();
			}
		}
		return l;
	}
	public void checkInit_declarator(Type l, Init_declarator x){
		if(x._y!=null){
			if(x._x._isFunc==true)
				error();
			Type ty1=checkDeclarator(l, x._x, false);
			Type ty2=checkInitializer(x._y);
			if(!ty1.equals(ty2))
				error();
		}
		else checkDeclarator(l, x._x, false);
	}
	public Type checkInitializer(Initializer x){
		if(x._x!=null){
			return checkAssignment_expression(x._x);
		}
		else{
			Type ret = INT.getInstance();
			int cnt = 0;
			Initializers t = x._y;
			for(int i = 0; t != null && i < t._l.size(); ++i){
				Type tmp = checkInitializer(t._l.get(i));
				if(ret.equals(INT.getInstance())&&tmp.equals(CHAR.getInstance()))
					tmp=INT.getInstance();
				if(!tmp.equals(ret)){
					++cnt;
					ret = tmp;
				}
			}
			if(cnt > 1)error();
			return new ARRAY(ret,0);
		}
	}
	public RECORD checkParameters(Parameters x){
		RECORD ret = new RECORD(null);
		if(x == null)return ret;
		RECORD.RecordField v = checkPlain_declaration(x._x);
		ret.fields.add(v);
		Plain_declarations y = x._xs;
		for(int i = 0; y != null && i < y._l.size(); ++i){
			v = checkPlain_declaration(y._l.get(i));
			ret.fields.add(v);
		}
		return ret;
	}
	public RECORD.RecordField checkPlain_declaration(Plain_declaration x){
		Type ty = x._ty.toType(env);
		if(ty == null)error();
		Declarator y = x._x;
		Plain_declarator t = y._x;
		while(t._link != null){
			ty = new POINTER(ty);
			t = t._link;
		}
		if(y._isFunc == true)error();
		else {
			Constant_expressions tt = y._cexp;
			for(int i = 0; tt != null && i < tt._l.size(); ++i){
				Type _ty = checkConstant_expression(tt._l.get(i));
				if(!_ty.equals(INT.getInstance())&&!_ty.equals(CHAR.getInstance()))error();
				ty = new ARRAY(ty, 0);
			}
		}
		return new RECORD.RecordField(ty, t._sym, 0);
	}
	public void checkCompound_statement(Compound_statement x){
		Declarations s = x._x;
		for(int i = 0; s != null && i < s._l.size(); ++i)
			checkDeclaration(s._l.get(i));
		Statements t = x._y;
		for(int i = 0; t != null && i < t._l.size(); ++i)
			checkStatement(t._l.get(i));
	}
	public void checkStatement(Statement x){
		if(x instanceof Expression_statement)
			checkStatement((Expression_statement) x);
		if(x instanceof Compound_statement){
			env.beginScope();
			checkCompound_statement((Compound_statement) x);
			env.endScope();
		}
		if(x instanceof Selection_statement)
			checkStatement((Selection_statement) x);
		if(x instanceof Iteration_statement)
			checkStatement((Iteration_statement) x);
		if(x instanceof Jump_statement)
			checkStatement((Jump_statement) x);
	}
	public void checkStatement(Expression_statement x){
		checkExpression(x._x);
	}
	public void checkStatement(Selection_statement x){
		Type ty = checkExpression(x._exp);
		if(!(ty.equals(INT.getInstance()) || ty.equals(CHAR.getInstance())))error();
		checkStatement(x._st1);
		if(x._st2!=null)checkStatement(x._st2);
	}
	public void checkStatement(Iteration_statement x){
		if(x instanceof While_statement){
			Type ty = checkExpression(((While_statement) x)._exp);
			if(!ty.equals(INT.getInstance())&&!ty.equals(CHAR.getInstance()))error();
			++loopCount;
			checkStatement(((While_statement) x)._st);
		}
		else{
			checkExpression(((For_statement)x)._exp1);
			checkExpression(((For_statement)x)._exp2);
			checkExpression(((For_statement)x)._exp3);
			++loopCount;
			checkStatement(((For_statement)x)._st);
		}
		--loopCount;
	}
	public void checkStatement(Jump_statement x){
		if(x instanceof Break_statement){
			if(loopCount < 1)
				error();
		}
		if(x instanceof Continue_statement){
			if(loopCount < 1)
				error();
		}
		if(x instanceof Return_statement){
			Type ty;
			if(((Return_statement)x)._exp==null)ty=VOID.getInstance();
			else ty=checkExpression(((Return_statement) x)._exp);
			if(ty != now)error();
		}
	}
	public Type checkExpression(Expression x){
		Type ret = VOID.getInstance();
		for(int i = 0; i < x._l.size(); ++i){
			ret = checkAssignment_expression(x._l.get(i));
		}
		return ret;
	}
	public Type checkAssignment_expression(Assignment_expression x){
		if(x._lexp != null)
			return checkLogical_or_expression(x._lexp);
		else{
			Type ty1 = checkUnary_expression(x._uexp);
			if(!checkLeft(x._uexp))error();
			Type ty2 = checkAssignment_expression(x._link);
			if(x._aop == Assignment_operator.ASSIGN && ty1 instanceof POINTER && ty2 instanceof POINTER){}
			else if(ty1 instanceof POINTER && (ty2.equals(CHAR.getInstance()) || ty2.equals(INT.getInstance()))){}
			else if(ty1.equals(INT.getInstance())&&ty2.equals(CHAR.getInstance())){}
			else if(ty2.equals(INT.getInstance())&&ty1.equals(CHAR.getInstance())){}
			else if(!ty1.equals(ty2))error();
			return ty1;
		}
	}
	public Type checkConstant_expression(Constant_expression x){
		return checkLogical_or_expression((Logical_or_expression) x);
	}
	public Type checkLogical_or_expression(Logical_or_expression x){
		Type ty1 = checkLogical_and_expression(x._x);
		if(x._link != null){
			if(!ty1.equals(INT.getInstance())&&!ty1.equals(CHAR.getInstance()))error();
			Type ty2 = checkLogical_or_expression(x._link);
			if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance()))error();
		}
		return ty1;
	}
	public Type checkLogical_and_expression(Logical_and_expression x){
		Type ty1 = checkInclusive_or_expression(x._x);
		if(x._link != null){
			if(!ty1.equals(INT.getInstance())&&!ty1.equals(CHAR.getInstance()))error();
			Type ty2 = checkLogical_and_expression(x._link);
			if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance()))error();
		}
		return ty1;
	}
	public Type checkInclusive_or_expression(Inclusive_or_expression x){
		Type ty1 = checkExclusive_or_expression(x._x);
		if(x._link != null){
			if(!ty1.equals(INT.getInstance())&&!ty1.equals(CHAR.getInstance()))error();
			Type ty2 = checkInclusive_or_expression(x._link);
			if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance()))error();
		}
		return ty1;
	}
	public Type checkExclusive_or_expression(Exclusive_or_expression x){
		Type ty1 = checkAnd_expression(x._x);
		if(x._link != null){
			if(!ty1.equals(INT.getInstance())&&!ty1.equals(CHAR.getInstance()))error();
			Type ty2 = checkExclusive_or_expression(x._link);
			if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance()))error();
		}
		return ty1;
	}
	public Type checkAnd_expression(And_expression x){
		Type ty1 = checkEquality_expression(x._x);
		if(x._link != null){
			if(!ty1.equals(INT.getInstance())&&!ty1.equals(CHAR.getInstance()))error();
			Type ty2 = checkAnd_expression(x._link);
			if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance()))error();
		}
		return ty1;
	}
	public Type checkEquality_expression(Equality_expression x){
		Type ty1 = checkRelational_expression(x._x);
		if(ty1.equals(CHAR.getInstance()))ty1 = INT.getInstance();
		if(x._link != null){
			Type ty2 = checkEquality_expression(x._link);
			if(ty2.equals(CHAR.getInstance()))ty2 = INT.getInstance();
			if(!ty1.equals(ty2))error();
			return INT.getInstance();
		}
		return ty1;
	}
	public Type checkRelational_expression(Relational_expression x){
		Type ty1 = checkShift_expression(x._x);
		if(x._link != null){
			Type ty2 = checkRelational_expression(x._link);
			if(!ty1.equals(INT.getInstance())&&!ty1.equals(CHAR.getInstance())&&!(ty1 instanceof POINTER))error();
			if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance())&&!(ty2 instanceof POINTER))error();
			return INT.getInstance();
		}
		return ty1;
	}
	public Type checkShift_expression(Shift_expression x){
		Type ty1 = checkAdditive_expression(x._x);
		if(x._link != null){
			Type ty2 = checkShift_expression(x._link);
			if(!ty1.equals(INT.getInstance())&&!ty1.equals(CHAR.getInstance()))error();
			if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance()))error();
			return INT.getInstance();
		}
		return ty1;
	}
	public Type checkAdditive_expression(Additive_expression x){
		Type ty1 = checkMultiplicative_expression(x._x);
		if(x._link != null){
			Type ty2 = checkAdditive_expression(x._link);
			if(ty1.equals(VOID.getInstance()))error();
			if(ty1 instanceof RECORD)error();
			if(ty2.equals(VOID.getInstance()))error();
			if(ty2 instanceof RECORD)error();
			
			if(x._aop==Additive_operator.PLUS){
				if(ty1 instanceof POINTER || ty2 instanceof POINTER)
					if(ty1 instanceof POINTER && ty2 instanceof POINTER)
						if(ty1.equals(ty2))
							return ty1;
						else error();
					else if(ty1 instanceof POINTER)
						return ty1;
					else return ty2;
				else return INT.getInstance();
			}
			if(x._aop==Additive_operator.MINUS){
				if(!(ty1 instanceof POINTER) && ty2 instanceof POINTER)
					error();
				if(ty1 instanceof POINTER && ty2 instanceof POINTER && ty1.equals(ty2))
					error();
				if(ty1 instanceof POINTER)
					return ty1;
				if(ty2 instanceof POINTER)
					return ty2;
				return INT.getInstance();
			}
		}
		return ty1;
	}
	public Type checkMultiplicative_expression(Multiplicative_expression x){
		Type ty1 = checkCast_expression(x._x);
		if(x._link != null){
			Type ty2 = checkMultiplicative_expression(x._link);
			if(ty1 instanceof POINTER || ty2 instanceof POINTER)
				error();
			return INT.getInstance();
		}
		return ty1;
	}
	public Type checkCast_expression(Cast_expression x){
		Type ty1 = checkUnary_expression(x._x);
		if(x._link != null){
			Type ty2 = checkCast_expression(x._link);
			return ty2;
		}
		return ty1;
	}
	public Type checkUnary_expression(Unary_expression x){
		if(x instanceof Postfix_expression)
			return checkPostfix_expression((Postfix_expression) x);
		if(x instanceof Type_size)
			return INT.getInstance();
		if(x instanceof Unary_cast_expression)
			return checkUnary_cast_expression((Unary_cast_expression) x);
		if(x instanceof Unary_expressions)
			return checkUnary_expressions((Unary_expressions) x);
		return VOID.getInstance();
	}
	public Type checkUnary_cast_expression(Unary_cast_expression x){
		Type ty = checkCast_expression(x._x);
		if(x._uop == Unary_operator.PLUS || x._uop == Unary_operator.MINUS){
			if(ty instanceof POINTER)
				error();
			if(ty.equals(VOID.getInstance()))
				error();
			if(ty instanceof RECORD)
				error();
			return INT.getInstance();
		}
		if(x._uop == Unary_operator.BITAND){
			if(!checkLeft(x._x))error();
			return new POINTER(ty);
		}
		if(x._uop == Unary_operator.ASTER){
			if(!(ty instanceof POINTER))error();
			return ((POINTER)ty).elementType;
		}
		if(x._uop == Unary_operator.BITNOT || x._uop == Unary_operator.NOT){
			if(ty.equals(INT.getInstance()) || ty.equals(CHAR.getInstance()))
				return INT.getInstance();
			else error();
		}
		return VOID.getInstance();
	}
	public Type checkPostfix_expression(Postfix_expression x){
		if(x instanceof Array_expression)
			return checkArray_expression((Array_expression) x);
		if(x instanceof Dec_expression)
			return checkDec_expression((Dec_expression) x);
		if(x instanceof Dot_expression)
			return checkDot_expression((Dot_expression) x);
		if(x instanceof Function_expression)
			return checkFunction_expression((Function_expression) x);
		if(x instanceof Inc_expression)
			return checkInc_expression((Inc_expression) x);
		if(x instanceof Pointer_expression)
			return checkPointer_expression((Pointer_expression) x);
		if(x instanceof Primary_expression)
			return checkPrimary_expression((Primary_expression) x);
		return VOID.getInstance();
	}
	public Type checkArray_expression(Array_expression x){
		Type ty = checkPostfix_expression(x._x);
		Type ty2 = checkExpression(x._y);
		if(!ty2.equals(INT.getInstance())&&!ty2.equals(CHAR.getInstance()))error();
		if(ty instanceof POINTER)
			return ((POINTER)ty).elementType;
		else
			error();
		return VOID.getInstance();
	}
	public Type checkDec_expression(Dec_expression x){
		Type ty = checkPostfix_expression(x._x);
		if(!checkLeft(x._x))error();
		if(!ty.equals(INT.getInstance())&&!ty.equals(CHAR.getInstance()))error();
		else if(ty instanceof POINTER)return ty;
		else return INT.getInstance();
		return VOID.getInstance();
	}
	public Type checkInc_expression(Inc_expression x){
		Type ty = checkPostfix_expression(x._x);
		if(!checkLeft(x._x))error();
		if(!ty.equals(INT.getInstance())&&!ty.equals(CHAR.getInstance()))error();
		else if(ty instanceof POINTER)return ty;
		else return INT.getInstance();
		return VOID.getInstance();
	}
	public Type checkDot_expression(Dot_expression x){
		Type ty = checkPostfix_expression(x._x);
		if(!(ty instanceof RECORD))
			error();
		boolean ok = false;
		for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
			if(((RECORD)ty).fields.get(i).name==x._sym){
				return ((RECORD)ty).fields.get(i).type;
			}
		}
		error();
		return VOID.getInstance();
	}
	public Type checkPointer_expression(Pointer_expression x){
		Type ty = checkPostfix_expression(x._x);
		if(!(ty instanceof POINTER))
			error();
		Type ty2 = ((POINTER)ty).elementType;
		if(!(ty instanceof POINTER))
			error();
		if(!((((POINTER)ty).elementType) instanceof RECORD))
			error();
		RECORD ty3 = (RECORD) ((POINTER)ty).elementType;
		for(int i = 0; i < ty3.fields.size(); ++i){
			if(ty3.fields.get(i).name==x._y)
				return ty3.fields.get(i).type;
		}
		error();
		return VOID.getInstance();
	}
	public Type checkFunction_expression(Function_expression x){
		if(!(x._x instanceof Id))error();
		Entry f = (Entry) env.vEnv.get(((Id)x._x)._sym);
		if(!(f instanceof FunEntry))error();
		Arguments y = x._y;
		int cnt = (y == null)?0:y._l.size();
		for(int i = 0; i < ((FunEntry)f).formals.fields.size(); ++i){
			if(i > cnt){
				error();
				break;
			}
			Type tmp = ((FunEntry)f).formals.fields.get(i).type;
			if(!tmp.equals(checkAssignment_expression(y._l.get(i))))
				error();
		}
		if(!((FunEntry) f).nc)
			if(((FunEntry)f).formals.fields.size() < cnt)
				error();
		return ((FunEntry)f).result;
	}
	public Type checkUnary_expressions(Unary_expressions x){
		Type ty = checkUnary_expression(x._link);
		if(x._uop==Unary_expressions_operator.SIZEOF){
			if(ty.equals(VOID.getInstance()))error();
			return INT.getInstance();
		}
		else{
			if(ty.equals(VOID.getInstance()))error();
			if(ty instanceof RECORD)error();
			return INT.getInstance();
		}
	}
	public Type checkPrimary_expression(Primary_expression x){
		if(x instanceof Constant) return INT.getInstance();
		if(x instanceof Expression) return checkExpression((Expression) x);
		if(x instanceof Id){
			Entry y = (Entry) env.vEnv.get(((Id)x)._sym);
			if(!(y instanceof VarEntry))
				error();
			else return ((VarEntry)y).ty;
			return VOID.getInstance();
		}
		return new POINTER(CHAR.getInstance());
	}
	boolean checkLeft(Object x){
		if(x instanceof StringLiteral)
			return false;
		if(x instanceof Constant)
			return false;
		if(x instanceof Function_expression)
			return false;
		if(x instanceof Dot_expression)
			return checkLeft(((Dot_expression) x)._x);
		if(x instanceof Pointer_expression)
			return true;
		if(x instanceof Cast_expression)
			if(((Cast_expression)x)._link != null)
				return false;
			else return checkLeft(((Cast_expression)x)._x);
		if(x instanceof Array_expression)
			return true;
		if(x instanceof Id)
			return true;
		if(x instanceof Unary_cast_expression){
			if(((Unary_cast_expression)x)._uop == Unary_operator.ASTER)
				return true;
			else
				return checkLeft(((Unary_cast_expression)x)._x);
		}
		return true;
	}
}

