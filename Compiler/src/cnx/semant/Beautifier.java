package cnx.semant;

import cnx.ast.*;
import java.io.*;

public class Beautifier {
	private File doc;
	private FileWriter out;
	private int indent = 0;
	public Beautifier(String x)throws Exception{
		doc = new File(x);
		out = new FileWriter(doc, false);
	}
	public void Indent()throws Exception{
		for(int i = 0; i < indent; ++i)
			out.write("    ");
	}
	public void start(Program x)throws Exception{
		visitProgram(x);
		out.close();
	}
	public void visitProgram(Program x)throws Exception{
		if(x._link != null){
			visitProgram(x._link);
			out.write('\n');
		}
		if(x._v instanceof Declaration)
			visitDeclaration((Declaration)x._v);
		if(x._v instanceof Function_definition)
			visitFunction_definition((Function_definition)x._v);
	}
	public void visitDeclaration(Declaration x)throws Exception{
		Indent();
		if(x._v != null){
			out.write("typedef ");
			visitType_specifier(x._ty);
			out.write(' ');
			visitDeclarators(x._v);
		}
		else{
			visitType_specifier(x._ty);
			out.write(' ');
			visitInit_declarators(x._init);
		}
		out.write(";\n");
	}
	public void visitType_specifier(Type_specifier x)throws Exception{
		if(x instanceof CharType)
			out.write("char");
		if(x instanceof Id_type)
			out.write(((Id_type)x)._sym.toString());
		if(x instanceof IntType)
			out.write("int");
		if(x instanceof Record_type)
			visitRecord_type(((Record_type)x));
		if(x instanceof VoidType)
			out.write("void");
	}
	public void visitRecord_type(Record_type x)throws Exception{
		if(x._isSt)out.write("struct");
		else out.write("union");
		if(x._sym != null)out.write(' '+x._sym.toString()+'\n');
		Indent();
		out.write("{\n");
		++indent;
		visitStructs(x._x);
		--indent;
		Indent();
		out.write("}");
	}
	public void visitStructs(Structs x)throws Exception{
		for(int i = 0; i < x._l1.size(); ++i){
			Indent();
			visitType_specifier(x._l1.get(i));
			out.write(' ');
			visitDeclarators(x._l2.get(i));
			out.write(";\n");
		}
	}
	public void visitDeclarators(Declarators x)throws Exception{
		boolean st = false;
		for(int i = 0; x != null && i < x._l.size(); ++i){
			if(st)out.write(", ");
			st = true;
			visitDeclarator(x._l.get(i));
		}
	}
	public void visitDeclarator(Declarator x)throws Exception{
		visitPlain_declarator(x._x);
		if(x._isFunc){
			out.write('(');
			if(x._arg != null)
				visitParameters(x._arg);
			out.write(')');
		}else{
			visitConstant_expressions(x._cexp);
		}
	}
	public void visitConstant_expressions(Constant_expressions x)throws Exception{
		for(int i = 0; x != null && i < x._l.size(); ++i){
			out.write('[');
			visitLogical_or_expression((Logical_or_expression)x._l.get(i));
			out.write(']');
		}
	}
	public void visitFunction_definition(Function_definition x)throws Exception{
		Indent();
		visitType_specifier(x._ty);
		out.write(' ');
		visitPlain_declarator(x._x);
		out.write('(');
		if(x._arg != null)
			visitParameters(x._arg);
		out.write(")\n");
		++indent;
		visitStatement(x._st);
		--indent;
	}
	public void visitPlain_declarator(Plain_declarator x)throws Exception{
		while(x._link != null){
			out.write('*');
			x = x._link;
		}
		out.write(x._sym.toString());
	}
	public void visitParameters(Parameters x)throws Exception{
		visitPlain_declaration(x._x);
		for(int i = 0; x._xs != null && i < x._xs._l.size(); ++i){
			out.write(", ");
			visitPlain_declaration(x._xs._l.get(i));
		}
		if(x._ellipsis)
			out.write(", ...");
	}
	public void visitPlain_declaration(Plain_declaration x)throws Exception{
		visitType_specifier(x._ty);
		out.write(' ');
		visitDeclarator(x._x);
	}
	public void visitInit_declarators(Init_declarators x)throws Exception{
		boolean st = false;
		for(int i = 0; x != null && i < x._l.size(); ++i){
			if(st)out.write(", ");
			st = true;
			visitInit_declarator(x._l.get(i));
		}
	}
	public void visitInit_declarator(Init_declarator x)throws Exception{
		visitDeclarator(x._x);
		if(x._y != null){
			out.write(" = ");
			visitInitializer(x._y);
		}
	}
	public void visitInitializer(Initializer x)throws Exception{
		if(x._x != null)
			visitAssignment_expression(x._x);
		else{
			out.write('{');
			visitInitializers(x._y);
			out.write('}');
		}
	}
	public void visitInitializers(Initializers x)throws Exception{
		boolean st =false;
		for(int i = 0; x != null && i < x._l.size(); ++i){
			if(st)out.write(", ");
			st = true;
			visitInitializer(x._l.get(i));
		}
	}
	public void visitStatement(Statement x)throws Exception{
		if(x instanceof Expression_statement){
			Indent();
			if(((Expression_statement)x)._x != null)
				visitExpression(((Expression_statement)x)._x);
			out.write(";\n");
		}
		if(x instanceof Compound_statement){
			--indent;
			Indent();
			out.write("{\n");
			++indent;
			visitDeclarations(((Compound_statement)x)._x);
			visitStatements(((Compound_statement)x)._y);
			--indent;
			Indent();
			out.write("}\n");
			++indent;
		}
		if(x instanceof Selection_statement){
			Indent();
			out.write("if(");
			visitExpression(((Selection_statement)x)._exp);
			out.write(")\n");
			++indent;
			visitStatement(((Selection_statement)x)._st1);
			--indent;
			if(((Selection_statement)x)._st2 != null){
				Indent();
				out.write("else\n");
				++indent;
				visitStatement(((Selection_statement)x)._st2);
				--indent;
			}
		}
		if(x instanceof For_statement){
			Indent();
			out.write("for(");
			if(((For_statement)x)._exp1 != null)
				visitExpression(((For_statement)x)._exp1);
			out.write(';');
			if(((For_statement)x)._exp2 != null){
				out.write(' ');
				visitExpression(((For_statement)x)._exp2);
			}
			out.write(';');
			if(((For_statement)x)._exp3 != null){
				out.write(' ');
				visitExpression(((For_statement)x)._exp3);
			}
			out.write(")\n");
			++indent;
			visitStatement(((For_statement)x)._st);
			--indent;
		}
		if(x instanceof While_statement){
			Indent();
			out.write("while(");
			visitExpression(((While_statement)x)._exp);
			out.write(")\n");
			++indent;
			visitStatement(((While_statement)x)._st);
			--indent;
		}
		if(x instanceof Continue_statement){
			Indent();
			out.write("continue;\n");
		}
		if(x instanceof Break_statement){
			Indent();
			out.write("break;\n");
		}
		if(x instanceof Return_statement){
			Indent();
			out.write("return ");
			visitExpression(((Return_statement)x)._exp);
			out.write(";\n");
		}
	}
	public void visitDeclarations(Declarations x)throws Exception{
		for(int i = 0; x != null && i < x._l.size(); ++i)
			visitDeclaration(x._l.get(i));
	}
	public void visitStatements(Statements x)throws Exception{
		for(int i = 0;  x != null && i < x._l.size(); ++i)
			visitStatement(x._l.get(i));
	}
	public void visitExpression(Expression x)throws Exception{
		boolean st = false;
		for(int i = 0;  x != null && i < x._l.size(); ++i){
			if(st)out.write(", ");
			st = true;
			visitAssignment_expression(x._l.get(i));
		}
	}
	public void visitAssignment_expression(Assignment_expression x)throws Exception{
		if(x._lexp != null)
			visitLogical_or_expression(x._lexp);
		else{
			visitUnary_expression(x._uexp);
			out.write(' ');
			switch(x._aop)
			{
				case ASSIGN: out.write('=');break;
				case MULASS: out.write("*=");break;
				case DIVASS: out.write("/=");break;
				case MODASS: out.write("%=");break;
				case ADDASS: out.write("+=");break;
				case SUBASS: out.write("-=");break;
				case SHLASS: out.write("<<=");break;
				case SHRASS: out.write(">>=");break;
				case ANDASS: out.write("&=");break;
				case ORASS: out.write("|=");break;
				case XORASS: out.write("^=");break;
			}
			out.write(' ');
			visitAssignment_expression(x._link);
		}
	}
	public void visitLogical_or_expression(Logical_or_expression x)throws Exception{
		if(x._link == null)
			visitLogical_and_expression(x._x);
		else{
			visitLogical_or_expression(x._link);
			out.write(" || ");
			visitLogical_and_expression(x._x);
		}
	}
	public void visitLogical_and_expression(Logical_and_expression x)throws Exception{
		if(x._link == null)
			visitInclusive_or_expression(x._x);
		else{
			visitLogical_and_expression(x._link);
			out.write(" && ");
			visitInclusive_or_expression(x._x);
		}
	}
	public void visitInclusive_or_expression(Inclusive_or_expression x)throws Exception{
		if(x._link == null)
			visitExclusive_or_expression(x._x);
		else{
			visitInclusive_or_expression(x._link);
			out.write(" | ");
			visitExclusive_or_expression(x._x);
		}
	}
	public void visitExclusive_or_expression(Exclusive_or_expression x)throws Exception{
		if(x._link == null)
			visitAnd_expression(x._x);
		else{
			visitExclusive_or_expression(x._link);
			out.write(" ^ ");
			visitAnd_expression(x._x);
		}
	}
	public void visitAnd_expression(And_expression x)throws Exception{
		if(x._link == null)
			visitEquality_expression(x._x);
		else{
			visitAnd_expression(x._link);
			out.write(" & ");
			visitEquality_expression(x._x);
		}
	}
	public void visitEquality_expression(Equality_expression x)throws Exception{
		if(x._link == null)
			visitRelational_expression(x._x);
		else{
			visitEquality_expression(x._link);
			switch(x._eop){
				case EQ: out.write(" == ");break;
				case NE: out.write(" != ");break;
			}
			visitRelational_expression(x._x);
		}
	}
	public void visitRelational_expression(Relational_expression x)throws Exception{
		if(x._link == null)
			visitShift_expression(x._x);
		else{
			visitRelational_expression(x._link);
			switch(x._rop){
				case LE: out.write(" <= ");break;
				case GE: out.write(" >= ");break;
				case LT: out.write(" < ");break;
				case GT: out.write(" > ");break;
			}
			visitShift_expression(x._x);
		}
	}
	public void visitShift_expression(Shift_expression x)throws Exception{
		if(x._link == null)
			visitAdditive_expression(x._x);
		else{
			visitShift_expression(x._link);
			switch(x._sop){
				case SHL: out.write(" << ");break;
				case SHR: out.write(" >> ");break;
			}
			visitAdditive_expression(x._x);
		}
	}
	public void visitAdditive_expression(Additive_expression x)throws Exception{
		if(x._link != null){
			visitAdditive_expression(x._link);
			switch(x._aop){
				case PLUS: out.write(" + ");break;
				case MINUS: out.write(" - ");break;
			}
		}
		visitMultiplicative_expression(x._x);
	}
	public void visitMultiplicative_expression(Multiplicative_expression x)throws Exception{
		if(x._link != null){
			visitMultiplicative_expression(x._link);
			switch(x._mop){
				case MULTIPLY: out.write(" * ");break;
				case DIVIDE: out.write(" / ");break;
				case MOD: out.write(" % ");break;
			}
		}
		visitCast_expression(x._x);
	}
	public void visitCast_expression(Cast_expression x)throws Exception{
		if(x._link != null){
			out.write('(');
			visitType_name(x._ty);
			out.write(')');
			visitCast_expression(x._link);
		}
		else
			visitUnary_expression(x._x);
	}
	public void visitType_name(Type_name x)throws Exception{
		visitType_specifier(x._ty);
		out.write(' ');
		for(int i = 0; i < x.cnt; ++i)
			out.write('*');
	}
	public void visitUnary_expression(Unary_expression x)throws Exception{
		if(x instanceof Unary_cast_expression){
			switch(((Unary_cast_expression)x)._uop){
				case BITAND: out.write("&(");break;
				case ASTER: out.write("*(");break;
				case PLUS: out.write("+(");break;
				case MINUS: out.write("-(");break;
				case NOT: out.write("~(");break;
				case BITNOT: out.write("!(");break;
			}
			visitCast_expression(((Unary_cast_expression)x)._x);
			out.write(")");
		}
		if(x instanceof Unary_expressions){
			switch(((Unary_expressions)x)._uop){
				case INC: out.write("++(");break;
				case DEC: out.write("--(");break;
				case SIZEOF: out.write("sizeof(");break;
			}
			visitUnary_expression(((Unary_expressions)x)._link);
			out.write(")");
		}
		if(x instanceof Type_size){
			out.write("sizeof(");
			visitType_name(((Type_size)x)._ty);
			out.write(")");
		}
		if(x instanceof Array_expression){
			visitUnary_expression(((Array_expression)x)._x);
			out.write('[');
			visitExpression(((Array_expression)x)._y);
			out.write(']');
		}
		if(x instanceof Dec_expression){
			out.write('(');
			visitUnary_expression(((Dec_expression)x)._x);
			out.write(")--");
		}
		if(x instanceof Inc_expression){
			out.write('(');
			visitUnary_expression(((Inc_expression)x)._x);
			out.write(")++");
		}
		if(x instanceof Dot_expression){
			out.write('(');
			visitUnary_expression(((Dot_expression)x)._x);
			out.write(")."+((Dot_expression)x)._sym.toString());
		}
		if(x instanceof Function_expression){
			visitUnary_expression(((Function_expression)x)._x);
			out.write('(');
			visitArguments(((Function_expression)x)._y);
			out.write(')');
		}
		if(x instanceof Pointer_expression){
			out.write('(');
			visitUnary_expression(((Pointer_expression)x)._x);
			out.write(")->"+((Pointer_expression)x)._y.toString());
		}
		if(x instanceof Primary_expression)
			visitPrimary_expression((Primary_expression)x);
	}
	public void visitArguments(Arguments x)throws Exception{
		boolean st = false;
		for(int i = 0;  x != null && i < x._l.size(); ++i){
			if(st)out.write(", ");
			st = true;
			visitAssignment_expression(x._l.get(i));
		}
	}
	public void visitPrimary_expression(Primary_expression x)throws Exception{
		if(x instanceof CharLiteral){
			char ch = ((CharLiteral)x)._x;
			out.write("'");
			if(ch == '\n')out.write("\\n");
			else if(ch == '\t')out.write("\\t");
			else if(ch == '\"')out.write("\\\"");
			else if(ch == '\\')out.write("\\\\");
			else out.write(ch);
			out.write("'");
		}
		if(x instanceof IntLiteral)
			out.write(String.valueOf(((IntLiteral)x)._x));
		if(x instanceof Expression)
			visitExpression((Expression)x);
		if(x instanceof Id)
			out.write(((Id)x)._sym.toString());
		if(x instanceof StringLiteral){
			out.write('"');
			for(int i = 0; i < ((StringLiteral)x)._s.length(); ++i){
				char ch = ((StringLiteral)x)._s.charAt(i);
				if(ch == '\n')out.write("\\n");
				else if(ch == '\t')out.write("\\t");
				else if(ch == '\"')out.write("\\\"");
				else if(ch == '\\')out.write("\\\\");
				else out.write(ch);
			}
			out.write('"');
		}
	}
}
