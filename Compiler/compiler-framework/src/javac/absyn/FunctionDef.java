package javac.absyn;

import javac.util.Position;

public class FunctionDef extends ExternalDecl {
	
	public FunctionHead head;
	public VariableDeclList vardec;
	public StmtList stmts;

	public FunctionDef(Position pos, FunctionHead head, VariableDeclList varDec, StmtList stmtList) {
		super(pos);
		this.head = head;
		this.vardec = varDec;
		this.stmts = stmtList;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		head.accept(visitor);
		vardec.accept(visitor);
		stmts.accept(visitor);
		visitor.visit(this);
	}
}
