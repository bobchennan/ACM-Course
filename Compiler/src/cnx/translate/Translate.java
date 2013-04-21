package cnx.translate;

import java.io.*;
import java.util.*;

import cnx.ast.*;
import cnx.env.*;
import cnx.type.*;
import cnx.quad.*;
import cnx.temp.*;

public class Translate{
	private Env env = null;
	private List<Quad> ans;
	public Translate(){
		this(new Env());
	}
	public Translate(Env x){
		env = x;
		ans=new ArrayList<Quad>();
	}
	private void emit(Quad x){
		ans.add(x);
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
	void tranProgram(Program x){
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
		Label f = Label.forFunction(t._sym);
		emit(new LABEL(f));
		emit(new Enter(f));
		if(x._st != null){
			env.beginScope();
			tranCompound_statement(x._st);
			env.endScope();
		}
		emit(new Leave(f));
	}
	public void tranDeclarators(Type l, Declarators x){
		for(int i = 0; x != null && i < x._l.size(); ++i)
			tranDeclarator(l, x._l.get(i), true);
	}
	public void tranDeclarator(Type l, Declarator x, boolean isTypedef){
		Plain_declarator t = x._x;
		while(t._link != null){
			l = new POINTER(l);
			t = t._link;
		}
		if(x._isFunc)
			env.vEnv.put(t._sym, new FunEntry(tranParameters(x._arg),l,x._arg._ellipsis));
		else {
			Constant_expressions s = x._cexp;
			for(int i = 0; s != null && i < s._l.size(); ++i){
				Addr tmp = tranConstant_expression(s._l.get(i));
				l = new ARRAY(l, tmp);
			}
			if(isTypedef)
				env.tEnv.put(t._sym, l);
			else
				env.vEnv.put(t._sym, new VarEntry(l));
		}
	}
	public void tranInit_declarators(Type l, Init_declarators x){
		for(int i = 0; x != null && i < x._l.size(); ++i)
			tranInit_declarator(l, x._l.get(i));
	}
	public void tranInit_declarator(Type l, Init_declarator x){
		if(x._y == null)tranDeclarator(l, x._x, false);
		else
			Move(x._x, x._y);
	}
	public RECORD tranParameters(Parameters x){
		RECORD ret = new RECORD(null);
		if(x == null)return ret;
		RECORD.RecordField v = tranPlain_declaration(x._x);
		ret.fields.add(v);
		Plain_declarations y = x._xs;
		for(int i = 0; y != null && i < y._l.size(); ++i){
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
			emit(new IfFalse(tmp,next));
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
			emit(new IfFalse(tmp, next));
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
			emit(new IfFalse(tmp, next));
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
			emit(new Return(tmp));
		}
	}
	public Addr tranExpression(Expression x){
		Addr ret = tranAssignment_expression(x._l.get(0));
		for(int i = 1; i < x._l.size(); ++i){
			ret = tranAssignment_expression(x._l.get(i));
		}
		return ret;
	}
	public Addr tranAssignment_expression(Assignment_expression x){
		if(x._lexp != null)
			return tranLogical_or_expression(x._lexp);
		else{
			Addr t1 = tranUnary_expression(x._uexp);
			Addr t2 = tranAssignment_expression(x._link);
			switch(x._aop){
				case ASSIGN: emit(new Move(t1, t2));break;
				case MULASS: emit(new Binop(t1, t1, 2, t2));break;
				case DIVASS: emit(new Binop(t1, t1, 3, t2));break;
				case MODASS: emit(new Binop(t1, t1, 4, t2));break;
				case ADDASS: emit(new Binop(t1, t1, 0, t2));break;
				case SUBASS: emit(new Binop(t1, t1, 1, t2));break;
				case SHLASS: emit(new Binop(t1, t1, 16, t2));break;
				case SHRASS: emit(new Binop(t1, t1, 17, t2));break;
				case ANDASS: emit(new Binop(t1, t1, 7, t2));break;
				case XORASS: emit(new Binop(t1, t1, 6, t2));break;
				case ORASS: emit(new Binop(t1, t1, 5, t2));break;
			}
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
			Addr t1 = tranLogical_or_expression(x._link);
			Addr t2 = tranLogical_and_expression(x._x);
			emit(new Binop(t1, t1, 14, t2));
			return t1;
		}
	}
	public Addr tranLogical_and_expression(Logical_and_expression x){
		if(x._link == null)
			return tranInclusive_or_expression(x._x);
		else{
			Addr t1 = tranLogical_and_expression(x._link);
			Addr t2 = tranInclusive_or_expression(x._x);
			emit(new Binop(t1, t1, 15, t2));
			return t1;
		}
	}
	public Addr tranInclusive_or_expression(Inclusive_or_expression x){
		if(x._link == null)
			return tranExclusive_or_expression(x._x);
		else{
			Addr t1 = tranInclusive_or_expression(x._link);
			Addr t2 = tranExclusive_or_expression(x._x);
			emit(new Binop(t1, t1, 5, t2));
			return t1;
		}
	}
	public Addr tranExclusive_or_expression(Exclusive_or_expression x){
		if(x._link == null)
			return tranAnd_expression(x._x);
		else{
			Addr t1 = tranExclusive_or_expression(x._link);
			Addr t2 = tranAnd_expression(x._x);
			emit(new Binop(t1, t1, 6, t2));
			return t1;
		}
	}
	public Addr tranAnd_expression(And_expression x){
		if(x._link == null)
			return tranEquality_expression(x._x);
		else{
			Addr t1 = tranAnd_expression(x._link);
			Addr t2 = tranEquality_expression(x._x);
			emit(new Binop(t1, t1, 7, t2));
			return t1;
		}
	}
	public Addr tranEquality_expression(Equality_expression x){
		if(x._link == null)
			return tranRelational_expression(x._x);
		else{
			Addr t1 = tranEquality_expression(x._link);
			Addr t2 = tranRelational_expression(x._x);
			switch(x._eop){
				case EQ: emit(new Binop(t1, t1, 8, t2));break;
				case NE: emit(new Binop(t1, t1, 9, t2));break;
			}
		}
	}
}