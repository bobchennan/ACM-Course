package cnx.translate;

import java.io.*;
import java.util.*;

import cnx.symbol.*;
import cnx.ast.*;
import cnx.env.*;
import cnx.type.*;
import cnx.quad.*;
import cnx.temp.*;
import cnx.semant.*;

public class Translate extends Semant{
	public List<Quad> ans;
	public List<CompilationUnit> fun;
	public Translate(){
		this(new Env());
	}
	public Translate(Env x){
		env = x;
		ans=new ArrayList<Quad>();
		fun=new ArrayList<CompilationUnit>();
	}
	private void emit(Quad x){
		ans.add(x);
	}
	private void emit(CompilationUnit x){
		fun.add(x);
	}
	private Stack<Label> breakLabels = new Stack<Label>();
	private Stack<Label> continueLabels = new Stack<Label>();

	private void popLabel(Stack<Label> t) {
		t.pop();
	}
	private void pushLabel(Stack<Label> t, Label l) {
		t.push(l);
	}
	private Label breakLabel(Stack<Label> t) {
		return t.peek();
	}
	Addr getSize(Type x){
		if(x instanceof ARRAY)
			return (Addr)((ARRAY)x).capacity;
		if(x instanceof POINTER)
			return new Const(Constants.pointerSize);
		if(x instanceof CHAR)
			return new Const(Constants.charSize);
		if(x instanceof INT)
			return new Const(Constants.intSize);
		if(x instanceof RECORD){
			Addr ret = new Temp();
			for(int i = 0; i < ((RECORD)x).fields.size(); ++i){
				Addr tmp = getSize(((RECORD)x).fields.get(i).type);
				if(i != 0)emit(new Binop(ret, ret, 0, tmp));
				else emit(new Move(ret, tmp));
			}
			return ret;
		}
		return null;
	}
	Addr getSize(Type_name x){
		return getSize(x._ty.toType(env));
	}
	Addr getSize(Unary_expression x){
		return getSize(checkUnary_expression(x));
	}
	public void tranProgram(Program x){
		if(x._link != null)tranProgram(x._link);
		if(x._v instanceof Declaration)
			tranDeclaration((Declaration)x._v);
		if(x._v instanceof Function_definition)
			tranFunction_definition((Function_definition)x._v);
	}
	public void tranDeclaration(Declaration x){
		Type l = x._ty.toType(env);
		if(x._v != null)
			tranDeclarators(l,x._v);
		else if(x._init != null)
			tranInit_declarators(l,x._init);
		else if(l instanceof RECORD)
			if(env.tEnv.get(((RECORD)l).name) == null)
				env.tEnv.put(((RECORD)l).name, ((RECORD)l));
	}
	public void tranFunction_definition(Function_definition x){
		Type ty = x._ty.toType(env);
		RECORD formals = tranParameters(x._arg);
		Plain_declarator t = x._x;
		while(t._link != null){
			ty = new POINTER(ty);
			t = t._link;
		}
		env.vEnv.put(t._sym,new FunEntry(formals, ty, (x._arg != null)?(x._arg._ellipsis):(false)));
		if(x._st != null){
			Label f = Label.forFunction(t._sym);
			LinkedList<Temp> params = new LinkedList<Temp>();
			for(int i = 0; i < formals.fields.size(); ++i){
				Temp tmp = new Temp();
				env.vEnv.put(formals.fields.get(i).name,new VarEntry(formals.fields.get(i).type, tmp));
				params.add(tmp);
			}
			Translate son = new Translate(env);
			son.emit(new LABEL(f));
			son.emit(new Enter(f, params));
			env.beginScope();
			if(x._st != null)
				son.tranCompound_statement(x._st);
			env.endScope();
			son.emit(new Leave(f));
			emit(new CompilationUnit(son.ans, f));
		}
	}
	public void tranDeclarators(Type l, Declarators x){
		for(int i = 0; x != null && i < x._l.size(); ++i)
			tranDeclarator(l, x._l.get(i), true);
	}
	public Addr tranDeclarator(Type l, Declarator x, boolean isTypedef){
		Plain_declarator t = x._x;
		while(t._link != null){
			l = new POINTER(l);
			t = t._link;
		}
		if(x._isFunc)
			env.vEnv.put(t._sym, new FunEntry(tranParameters(x._arg),l,x._arg._ellipsis));
		else {
			Constant_expressions s = x._cexp;
			Addr ret = null;
			if(s != null)
				for(int i = s._l.size() - 1; i >= 0 ; --i){
					Addr tmp = tranConstant_expression(s._l.get(i));
					if(ret == null)ret = tmp;
					else{
						Addr tt = new Temp();
						emit(new Binop(tt, ret, 2, tmp));
						ret = tt;
					}
					l = new ARRAY(l, ret, tmp);
				}
			if(isTypedef)
				env.tEnv.put(t._sym, l);
			else{
				Addr re = null;
				if(l instanceof ARRAY)
					re = tranArray(l);
				else if(l instanceof RECORD)
					re = tranRecord((RECORD)l);
				else{
					re = new Temp();
					emit(new Move(re, new Const(0)));
				}
				env.vEnv.put(t._sym, new VarEntry(l, re));
				return re;
			}
		}
		return null;
	}
	Addr tranArray(Type ty){
		if(((ARRAY)ty).elementType instanceof ARRAY){
			Label begin = new Label();
			Label next = new Label();
			Temp i = new Temp();
			Temp cmp = new Temp();
			Temp limit = new Temp();
			
			emit(new Binop(limit, (Addr)((ARRAY)ty).other, 2, new Const(Constants.pointerSize)));
			Addr tmp = new Temp();
			emit(new Malloc(tmp, limit));
			emit(new Move(i, tmp));
			emit(new Binop(limit, limit, 0, tmp));
			emit(new LABEL(begin));
			emit(new Binop(cmp, i, 10, limit));
			emit(new IfFalse(cmp, next));
			Addr tmp2 = tranArray(((ARRAY)ty).elementType);
			emit(new Store(i, new Const(0), tmp2));
			emit(new Binop(i, i, 0, new Const(Constants.pointerSize)));
			emit(new Goto(begin));
			emit(new LABEL(next));
			return tmp;
		}
		else{
			if(((ARRAY)ty).elementType instanceof RECORD){
				Addr ret = new Temp();
				Addr tmp = new Temp();
				Label begin = new Label();
				Label next = new Label();
				Temp i = new Temp();
				Temp cmp = new Temp();
				Temp limit = new Temp();
				emit(new Binop(tmp, new Const(Constants.pointerSize), 2, (Addr)((ARRAY)ty).other));
				emit(new Malloc(ret, tmp));
				emit(new Binop(limit, ret, 0, tmp));
				emit(new Move(i, tmp));
				emit(new LABEL(begin));
				emit(new Binop(cmp, i, 10, limit));
				emit(new IfFalse(cmp, next));
				Addr tmp2 = tranRecord((RECORD)((ARRAY)ty).elementType);
				emit(new Store(i, new Const(0), tmp2));
				emit(new Binop(i, i, 0, new Const(Constants.pointerSize)));
				emit(new Goto(begin));
				emit(new LABEL(next));
				return ret;
			}
			else{
				Addr tmp = getSize(ty);
				Addr ret = new Temp();
				if(tmp instanceof Temp)
					emit(new Malloc(ret, tmp));
				else{
					Addr tmp2 = new Temp();
					emit(new Move(tmp2, tmp));
					emit(new Malloc(ret, tmp2));
				}
				return ret;
			}
		}
	}
	Addr tranRecord(RECORD ty){
		Addr ret = new Temp();
		Addr tmp = new Temp();
		emit(new Move(tmp, new Const(0)));
		for(int i = 0; i < ty.fields.size(); ++i)
			if(ty.fields.get(i).type instanceof ARRAY || ty.fields.get(i).type instanceof RECORD)
				emit(new Binop(tmp, tmp, 0, new Const(Constants.pointerSize)));
			else
				emit(new Binop(tmp, tmp, 0, getSize(ty.fields.get(i).type)));
		emit(new Malloc(ret, tmp));
		emit(new Move(tmp, new Const(0)));
		for(int i = 0; i < ty.fields.size(); ++i)
			if(ty.fields.get(i).type instanceof ARRAY || ty.fields.get(i).type instanceof RECORD){
				Addr tmp2 = new Temp();
				emit(new Binop(tmp2, ret, 0, tmp));
				if(ty.fields.get(i).type instanceof ARRAY)
					emit(new Store(tmp2, new Const(0), tranArray(ty.fields.get(i).type)));
				else
					emit(new Store(tmp2, new Const(0), tranRecord((RECORD)ty.fields.get(i).type)));
				emit(new Binop(tmp, tmp, 0, new Const(Constants.pointerSize)));
			}
			else
				emit(new Binop(tmp, tmp, 0, getSize(ty.fields.get(i).type)));
		return ret;
	}
	public void tranInit_declarators(Type l, Init_declarators x){
		for(int i = 0; x != null && i < x._l.size(); ++i)
			tranInit_declarator(l, x._l.get(i));
	}
	public void tranInit_declarator(Type l, Init_declarator x){
		if(x._y == null)tranDeclarator(l, x._x, false);
		else{
			Addr t = tranDeclarator(l, x._x, false);
			Move(t, x._y);
		}
	}
	public void Move(Addr t1, Initializer y){
		if(y._x != null){
			Addr t = tranAssignment_expression(y._x);
			Type ty = checkAssignment_expression(y._x);
			if(ty instanceof RECORD)
				moveRecord(ty, t1, t);
			else
				emit(new Move(t1, t));
		}
		else for(int i = 0; y._y != null && y._y._l != null && i < y._y._l.size(); ++i){
			if(y._y._l.get(i)._x == null){
				Addr t2 = new Temp();
				emit(new Load(t2, t1, new Const(i)));
				Move(t2, y._y._l.get(i));
			}
			else emit(new Store(t1,new Const(i),tranAssignment_expression(y._y._l.get(i)._x)));
		}
	}
	public RECORD tranParameters(Parameters x){
		RECORD ret = new RECORD(null);
		if(x == null)return ret;
		RECORD.RecordField v = tranPlain_declaration(x._x);
		ret.fields.add(v);
		Plain_declarations y = x._xs;
		for(int i = 0; y != null && y._l != null && i < y._l.size(); ++i){
			v = tranPlain_declaration(y._l.get(i));
			ret.fields.add(v);
		}
		return ret;
	}
	public RECORD.RecordField tranPlain_declaration(Plain_declaration x){
		Type ty = x._ty.toType(env);
		Declarator y = x._x;
		Plain_declarator t = y._x;
		while(t._link != null){
			ty = new POINTER(ty);
			t = t._link;
		}
		Constant_expressions tt = y._cexp;
		for(int i = 0; tt != null && i < tt._l.size(); ++i){
			Addr _ty = tranConstant_expression(tt._l.get(i));
			ty = new ARRAY(ty, _ty);
		}
		return new RECORD.RecordField(ty, t._sym, 0);
	}
	public void tranCompound_statement(Compound_statement x){
		Declarations s = x._x;
		for(int i = 0; s != null && i < s._l.size(); ++i)
			tranDeclaration(s._l.get(i));
		Statements t = x._y;
		for(int i = 0; t != null && i < t._l.size(); ++i)
			tranStatement(t._l.get(i));
	}
	public void tranStatement(Statement x){
		if(x instanceof Expression_statement)
			tranStatement((Expression_statement) x);
		if(x instanceof Compound_statement){
			env.beginScope();
			tranCompound_statement((Compound_statement) x);
			env.endScope();
		}
		if(x instanceof Selection_statement)
			tranStatement((Selection_statement) x);
		if(x instanceof Iteration_statement)
			tranStatement((Iteration_statement) x);
		if(x instanceof Jump_statement)
			tranStatement((Jump_statement) x);
	}
	public void tranStatement(Expression_statement x){
		tranExpression(x._x);
	}
	public void tranStatement(Selection_statement x){
		if(x._st2 == null){
			Label next = new Label();
			Addr tmp = tranExpression(x._exp);
			emit(new IfFalse(tmp, next));
			tranStatement(x._st1);
			emit(new LABEL(next));
		}
		else{
			Label next = new Label();
			Label otherwise = new Label();
			Addr tmp = tranExpression(x._exp);
			emit(new IfFalse(tmp, otherwise));
			tranStatement(x._st1);
			emit(new Goto(next));
			emit(new LABEL(otherwise));
			tranStatement(x._st2);
			emit(new LABEL(next));
		}
	}
	public void tranStatement(Iteration_statement x){
		if(x instanceof While_statement){
			Label begin = new Label();
			Label next = new Label();
			
			emit(new LABEL(begin));
			Addr tmp = tranExpression(((While_statement) x)._exp);
			Addr tmp2 = new Temp();
			emit(new Binop(tmp2, tmp, 8, new Const(0)));
			emit(new IfFalse(tmp2, next));
			pushLabel(breakLabels, next);
			tranStatement(((While_statement) x)._st);
			popLabel(breakLabels);
			emit(new Goto(begin));
			emit(new LABEL(next));
		}
		else{
			Label begin = new Label();
			Label next = new Label();
			Label check = new Label();
			
			tranExpression(((For_statement) x)._exp1);
			emit(new LABEL(check));
			Addr tmp = tranExpression(((For_statement)x)._exp2);
			Addr tmp2 = new Temp();
			emit(new Binop(tmp2, tmp, 8, new Const(0)));
			emit(new IfFalse(tmp2, next));
			emit(new LABEL(begin));
			pushLabel(breakLabels, next);
			pushLabel(continueLabels, check);
			tranStatement(((For_statement)x)._st);
			popLabel(breakLabels);
			popLabel(continueLabels);
			tmp = tranExpression(((For_statement)x)._exp2);
			emit(new IfFalse(tmp, next));
			tranExpression(((For_statement)x)._exp3);
			emit(new Goto(begin));
			emit(new LABEL(next));
		}
	}
	public void tranStatement(Jump_statement x){
		if(x instanceof Break_statement)
			emit(new Goto(breakLabel(breakLabels)));
		if(x instanceof Continue_statement)
			emit(new Goto(breakLabel(continueLabels)));
		if(x instanceof Return_statement){
			Addr tmp = tranExpression(((Return_statement)x)._exp);
			if(tmp instanceof Temp)
				emit(new Return(tmp));
			else{
				Addr tmp2 = new Temp();
				emit(new Move(tmp2, tmp));
				emit(new Return(tmp2));
			}
		}
	}
	public Addr tranExpression(Expression x){
		if(x == null || x._l == null)return null;
		Addr ret = tranAssignment_expression(x._l.get(0));
		for(int i = 1; x._l != null && i < x._l.size(); ++i){
			ret = tranAssignment_expression(x._l.get(i));
		}
		return ret;
	}
	private Unary_expression Left;
	private Addr Right;
	private int Assign_op;
	public Addr tranAssignment_expression(Assignment_expression x){
		if(x._lexp != null)
			return tranLogical_or_expression(x._lexp);
		else{
			Right = tranAssignment_expression(x._link);
			switch(x._aop){
				case ASSIGN: Assign_op = -1;break;
				case MULASS: Assign_op = 2;break;
				case DIVASS: Assign_op = 3;break;
				case MODASS: Assign_op = 4;break;
				case ADDASS: Assign_op = 0;break;
				case SUBASS: Assign_op = 1;break;
				case SHLASS: Assign_op = 16;break;
				case SHRASS: Assign_op = 17;break;
				case ANDASS: Assign_op = 7;break;
				case XORASS: Assign_op = 6;break;
				case ORASS: Assign_op = 5;break;
			}
			Left = x._uexp;
			Addr t1 = tranUnary_expression(x._uexp);
			Left = null;
			return t1;
		}
	}
	public Addr tranConstant_expression(Constant_expression x){
		return tranLogical_or_expression((Logical_or_expression) x);
	}
	public Addr tranLogical_or_expression(Logical_or_expression x){
		if(x._link == null)
			return tranLogical_and_expression(x._x);
		else{
			Label next = new Label();
			Label otherwise = new Label();
			
			Addr ret = new Temp();
			Addr t1 = tranLogical_or_expression(x._link);
			emit(new IfFalse(t1, next));
			emit(new Move(ret, new Const(1)));
			emit(new Goto(otherwise));
			emit(new LABEL(next));
			Addr t2 = tranLogical_and_expression(x._x);
			emit(new Move(ret, t2));
			emit(new LABEL(otherwise));
			return ret;
		}
	}
	public Addr tranLogical_and_expression(Logical_and_expression x){
		if(x._link == null)
			return tranInclusive_or_expression(x._x);
		else{
			Addr ret = new Temp();
			emit(new Move(ret, new Const(0)));
			Label next = new Label();
			Addr t1 = tranLogical_and_expression(x._link);
			emit(new IfFalse(t1, next));
			Addr t2 = tranInclusive_or_expression(x._x);
			emit(new Move(ret, t2));
			emit(new LABEL(next));
			return ret;
		}
	}
	public Addr tranInclusive_or_expression(Inclusive_or_expression x){
		if(x._link == null)
			return tranExclusive_or_expression(x._x);
		else{
			Addr t1 = tranInclusive_or_expression(x._link);
			Addr t2 = tranExclusive_or_expression(x._x);
			Addr ret = new Temp();
			emit(new Binop(ret, t1, 5, t2));
			return ret;
		}
	}
	public Addr tranExclusive_or_expression(Exclusive_or_expression x){
		if(x._link == null)
			return tranAnd_expression(x._x);
		else{
			Addr t1 = tranExclusive_or_expression(x._link);
			Addr t2 = tranAnd_expression(x._x);
			Addr ret = new Temp();
			emit(new Binop(ret, t1, 6, t2));
			return ret;
		}
	}
	public Addr tranAnd_expression(And_expression x){
		if(x._link == null)
			return tranEquality_expression(x._x);
		else{
			Addr t1 = tranAnd_expression(x._link);
			Addr t2 = tranEquality_expression(x._x);
			Addr ret = new Temp();
			emit(new Binop(ret, t1, 7, t2));
			return ret;
		}
	}
	public Addr tranEquality_expression(Equality_expression x){
		if(x._link == null)
			return tranRelational_expression(x._x);
		else{
			Addr t1 = tranEquality_expression(x._link);
			Addr t2 = tranRelational_expression(x._x);
			Addr ret = new Temp();
			switch(x._eop){
				case EQ: emit(new Binop(ret, t1, 8, t2));break;
				case NE: emit(new Binop(ret, t1, 9, t2));break;
			}
			return ret;
		}
	}
	public Addr tranRelational_expression(Relational_expression x){
		if(x._link == null)
			return tranShift_expression(x._x);
		else{
			Addr t1 = tranRelational_expression(x._link);
			Addr t2 = tranShift_expression(x._x);
			Addr ret = new Temp();
			switch(x._rop){
				case LT: emit(new Binop(ret, t1, 10, t2));break;
				case LE: emit(new Binop(ret, t1, 11, t2));break;
				case GT: emit(new Binop(ret, t1, 12, t2));break;
				case GE: emit(new Binop(ret, t1, 13, t2));break;
			}
			return ret;
		}
	}
	public Addr tranShift_expression(Shift_expression x){
		if(x._link == null)
			return tranAdditive_expression(x._x);
		else{
			Addr t1 = tranShift_expression(x._link);
			Addr t2 = tranAdditive_expression(x._x);
			Addr ret = new Temp();
			switch(x._sop){
				case SHL: emit(new Binop(ret, t1, 16, t2));break;
				case SHR: emit(new Binop(ret, t1, 17, t2));break;
			}
			return ret;
		}
	}
	public Addr tranAdditive_expression(Additive_expression x){
		if(x._link == null)
			return tranMultiplicative_expression(x._x);
		else{
			Addr t1 = tranAdditive_expression(x._link);
			Addr t2 = tranMultiplicative_expression(x._x);
			Addr ret = new Temp();
			switch(x._aop){
				case PLUS: emit(new Binop(ret, t1, 0, t2));break;
				case MINUS: emit(new Binop(ret, t1, 1, t2));break;
			}
			return ret;
		}
	}
	public Addr tranMultiplicative_expression(Multiplicative_expression x){
		if(x._link == null)
			return tranCast_expression(x._x);
		else{
			Addr t1 = tranMultiplicative_expression(x._link);
			Addr t2 = tranCast_expression(x._x);
			Addr ret = new Temp();
			switch(x._mop){
				case MULTIPLY: emit(new Binop(ret, t1, 2, t2));break;
				case DIVIDE: emit(new Binop(ret, t1, 3, t2));break;
				case MOD: emit(new Binop(ret, t1, 4, t2));break;
			}
			return ret;
		}
	}
	public Addr tranCast_expression(Cast_expression x){
		if(x._x != null)
			return tranUnary_expression(x._x);
		else{
			Addr t = new Temp();
			Addr t1 = tranCast_expression(x._link);
			emit(new Unary(t, x._ty, t1));
			return t;
		}
	}
	public Addr tranUnary_expression(Unary_expression x){
		if(x instanceof Postfix_expression)
			return tranPostfix_expression((Postfix_expression)x);
		if(x instanceof Type_size)
			return getSize(((Type_size)x)._ty);
		if(x instanceof Unary_cast_expression){
			Addr t = tranCast_expression(((Unary_cast_expression)x)._x);
			Type ty = checkCast_expression(((Unary_cast_expression)x)._x);
			Addr t1 = new Temp();
			switch(((Unary_cast_expression)x)._uop){
				case BITAND: emit(new Unary(t1, 1, t));break;
				case ASTER: {
					if(x == Left){
						if(Assign_op==-1){
							if(ty instanceof RECORD){
								Addr tmp = new Temp();
								emit(new Load(tmp, t, new Const(0)));
								moveRecord(ty, t, tmp);
							}
							else
								emit(new Store(t, new Const(0), Right));
							emit(new Move(t1, Right));
						}
						else{
							Addr tmp = new Temp();
							emit(new Load(tmp, t, new Const(0)));
							emit(new Binop(tmp, tmp, Assign_op, Right));
							emit(new Store(t, new Const(0), tmp));
							emit(new Move(t1, tmp));
						}
					}
					else
						emit(new Unary(t1, 2, t));
					break;
				}
				case PLUS: return t;
				case MINUS: emit(new Unary(t1, 0, t));break;
				case BITNOT: emit(new Unary(t1, 3, t));break;
				case NOT: emit(new Unary(t1, 4, t));break;
			}
			return t1;
		}
		if(x instanceof Unary_expressions){
			Addr t = tranUnary_expression(((Unary_expressions)x)._link);
			Addr ret = new Temp();
			switch(((Unary_expressions)x)._uop){
				case SIZEOF: return getSize(((Unary_expressions)x)._link);
				case INC: emit(new Unary(ret, 5, t));break;
				case DEC: emit(new Unary(ret, 6, t));break;
			}
			return ret;
		}
		return null;
	}
	public Addr tranPostfix_expression(Postfix_expression x){
		if(x instanceof Array_expression)
			return tranArray_expression((Array_expression)x);
		if(x instanceof Dec_expression){
			Addr t = new Temp();
			Addr t1 = tranPostfix_expression(((Dec_expression)x)._x);
			emit(new Move(t, t1));
			emit(new Unary(t1, 6, t1));
			return t;
		}
		if(x instanceof Inc_expression){
			Addr t = new Temp();
			Addr t1 = tranPostfix_expression(((Inc_expression)x)._x);
			emit(new Move(t, t1));
			emit(new Unary(t1, 5, t1));
			return t;
		}
		if(x instanceof Function_expression)
			return tranFunction_expression((Function_expression)x);
		if(x instanceof Dot_expression)
			return tranDot_expression((Dot_expression)x);
		if(x instanceof Pointer_expression)
			return tranPointer_expression((Pointer_expression)x);
		return tranPrimary_expression((Primary_expression)x);
	}
	public Addr tranFunction_expression(Function_expression x){
		Symbol name = ((Id)x._x)._sym;
		Entry f = (Entry) env.vEnv.get(((Id)x._x)._sym);
		Arguments y = x._y;
		int cnt = (y != null && y._l != null)?y._l.size():0;
		LinkedList<Temp> params = new LinkedList<Temp>();
		for(int i = 0; i < cnt; ++i){
			Addr tmp = tranAssignment_expression(y._l.get(i));
			if(!(tmp instanceof Temp)){
				Temp tm = new Temp();
				emit(new Move(tm, tmp));
				params.add(tm);
			}
			else params.add((Temp)tmp);
		}
		if(((FunEntry)f).result.equals(VOID.getInstance())){
			emit(new Call(Label.forFunction(name),params));
			return null;
		}
		else{
			Addr ret = new Temp();
			emit(new Call(ret,Label.forFunction(name), params));
			return ret;
		}
	}
	public Addr tranArray_expression(Array_expression x){
		Addr now = tranExpression(x._y);
		Addr l = tranPostfix_expression(x._x);
		Addr ret = new Temp();
		Type ty = checkArray_expression(x);
		if(now instanceof Const){
			if(x == Left){
				if(ty instanceof RECORD){
					Addr tmp = new Temp();
					emit(new Load(tmp, l ,((Const)now)));
					moveRecord(ty, tmp, Right);
				}
				else
					emit(new Store(l, ((Const)now), Right));
				emit(new Move(ret, Right));
			}
			else
				emit(new Load(ret, l, ((Const)now)));
			return ret;
		}
		else{
			Addr tmp = new Temp();
			emit(new Binop(tmp, now, 2, new Const(Constants.pointerSize)));
			if(x == Left){
				Addr t = new Temp();
				emit(new Binop(t, l, 0, tmp));
				if(ty instanceof RECORD){
					Addr tmp2 = new Temp();
					emit(new Load(tmp2, t, new Const(0)));
					moveRecord(ty, tmp2, Right);
				}
				else
					emit(new Store(t, new Const(0), Right));
				emit(new Move(ret, Right));
			}
			else{
				Addr tmp2 = new Temp();
				emit(new Binop(tmp2, l, 0, tmp));
				emit(new Load(ret, tmp2, new Const(0)));
			}
			return ret;
		}
	}
	public Addr tranDot_expression(Dot_expression x){
		Type ty = checkPostfix_expression(x._x);
		Addr base = tranPostfix_expression(x._x);
		Addr ret = new Temp();
		for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
			if(((RECORD)ty).fields.get(i).name == x._sym){
				if(i == 0)ret=new Const(0);
				ty = ((RECORD)ty).fields.get(i).type;
				break;
			}
			if(i == 0)emit(new Move(ret, getSize(((RECORD)ty).fields.get(i).type)));
			else emit(new Binop(ret, ret, 0, getSize(((RECORD)ty).fields.get(i).type)));
		}
		Addr ret2 = new Temp();
		emit(new Binop(ret2, base, 0, ret));
		Addr ret3 = new Temp();
		if(x == Left){
			if(ty instanceof RECORD){
				Addr tmp = new Temp();
				emit(new Load(tmp, ret2, new Const(0)));
				moveRecord(ty, tmp, Right);
			}
			else
				emit(new Store(ret2, new Const(0), Right));
			emit(new Move(ret3, Right));
		}
		else
			emit(new Load(ret3, ret2, new Const(0)));
		return ret3;
	}
	public Addr tranPointer_expression(Pointer_expression x){
		Type ty = checkPostfix_expression(x);
		Addr bas = tranPostfix_expression(x._x);
		Addr base = new Temp();
		emit(new Unary(base, 2, bas));
		Addr ret = new Temp();
		for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
			if(((RECORD)ty).fields.get(i).name == x._y){
				if(i == 0)ret=new Const(0);
				ty = ((RECORD)ty).fields.get(i).type;
				break;
			}
			if(i == 0)emit(new Move(ret, getSize(((RECORD)ty).fields.get(i).type)));
			else emit(new Binop(ret, ret, 0, getSize(((RECORD)ty).fields.get(i).type)));
		}
		Addr ret2 = new Temp();
		emit(new Binop(ret2, base, 0, ret));
		Addr ret3 = new Temp();
		if(x == Left){
			if(ty instanceof RECORD){
				Addr tmp = new Temp();
				emit(new Load(tmp, ret2, new Const(0)));
				moveRecord(ty, tmp, Right);
			}
			else
				emit(new Store(ret2, new Const(0), Right));
			emit(new Move(ret3, Right));
		}
		else
			emit(new Load(ret3, ret2, new Const(0)));
		return ret3;
	}
	public Addr tranPrimary_expression(Primary_expression x){
		if(x instanceof CharLiteral)
			return new Const(((CharLiteral)x)._x);
		if(x instanceof IntLiteral)
			return new Const(((IntLiteral)x)._x);
		if(x instanceof Id){
			Addr ret = (Addr)((VarEntry)env.vEnv.get(((Id)x)._sym)).p;
			Type ty = ((VarEntry)env.vEnv.get(((Id)x)._sym)).ty;
			if(x == Left)
				if(ty instanceof RECORD)
					moveRecord(ty, ret, Right);
				else
					emit(new Move(ret, Right));
			return ret;
		}
		if(x instanceof Expression)
			return tranExpression((Expression)x);
		if(x instanceof StringLiteral){
			Label ret = new Label();
			emit(new DataFrag(ret, ((StringLiteral)x)._s));
			return ret;
		}
		return null;
	}
	public void moveRecord(Type ty, Addr t1, Addr t){
		if(ty instanceof UNION){
			Addr temp = new Temp();
			emit(new Load(temp, t, new Const(0)));
			emit(new Store(t1, new Const(0), temp));
		}
		else{
			Addr temp = new Temp();
			Addr r = new Temp();
			emit(new Move(r, t));
			Addr r1 = new Temp();
			emit(new Move(r1, t1));
			for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
				emit(new Load(temp, r, new Const(0)));
				emit(new Store(r1, new Const(0), temp));
				
				Addr rr =  getSize(((RECORD)ty).fields.get(i).type);
				if(i != ((RECORD)ty).fields.size()-1){
					emit(new Binop(r, r, 0, rr));
					emit(new Binop(r1, r1, 0, rr));
				}
			}
		}
	}
}