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
	public int maxArg = 0;
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
	
	private LinkedList<DataFrag> dataFrags = new LinkedList<DataFrag>();
	
	private void emit(DataFrag dataFrag) {
		dataFrags.add(dataFrag);
	}
	
	public LinkedList<DataFrag> getDataFrags() {
		return dataFrags;
	}

	private void emit(LinkedList<DataFrag> dataFrags) {
		this.dataFrags.addAll(dataFrags);
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
	Temp getSize(Type x){
		if(x instanceof ARRAY)
			return (Temp)((ARRAY)x).capacity;
		if(x instanceof POINTER){
			Temp ret = Constants.now.newLocal();
			emit(new Move(ret, new Const(Constants.pointerSize)));
			return ret;
		}
		if(x instanceof CHAR){
			Temp ret = Constants.now.newLocal();
			emit(new Move(ret, new Const(Constants.charSize)));
			return ret;
		}
		if(x instanceof INT){
			Temp ret = Constants.now.newLocal();
			emit(new Move(ret, new Const(Constants.intSize)));
			return ret;
		}
		if(x instanceof RECORD){
			Temp ret = Constants.now.newLocal();
			for(int i = 0; i < ((RECORD)x).fields.size(); ++i){
				Addr tmp = getSize(((RECORD)x).fields.get(i).type);
				if(i != 0)emit(new Binop(ret, ret, 0, tmp));
				else emit(new Move(ret, tmp));
			}
			return ret;
		}
		return null;
	}
	Temp getSize(Type_name x){
		return getSize(x._ty.toType(env));
	}
	Temp getSize(Unary_expression x){
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
			Translate son = new Translate(env);
			son.emit(new LABEL(f));
			env.beginScope();
			Level bak = Constants.now;
			Label ff = Label.forFunctionExit(t._sym);
			Constants.exit = ff;
			Constants.now = new Level(bak, t._sym.toString(), f);
			LinkedList<Temp> params = new LinkedList<Temp>();
			for(int i = 0; i < formals.fields.size(); ++i){
				Temp tmp = Constants.now.newLocal();
				env.vEnv.put(formals.fields.get(i).name,new VarEntry(formals.fields.get(i).type, tmp));
				params.add(tmp);
			}
			if(maxArg < params.size())
				maxArg = params.size();
			son.emit(new Enter(f, params));
			son.tranCompound_statement(x._st);
			env.endScope();
			son.emit(new LABEL(ff));
			son.emit(new Leave(f));
			emit(son.getDataFrags());
			emit(new CompilationUnit(son.ans));
			Constants.now = bak;
		}
	}
	public void tranDeclarators(Type l, Declarators x){
		for(int i = 0; x != null && i < x._l.size(); ++i)
			tranDeclarator(l, x._l.get(i), true, false);
	}
	public Temp tranDeclarator(Type l, Declarator x, boolean isTypedef, boolean needEmpty){
		Plain_declarator t = x._x;
		while(t._link != null){
			l = new POINTER(l);
			t = t._link;
		}
		if(x._isFunc)
			env.vEnv.put(t._sym, new FunEntry(tranParameters(x._arg),l,x._arg._ellipsis));
		else {
			Constant_expressions s = x._cexp;
			if(s != null){
				Addr ret = new Const(Constants.pointerSize);
				for(int i = s._l.size() - 1; i >= 0 ; --i){
					Addr tmp = tranConstant_expression(s._l.get(i));
					Addr tt = Constants.now.newLocal();
					emit(new Binop(tt, tmp, 2, ret));
					ret = tt;
					l = new ARRAY(l, ret, tmp);
				}
			}
			if(isTypedef)
				env.tEnv.put(t._sym, l);
			else{
				Temp re = null;
				if(l instanceof ARRAY)
					re = tranArray(l);
				else if(l instanceof RECORD)
					re = tranRecord((RECORD)l);
				else{
					re = Constants.now.newLocal();
					//if(needEmpty && Constants.now.label.toString() == Constants.top_level)emit(new Move(re, new Const(0)));
				}
				env.vEnv.put(t._sym, new VarEntry(l, re));
				return re;
			}
		}
		return null;
	}
	Temp tranArray(Type ty){
		if(((ARRAY)ty).elementType instanceof ARRAY){
			Label begin = new Label();
			Label next = new Label();
			Temp i = Constants.now.newLocal();
			Temp cmp = Constants.now.newLocal();
			Temp limit = Constants.now.newLocal();
			
			emit(new Binop(limit, (Addr)((ARRAY)ty).other, 2, new Const(Constants.pointerSize)));
			Temp tmp = Constants.now.newLocal();
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
				Temp ret = Constants.now.newLocal();
				Addr tmp = Constants.now.newLocal();
				Label begin = new Label();
				Label next = new Label();
				Temp i = Constants.now.newLocal();
				Temp cmp = Constants.now.newLocal();
				Temp limit = Constants.now.newLocal();
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
				Temp ret = Constants.now.newLocal();
				if(tmp instanceof Temp)
					emit(new Malloc(ret, tmp));
				else{
					Addr tmp2 = Constants.now.newLocal();
					emit(new Move(tmp2, tmp));
					emit(new Malloc(ret, tmp2));
				}
				return ret;
			}
		}
	}
	Temp tranRecord(RECORD ty){
		Temp ret = Constants.now.newLocal();
		Addr tmp = Constants.now.newLocal();
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
				Addr tmp2 = Constants.now.newLocal();
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
		if(x._y == null)tranDeclarator(l, x._x, false, true);
		else{
			Addr t = tranDeclarator(l, x._x, false, false);
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
				Addr t2 = Constants.now.newLocal();
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
		Constant_expressions s = y._cexp;
		Addr ret = null;
		if(s != null)
			for(int i = s._l.size() - 1; i >= 0 ; --i){
				Addr tmp = tranConstant_expression(s._l.get(i));
				if(ret == null)ret = tmp;
				else{
					Addr tt = Constants.now.newLocal();
					emit(new Binop(tt, ret, 2, tmp));
					ret = tt;
				}
				ty = new ARRAY(ty, ret, tmp);
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
			Addr tmp2 = Constants.now.newLocal();
			emit(new Binop(tmp2, tmp, 8, new Const(1)));
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
			Addr tmp2 = Constants.now.newLocal();
			emit(new Binop(tmp2, tmp, 8, new Const(1)));
			emit(new IfFalse(tmp2, next));
			emit(new LABEL(begin));
			pushLabel(breakLabels, next);
			pushLabel(continueLabels, check);
			tranStatement(((For_statement)x)._st);
			popLabel(breakLabels);
			popLabel(continueLabels);
			tranExpression(((For_statement)x)._exp3);
			tmp = tranExpression(((For_statement)x)._exp2);
			emit(new IfFalse(tmp, next));
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
				Addr tmp2 = Constants.now.newLocal();
				emit(new Move(tmp2, tmp));
				emit(new Return(tmp2));
			}
		}
	}
	public Temp tranExpression(Expression x){
		if(x == null || x._l == null)return null;
		Temp ret = tranAssignment_expression(x._l.get(0));
		for(int i = 1; x._l != null && i < x._l.size(); ++i){
			ret = tranAssignment_expression(x._l.get(i));
		}
		return ret;
	}
	private Unary_expression Left;
	private Addr Right;
	private int Assign_op;
	public Temp tranAssignment_expression(Assignment_expression x){
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
			Temp t1 = tranUnary_expression(x._uexp);
			Left = null;
			return t1;
		}
	}
	public Temp tranConstant_expression(Constant_expression x){
		return tranLogical_or_expression((Logical_or_expression) x);
	}
	public Temp tranLogical_or_expression(Logical_or_expression x){
		if(x._link == null)
			return tranLogical_and_expression(x._x);
		else{
			Label next = new Label();
			Label otherwise = new Label();
			
			Temp ret = Constants.now.newLocal();
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
	public Temp tranLogical_and_expression(Logical_and_expression x){
		if(x._link == null)
			return tranInclusive_or_expression(x._x);
		else{
			Temp ret = Constants.now.newLocal();
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
	public Temp tranInclusive_or_expression(Inclusive_or_expression x){
		if(x._link == null)
			return tranExclusive_or_expression(x._x);
		else{
			Addr t1 = tranInclusive_or_expression(x._link);
			Addr t2 = tranExclusive_or_expression(x._x);
			Temp ret = Constants.now.newLocal();
			emit(new Binop(ret, t1, 5, t2));
			return ret;
		}
	}
	public Temp tranExclusive_or_expression(Exclusive_or_expression x){
		if(x._link == null)
			return tranAnd_expression(x._x);
		else{
			Addr t1 = tranExclusive_or_expression(x._link);
			Addr t2 = tranAnd_expression(x._x);
			Temp ret = Constants.now.newLocal();
			emit(new Binop(ret, t1, 6, t2));
			return ret;
		}
	}
	public Temp tranAnd_expression(And_expression x){
		if(x._link == null)
			return tranEquality_expression(x._x);
		else{
			Addr t1 = tranAnd_expression(x._link);
			Addr t2 = tranEquality_expression(x._x);
			Temp ret = Constants.now.newLocal();
			emit(new Binop(ret, t1, 7, t2));
			return ret;
		}
	}
	public Temp tranEquality_expression(Equality_expression x){
		if(x._link == null)
			return tranRelational_expression(x._x);
		else{
			Addr t1 = tranEquality_expression(x._link);
			Addr t2 = tranRelational_expression(x._x);
			Temp ret = Constants.now.newLocal();
			switch(x._eop){
				case EQ: emit(new Binop(ret, t1, 8, t2));break;
				case NE: emit(new Binop(ret, t1, 9, t2));break;
			}
			return ret;
		}
	}
	public Temp tranRelational_expression(Relational_expression x){
		if(x._link == null)
			return tranShift_expression(x._x);
		else{
			Addr t1 = tranRelational_expression(x._link);
			Addr t2 = tranShift_expression(x._x);
			Temp ret = Constants.now.newLocal();
			switch(x._rop){
				case LT: emit(new Binop(ret, t1, 10, t2));break;
				case LE: emit(new Binop(ret, t1, 11, t2));break;
				case GT: emit(new Binop(ret, t1, 12, t2));break;
				case GE: emit(new Binop(ret, t1, 13, t2));break;
			}
			return ret;
		}
	}
	public Temp tranShift_expression(Shift_expression x){
		if(x._link == null)
			return tranAdditive_expression(x._x);
		else{
			Addr t1 = tranShift_expression(x._link);
			Addr t2 = tranAdditive_expression(x._x);
			Temp ret = Constants.now.newLocal();
			switch(x._sop){
				case SHL: emit(new Binop(ret, t1, 16, t2));break;
				case SHR: emit(new Binop(ret, t1, 17, t2));break;
			}
			return ret;
		}
	}
	public Temp tranAdditive_expression(Additive_expression x){
		if(x._link == null)
			return tranMultiplicative_expression(x._x);
		else{
			Addr t1 = tranAdditive_expression(x._link);
			Addr t2 = tranMultiplicative_expression(x._x);
			Temp ret = Constants.now.newLocal();
			switch(x._aop){
				case PLUS: emit(new Binop(ret, t1, 0, t2));break;
				case MINUS: emit(new Binop(ret, t1, 1, t2));break;
			}
			return ret;
		}
	}
	public Temp tranMultiplicative_expression(Multiplicative_expression x){
		if(x._link == null)
			return tranCast_expression(x._x);
		else{
			Addr t1 = tranMultiplicative_expression(x._link);
			Addr t2 = tranCast_expression(x._x);
			Temp ret = Constants.now.newLocal();
			switch(x._mop){
				case MULTIPLY: emit(new Binop(ret, t1, 2, t2));break;
				case DIVIDE: emit(new Binop(ret, t1, 3, t2));break;
				case MOD: emit(new Binop(ret, t1, 4, t2));break;
			}
			return ret;
		}
	}
	public Temp tranCast_expression(Cast_expression x){
		if(x._x != null)
			return tranUnary_expression(x._x);
		else{
			Temp t = Constants.now.newLocal();
			Addr t1 = tranCast_expression(x._link);
			emit(new Unary(t, x._ty, t1));
			return t;
		}
	}
	public Temp tranUnary_expression(Unary_expression x){
		if(x instanceof Postfix_expression)
			return tranPostfix_expression((Postfix_expression)x);
		if(x instanceof Type_size)
			return getSize(((Type_size)x)._ty);
		if(x instanceof Unary_cast_expression){
			Temp t = tranCast_expression(((Unary_cast_expression)x)._x);
			Type ty = checkCast_expression(((Unary_cast_expression)x)._x);
			Temp t1 = Constants.now.newLocal();
			switch(((Unary_cast_expression)x)._uop){
				case BITAND: emit(new Unary(t1, 1, t));break;
				case ASTER: {
					if(x == Left){
						if(Assign_op==-1){
							if(ty instanceof RECORD){
								Addr tmp = Constants.now.newLocal();
								emit(new Load(tmp, t, new Const(0)));
								moveRecord(ty, t, tmp);
							}
							else
								emit(new Store(t, new Const(0), Right));
							emit(new Move(t1, Right));
						}
						else{
							Addr tmp = Constants.now.newLocal();
							emit(new Load(tmp, t, new Const(0)));
							emit(new Binop(tmp, tmp, Assign_op, Right));
							emit(new Store(t, new Const(0), tmp));
							emit(new Move(t1, tmp));
						}
					}
					else
						emit(new Load(t1, t, new Const(0)));
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
			Temp t = tranUnary_expression(((Unary_expressions)x)._link);
			switch(((Unary_expressions)x)._uop){
				case SIZEOF: return getSize(((Unary_expressions)x)._link);
				case INC: emit(new Unary(t, 5, t));break;
				case DEC: emit(new Unary(t, 6, t));break;
			}
			return t;
		}
		return null;
	}
	public Temp tranPostfix_expression(Postfix_expression x){
		if(x instanceof Array_expression)
			return tranArray_expression((Array_expression)x);
		if(x instanceof Dec_expression){
			Temp t1 = tranPostfix_expression(((Dec_expression)x)._x);
			emit(new Unary(t1, 6, t1));
			return t1;
		}
		if(x instanceof Inc_expression){
			Temp t1 = tranPostfix_expression(((Inc_expression)x)._x);
			emit(new Unary(t1, 5, t1));
			return t1;
		}
		if(x instanceof Function_expression)
			return tranFunction_expression((Function_expression)x);
		if(x instanceof Dot_expression)
			return tranDot_expression((Dot_expression)x);
		if(x instanceof Pointer_expression)
			return tranPointer_expression((Pointer_expression)x);
		return tranPrimary_expression((Primary_expression)x);
	}
	public Temp tranFunction_expression(Function_expression x){
		Symbol name = ((Id)x._x)._sym;
		Entry f = (Entry) env.vEnv.get(((Id)x._x)._sym);
		Arguments y = x._y;
		int cnt = (y != null && y._l != null)?y._l.size():0;
		LinkedList<Temp> params = new LinkedList<Temp>();
		for(int i = 0; i < cnt; ++i){
			Addr tmp = tranAssignment_expression(y._l.get(i));
			if(!(tmp instanceof Temp)){
				Temp tm = Constants.now.newLocal();
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
			Temp ret = Constants.now.newLocal();
			emit(new Call(ret,Label.forFunction(name), params));
			return ret;
		}
	}
	public Temp tranArray_expression(Array_expression x){
		Addr now = tranExpression(x._y);
		Addr l = tranPostfix_expression(x._x);
		Temp ret = Constants.now.newLocal();
		Type ty = checkArray_expression(x);
		if(now instanceof Const){
			if(x == Left){
				if(ty instanceof RECORD){
					Addr tmp = Constants.now.newLocal();
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
			Addr tmp = Constants.now.newLocal();
			emit(new Binop(tmp, now, 2, new Const(Constants.pointerSize)));
			if(x == Left){
				Addr t = Constants.now.newLocal();
				emit(new Binop(t, l, 0, tmp));
				if(ty instanceof RECORD){
					Addr tmp2 = Constants.now.newLocal();
					emit(new Load(tmp2, t, new Const(0)));
					moveRecord(ty, tmp2, Right);
				}
				else
					emit(new Store(t, new Const(0), Right));
				emit(new Move(ret, Right));
			}
			else{
				Addr tmp2 = Constants.now.newLocal();
				emit(new Binop(tmp2, l, 0, tmp));
				emit(new Load(ret, tmp2, new Const(0)));
			}
			return ret;
		}
	}
	public Temp tranDot_expression(Dot_expression x){
		Type ty = checkPostfix_expression(x._x);
		Addr base = tranPostfix_expression(x._x);
		Addr ret = Constants.now.newLocal();
		for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
			if(((RECORD)ty).fields.get(i).name == x._sym){
				if(i == 0)ret=new Const(0);
				ty = ((RECORD)ty).fields.get(i).type;
				break;
			}
			if(i == 0)emit(new Move(ret, getSize(((RECORD)ty).fields.get(i).type)));
			else emit(new Binop(ret, ret, 0, getSize(((RECORD)ty).fields.get(i).type)));
		}
		Addr ret2 = Constants.now.newLocal();
		emit(new Binop(ret2, base, 0, ret));
		Temp ret3 = Constants.now.newLocal();
		if(x == Left){
			if(ty instanceof RECORD){
				Addr tmp = Constants.now.newLocal();
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
	public Temp tranPointer_expression(Pointer_expression x){
		Type ty = checkPostfix_expression(x);
		Addr bas = tranPostfix_expression(x._x);
		Addr base = Constants.now.newLocal();
		emit(new Unary(base, 2, bas));
		Addr ret = Constants.now.newLocal();
		for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
			if(((RECORD)ty).fields.get(i).name == x._y){
				if(i == 0)ret=new Const(0);
				ty = ((RECORD)ty).fields.get(i).type;
				break;
			}
			if(i == 0)emit(new Move(ret, getSize(((RECORD)ty).fields.get(i).type)));
			else emit(new Binop(ret, ret, 0, getSize(((RECORD)ty).fields.get(i).type)));
		}
		Addr ret2 = Constants.now.newLocal();
		emit(new Binop(ret2, base, 0, ret));
		Temp ret3 = Constants.now.newLocal();
		if(x == Left){
			if(ty instanceof RECORD){
				Addr tmp = Constants.now.newLocal();
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
	public Temp tranPrimary_expression(Primary_expression x){
		if(x instanceof CharLiteral){
			Temp ret = Constants.now.newLocal();
			emit(new Move(ret, new Const(((CharLiteral)x)._x)));
			return ret;
		}
		if(x instanceof IntLiteral){
			Temp ret = Constants.now.newLocal();
			emit(new Move(ret, new Const(((IntLiteral)x)._x)));
			return ret;
		}
		if(x instanceof Id){
			Temp ret = (Temp)((VarEntry)env.vEnv.get(((Id)x)._sym)).p;
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
			Temp t = Constants.now.newLocal();
			emit(new Move(t, ret));
			return t;
		}
		return null;
	}
	public void moveRecord(Type ty, Addr t1, Addr t){
		if(ty instanceof UNION){
			Addr temp = Constants.now.newLocal();
			emit(new Load(temp, t, new Const(0)));
			emit(new Store(t1, new Const(0), temp));
		}
		else{
			Addr temp = Constants.now.newLocal();
			Addr r = Constants.now.newLocal();
			emit(new Move(r, t));
			Addr r1 = Constants.now.newLocal();
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
	private int calculateBinop(int op, int a, int b) {
		int[] results = {
				a + b, a - b, a * b, (b == 0 ? 0 : a / b),
				(b == 0 ? 0 : a % b), (a | b), (a ^ b), (a & b),
				(a == b ? 1 : 0), (a != b ? 1 : 0),
				(a < b ? 1 : 0), (a <= b ? 1 : 0),
				(a > b ? 1 : 0), (a >= b ? 1 : 0),
				(a!=0?true:false || b!=0?true:false)?1:0, (a!=0?true:false && b!=0?true:false)?1:0, (a << b), (a >> b)
		};
		return results[op];
	}
	public Addr makeBinop(Temp t, Addr x, Addr y, int op){
		if(x instanceof Const){
			Addr tmp = x;
			x = y;
			y = tmp;
		}
		if(x instanceof Const && t == null)
			return new Const(calculateBinop(op, ((Const)x).value, ((Const)y).value));
		else{
			if(t == null)t = Constants.now.newLocal();
			emit(new Binop(t, x, op, y));
			return t;
		}
	}
}