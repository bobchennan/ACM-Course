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
	Addr getSize(Type x){
		if(x instanceof ARRAY)
			return (Addr)((ARRAY)x).capacity;
		if(x instanceof POINTER){
			return new Const(Constants.pointerSize);
		}
		if(x instanceof CHAR){
			return new Const(Constants.charSize);
		}
		if(x instanceof INT){
			return new Const(Constants.intSize);
		}
		if(x instanceof RECORD){
			Addr ret = null;
			for(int i = 0; i < ((RECORD)x).fields.size(); ++i){
				Addr tmp = getSize(((RECORD)x).fields.get(i).type);
				if(i != 0)ret = makeBinop(ret, ret, tmp, 0);
				else ret = tmp;
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
	public Addr tranDeclarator(Type l, Declarator x, boolean isTypedef, boolean needEmpty){
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
					Addr tt = null;
					tt = makeBinop(tt, tmp, ret, 2);
					ret = tt;
					l = new ARRAY(l, ret, tmp);
				}
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
					re = Constants.now.newLocal();
					if(needEmpty && Constants.now.label.toString() == Constants.top_level)emit(new Move(re, new Const(0)));
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
			Temp i = Constants.now.newLocal();
			Temp cmp = Constants.now.newLocal();
			Addr limit = null;
			
			limit = makeBinop(limit, (Addr)((ARRAY)ty).other, new Const(Constants.pointerSize), 2);
			Temp tmp = Constants.now.newLocal();
			emit(new Malloc(tmp, limit));
			emit(new Move(i, tmp));
			limit = makeBinop(limit, limit, tmp, 0);
			emit(new LABEL(begin));
			cmp = (Temp)makeBinop(cmp, i, limit, 10);
			emit(new IfFalse(cmp, next));
			Addr tmp2 = tranArray(((ARRAY)ty).elementType);
			if(tmp2 instanceof Const){
				Addr bak = tmp2;
				tmp2 = Constants.now.newLocal();
				emit(new Move(tmp2, bak));
			}
			makeStore(i, new Const(0), tmp2);
			i = (Temp)makeBinop(i, i, new Const(Constants.pointerSize), 0);
			emit(new Goto(begin));
			emit(new LABEL(next));
			return tmp;
		}
		else{
			if(((ARRAY)ty).elementType instanceof RECORD){
				Temp ret = Constants.now.newLocal();
				Addr tmp = null;
				Label begin = new Label();
				Label next = new Label();
				Temp i = Constants.now.newLocal();
				Temp cmp = Constants.now.newLocal();
				Addr limit = null;
				tmp = makeBinop(tmp, new Const(Constants.pointerSize), (Addr)((ARRAY)ty).other, 2);
				emit(new Malloc(ret, tmp));
				limit = makeBinop(limit, ret, tmp, 0);
				emit(new Move(i, tmp));
				emit(new LABEL(begin));
				cmp = (Temp)makeBinop(cmp, i, limit, 10);
				emit(new IfFalse(cmp, next));
				Addr tmp2 = tranRecord((RECORD)((ARRAY)ty).elementType);
				if(tmp2 instanceof Const){
					Addr bak = tmp2;
					tmp2 = Constants.now.newLocal();
					emit(new Move(tmp2, bak));
				}
				makeStore(i, new Const(0), tmp2);
				i = (Temp)makeBinop(i, i, new Const(Constants.pointerSize), 0);
				emit(new Goto(begin));
				emit(new LABEL(next));
				return ret;
			}
			else{
				Addr tmp = getSize(ty);
				Temp ret = Constants.now.newLocal();
				emit(new Malloc(ret, tmp));
				return ret;
			}
		}
	}
	Temp tranRecord(RECORD ty){
		Temp ret = Constants.now.newLocal();
		Addr tmp = new Const (0);
		for(int i = 0; i < ty.fields.size(); ++i)
			if(ty.fields.get(i).type instanceof ARRAY || ty.fields.get(i).type instanceof RECORD)
				tmp = makeBinop(tmp, tmp, new Const(Constants.pointerSize), 0);
			else
				tmp = makeBinop(tmp, tmp, getSize(ty.fields.get(i).type), 0);
		emit(new Malloc(ret, tmp));
		Addr bak = tmp;
		tmp = null;
		for(int i = 0; i < ty.fields.size(); ++i)
			if(ty.fields.get(i).type instanceof ARRAY || ty.fields.get(i).type instanceof RECORD){
				Addr tmp2 = null;
				tmp2 = makeBinop(tmp2, ret, tmp, 0);
				if(ty.fields.get(i).type instanceof ARRAY)
					makeStore(tmp2, new Const(0), tranArray(ty.fields.get(i).type));
				else
					makeStore(tmp2, new Const(0), tranRecord((RECORD)ty.fields.get(i).type));
				makeBinop(tmp, tmp, new Const(Constants.pointerSize), 0, bak);
			}
			else
				tmp = makeBinop(tmp, tmp, getSize(ty.fields.get(i).type), 0);
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
			else
				makeStore(t1, new Const(i), tranAssignment_expression(y._y._l.get(i)._x));
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
					Addr tt = null;
					tt = makeBinop(tt, ret, tmp, 2);
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
			Addr tmp2 = null;
			tmp2 = makeBinop(tmp2, tmp, new Const(0), 9);
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
			Addr tmp2 = null;
			tmp2 = makeBinop(tmp2, tmp, new Const(0), 9);
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
			emit(new Return(tmp));
			
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
	public Addr tranLogical_and_expression(Logical_and_expression x){
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
	public Addr tranInclusive_or_expression(Inclusive_or_expression x){
		if(x._link == null)
			return tranExclusive_or_expression(x._x);
		else{
			Addr t1 = tranInclusive_or_expression(x._link);
			Addr t2 = tranExclusive_or_expression(x._x);
			Addr ret = null;
			ret = makeBinop(ret, t1, t2, 5);
			return ret;
		}
	}
	public Addr tranExclusive_or_expression(Exclusive_or_expression x){
		if(x._link == null)
			return tranAnd_expression(x._x);
		else{
			Addr t1 = tranExclusive_or_expression(x._link);
			Addr t2 = tranAnd_expression(x._x);
			Addr ret = null;
			ret = makeBinop(ret, t1, t2, 6);
			return ret;
		}
	}
	public Addr tranAnd_expression(And_expression x){
		if(x._link == null)
			return tranEquality_expression(x._x);
		else{
			Addr t1 = tranAnd_expression(x._link);
			Addr t2 = tranEquality_expression(x._x);
			Addr ret = null;
			ret = makeBinop(ret, t1, t2, 7);
			return ret;
		}
	}
	public Addr tranEquality_expression(Equality_expression x){
		if(x._link == null)
			return tranRelational_expression(x._x);
		else{
			Addr t1 = tranEquality_expression(x._link);
			Addr t2 = tranRelational_expression(x._x);
			Addr ret = null;
			switch(x._eop){
				case EQ: ret = makeBinop(ret, t1, t2, 8);break;
				case NE: ret = makeBinop(ret, t1, t2, 9);break;
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
			Addr ret = null;
			switch(x._rop){
				case LT: ret = makeBinop(ret, t1, t2, 10);break;
				case LE: ret = makeBinop(ret, t1, t2, 11);break;
				case GT: ret = makeBinop(ret, t1, t2, 12);break;
				case GE: ret = makeBinop(ret, t1, t2, 13);break;
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
			Addr ret = null;
			switch(x._sop){
				case SHL: ret = makeBinop(ret, t1, t2, 16);break;
				case SHR: ret = makeBinop(ret, t1, t2, 17);break;
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
			Addr ret = null;
			switch(x._aop){
				case PLUS: ret = makeBinop(ret, t1, t2, 0);break;
				case MINUS: ret = makeBinop(ret, t1, t2, 1);break;
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
			Addr ret = null;
			switch(x._mop){
				case MULTIPLY: ret = makeBinop(ret, t1, t2, 2);break;
				case DIVIDE: ret = makeBinop(ret, t1, t2, 3);break;
				case MOD: ret = makeBinop(ret, t1, t2, 4);break;
			}
			return ret;
		}
	}
	public Addr tranCast_expression(Cast_expression x){
		if(x._x != null)
			return tranUnary_expression(x._x);
		else{
			Temp t = Constants.now.newLocal();
			Addr t1 = tranCast_expression(x._link);
			emit(new Move(t, t1));
			return t;
		}
	}
	public Addr tranUnary_expression(Unary_expression x){
		if(x instanceof Postfix_expression)
			return tranPostfix_expression((Postfix_expression)x);
		if(x instanceof Type_size)
			return getSize(((Type_size)x)._ty);
		if(x instanceof Unary_cast_expression){
			Type ty = checkCast_expression(((Unary_cast_expression)x)._x);
			switch(((Unary_cast_expression)x)._uop){
				case BITAND: {
					return calcAddress(((Unary_cast_expression)x)._x);
				}
				case ASTER: {
					Addr t = tranCast_expression(((Unary_cast_expression)x)._x);
					if(x == Left){
						if(Assign_op==-1){
							if(ty instanceof RECORD){
								Addr tmp = Constants.now.newLocal();
								emit(new Load(tmp, t, new Const(0)));
								moveRecord(ty, t, tmp);
							}
							else
								makeStore(t, new Const(0), Right);
							return Right;
						}
						else{
							Addr tmp = Constants.now.newLocal();
							emit(new Load(tmp, t, new Const(0)));
							if(Assign_op != -1)emit(new Binop(tmp, tmp, Assign_op, Right));
							else emit(new Move(tmp, Right));
							emit(new Store(t, new Const(0), tmp));
							return tmp;
						}
					}
					else{
						Temp ret = Constants.now.newLocal();
						emit(new Load(ret, t, new Const(0)));
						return ret;
					}
				}
				case PLUS:{Addr t = tranCast_expression(((Unary_cast_expression)x)._x);return t;}
				case MINUS:{
					Addr t = tranCast_expression(((Unary_cast_expression)x)._x);
					Addr ret = null;
					ret = makeUnary(ret, 0, t);
					return ret;
				}
				case BITNOT:{
					Addr t = tranCast_expression(((Unary_cast_expression)x)._x);
					Addr ret = null;
					ret = makeUnary(ret, 3, t);
					return ret;
				}
				case NOT:{
					Addr t = tranCast_expression(((Unary_cast_expression)x)._x);
					Addr ret = null;
					ret = makeUnary(ret, 4, t);
					return ret;
				}
			}
		}
		if(x instanceof Unary_expressions){
			Addr t = tranUnary_expression(((Unary_expressions)x)._link);
			switch(((Unary_expressions)x)._uop){
				case SIZEOF: return getSize(((Unary_expressions)x)._link);
				case INC:{
					if(((Unary_expressions)x)._link instanceof Id){
						emit(new Unary(t, 5, t));
					}else{
						Addr tt = calcAddress(((Unary_expressions)x)._link);
						emit(new Unary(t, 5, t));
						emit(new Store(tt, new Const(0), t));
					}
					break;
				}
				case DEC:{
					if(((Unary_expressions)x)._link instanceof Id){
						emit(new Unary(t, 6, t));
					}else{
						Addr tt = calcAddress(((Unary_expressions)x)._link);
						emit(new Unary(t, 6, t));
						emit(new Store(tt, new Const(0), t));
					}
					break;
				}
			}
			return t;
		}
		return null;
	}
	public Addr tranPostfix_expression(Postfix_expression x){
		if(x instanceof Array_expression)
			return tranArray_expression((Array_expression)x);
		if(x instanceof Dec_expression){
			Addr t1 = tranPostfix_expression(((Dec_expression)x)._x);
			if(((Dec_expression)x)._x instanceof Id){
				Addr t3 = Constants.now.newLocal();
				emit(new Move(t3, t1));
				makeBinop(t1, t1, new Const(1), 1);
				return t3;
			}
			else{
				Addr t2 = calcAddress(((Dec_expression)x)._x);
				Addr t3 = Constants.now.newLocal();
				emit(new Unary(t3, 6, t1));
				emit(new Store(t2, new Const(0), t3));
				return t1;
			}
		}
		if(x instanceof Inc_expression){
			Addr t1 = tranPostfix_expression(((Inc_expression)x)._x);
			if(((Inc_expression)x)._x instanceof Id){
				Addr t3 = Constants.now.newLocal();
				emit(new Move(t3, t1));
				makeBinop(t1, t1, new Const(1), 0);
				return t3;
			}
			else{
				Addr t2 = calcAddress(((Inc_expression)x)._x);
				Addr t3 = Constants.now.newLocal();
				emit(new Unary(t3, 5, t1));
				emit(new Store(t2, new Const(0), t3));
				return t1;
			}
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
	public Addr tranArray_expression(Array_expression x){
		Addr now = tranExpression(x._y);
		Addr l = tranPostfix_expression(x._x);
		Type ty = checkArray_expression(x);
		if(now instanceof Const){
			if(x == Left){
				if(ty instanceof RECORD){
					Addr tmp = Constants.now.newLocal();
					emit(new Load(tmp, l ,((Const)now)));
					moveRecord(ty, tmp, Right);
					return Right;
				}
				else{
					if(Assign_op == -1){
						makeStore(l, ((Const)now), Right);
						return Right;
					}
					else{
						Addr tmp = Constants.now.newLocal();
						emit(new Load(tmp, l ,((Const)now)));
						Addr ret = null;
						makeBinop(ret, tmp, Right, Assign_op);
						emit(new Store(l, ((Const)now), ret));
						return ret;
					}
				}
			}
			else{
				Temp ret = Constants.now.newLocal();
				emit(new Load(ret, l, ((Const)now)));
				return ret;
			}
		}
		else{
			Addr tmp = null;
			tmp = makeBinop(tmp, now, new Const(Constants.pointerSize), 2);
			if(x == Left){
				Addr t = null;
				t = makeBinop(t, l, tmp, 0);
				if(ty instanceof RECORD){
					Addr tmp2 = Constants.now.newLocal();
					emit(new Load(tmp2, t, new Const(0)));
					moveRecord(ty, tmp2, Right);
				}
				else{
					if(Assign_op == -1)makeStore(t, new Const(0), Right);
					else{
						Addr tmp2 = Constants.now.newLocal();
						emit(new Load(tmp2, t ,new Const(0)));
						Addr ret = null;
						makeBinop(ret, tmp, Right, Assign_op);
						emit(new Store(t, new Const(0), ret));
						return ret;
					}
				}
				return Right;
			}
			else{
				Temp ret = Constants.now.newLocal();
				Addr tmp2 = null;
				tmp2 = makeBinop(tmp2, l, tmp, 0);
				emit(new Load(ret, tmp2, new Const(0)));
				return ret;
			}
		}
	}
	public Addr tranDot_expression(Dot_expression x){
		Type ty = checkPostfix_expression(x._x);
		Addr base = tranPostfix_expression(x._x);
		Addr ret = null;
		for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
			if(((RECORD)ty).fields.get(i).name == x._sym){
				if(i == 0)ret=new Const(0);
				ty = ((RECORD)ty).fields.get(i).type;
				break;
			}
			if(i == 0)ret = getSize(((RECORD)ty).fields.get(i).type);
			else ret = makeBinop(ret, ret, getSize(((RECORD)ty).fields.get(i).type), 0);
		}
		Addr ret2 = null;
		ret2 = makeBinop(ret2, base, ret, 0);
		Temp ret3 = Constants.now.newLocal();
		if(x == Left){
			if(ty instanceof RECORD){
				Addr tmp = Constants.now.newLocal();
				emit(new Load(tmp, ret2, new Const(0)));
				moveRecord(ty, tmp, Right);
			}
			else
				makeStore(ret2, new Const(0), Right);
			emit(new Move(ret3, Right));
		}
		else
			emit(new Load(ret3, ret2, new Const(0)));
		return ret3;
	}
	public Addr tranPointer_expression(Pointer_expression x){
		Type ty = checkPostfix_expression(x._x);
		ty = ((POINTER)ty).elementType;
		Addr bas = tranPostfix_expression(x._x);
		Addr base = Constants.now.newLocal();
		emit(new Unary(base, 2, bas));
		Addr ret = null;
		for(int i = 0; i < ((RECORD)ty).fields.size(); ++i){
			if(((RECORD)ty).fields.get(i).name == x._y){
				if(i == 0)ret=new Const(0);
				ty = ((RECORD)ty).fields.get(i).type;
				break;
			}
			if(i == 0)ret = getSize(((RECORD)ty).fields.get(i).type);
			else ret = makeBinop(ret, ret, getSize(((RECORD)ty).fields.get(i).type), 0);
		}
		Addr ret2 = null;
		ret2 = makeBinop(ret2, base, ret, 0);
		Temp ret3 = Constants.now.newLocal();
		if(x == Left){
			if(ty instanceof RECORD){
				Addr tmp = Constants.now.newLocal();
				emit(new Load(tmp, ret2, new Const(0)));
				moveRecord(ty, tmp, Right);
			}
			else
				makeStore(ret2, new Const(0), Right);
			emit(new Move(ret3, Right));
		}
		else
			emit(new Load(ret3, ret2, new Const(0)));
		return ret3;
	}
	public Addr tranPrimary_expression(Primary_expression x){
		if(x instanceof CharLiteral){
			return new Const(((CharLiteral)x)._x);
		}
		if(x instanceof IntLiteral){
			return new Const(((IntLiteral)x)._x);
		}
		if(x instanceof Id){
			Temp ret = (Temp)((VarEntry)env.vEnv.get(((Id)x)._sym)).p;
			Type ty = ((VarEntry)env.vEnv.get(((Id)x)._sym)).ty;
			if(x == Left)
				if(ty instanceof RECORD)
					moveRecord(ty, ret, Right);
				else{
					if(Assign_op == -1)emit(new Move(ret, Right));
					else makeBinop(ret, ret, Right, Assign_op);
				}
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
					makeBinop(r, r, rr, 0);
					makeBinop(r1, r1, rr, 0);
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
	public Addr makeBinop(Addr t, Addr x, Addr y, int op){
		if(x instanceof Const && !(y instanceof Const) && Constants.isInvertable[op]){
			Addr tmp = x;
			x = y;
			y = tmp;
		}
		if(x instanceof Const && y instanceof Const && !(t instanceof Temp))
			return new Const(calculateBinop(op, ((Const)x).value, ((Const)y).value));
		else if(x instanceof Const && y instanceof Temp && !Constants.isInvertable[op]){
			if(t instanceof Const || t == null)t = Constants.now.newLocal();
			emit(new Move(t, x));
			emit(new Binop(t, t, op, y));
			return t;
		}
		else{
			if(t instanceof Const || t == null)t = Constants.now.newLocal();
			emit(new Binop(t, x, op, y));
			return t;
		}
	}
	public Addr makeBinop(Addr t, Addr x, Addr y, int op, Addr z){
		if(x instanceof Const && !(y instanceof Const) && Constants.isInvertable[op]){
			Addr tmp = x;
			x = y;
			y = tmp;
		}
		if(x instanceof Const && y instanceof Const && !(t instanceof Temp))
			return new Const(calculateBinop(op, ((Const)x).value, ((Const)y).value));
		else if(x instanceof Const && y instanceof Temp && !Constants.isInvertable[op]){
			t = z;
			emit(new Move(t, x));
			emit(new Binop(t, t, op, y));
			return t;
		}
		else{
			t = z;
			emit(new Binop(t, x, op, y));
			return t;
		}
	}
	private void makeStore(Addr x, Const y, Addr z){
		if(z instanceof Const)
			if(((Const) z).value == 0)
				emit(new StoreZ((Temp)x, y));
			else{
				Addr ret = Constants.now.newLocal();
				emit(new Move(ret, z));
				emit(new Store(x, y, ret));
			}
		else emit(new Store(x, y, z));
	}
	private Addr makeUnary(Addr x, int op, Addr y){
		if(y instanceof Const && x == null)
			if(op == 0)return new Const(-((Const)y).value);
			else if(op == 3)return new Const(~((Const)y).value);
			else if(((Const)y).value == 0)return new Const(1);
			else return new Const(0);
		else{
			Addr ret = x;
			if(ret == null)ret = Constants.now.newLocal();
			emit(new Unary(ret, op, y));
			return ret;
		}
	}
	private Addr calcAddress(Object x){
		if(x instanceof Cast_expression)
			if(((Cast_expression) x)._x == null)
				return calcAddress(((Cast_expression) x)._link);
			else
				return calcAddress(((Cast_expression) x)._x);
		if(x instanceof Unary_cast_expression)
			if(((Unary_cast_expression) x)._uop == Unary_operator.ASTER)
				return tranCast_expression(((Unary_cast_expression) x)._x);
			else{
				return calcAddress(((Unary_cast_expression) x)._x);
			}
		if(x instanceof Array_expression){
			Addr y = tranPostfix_expression(((Array_expression)x)._x);
			Addr z = tranExpression(((Array_expression) x)._y);
			return makeBinop(null, y, makeBinop(null, z, new Const(Constants.pointerSize), 2), 0);
		}
		/* to do dot && pointer */
		if(x instanceof Id){
			VarEntry y = (VarEntry)env.vEnv.get(((Id)x)._sym);
			Temp ret = Constants.now.newLocal();
			emit(new Address(ret, Constants.pointerSize * ((Temp)y.p).index, ((Temp)y.p).allArea));
			return ret;
		}
		if(x instanceof Expression)
			return calcAddress(((Expression)x)._l.get(((Expression)x)._l.size()-1));
		if(x instanceof Assignment_expression)
			return calcAddress(((Assignment_expression) x)._lexp);
		if(x instanceof Logical_or_expression)
			return calcAddress(((Logical_or_expression)x)._x);
		if(x instanceof Logical_and_expression)
			return calcAddress(((Logical_and_expression)x)._x);
		if(x instanceof Inclusive_or_expression)
			return calcAddress(((Inclusive_or_expression)x)._x);
		if(x instanceof Exclusive_or_expression)
			return calcAddress(((Exclusive_or_expression)x)._x);
		if(x instanceof And_expression)
			return calcAddress(((And_expression)x)._x);
		if(x instanceof Equality_expression)
			return calcAddress(((Equality_expression)x)._x);
		if(x instanceof Relational_expression)
			return calcAddress(((Relational_expression)x)._x);
		if(x instanceof Shift_expression)
			return calcAddress(((Shift_expression)x)._x);
		if(x instanceof Additive_expression)
			return calcAddress(((Additive_expression)x)._x);
		if(x instanceof Multiplicative_expression)
			return calcAddress(((Multiplicative_expression)x)._x);
		return null;
	}
}