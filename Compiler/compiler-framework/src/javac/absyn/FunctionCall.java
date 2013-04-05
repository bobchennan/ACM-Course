package javac.absyn;

import java.util.List;

import javac.util.Position;

public class FunctionCall extends Expr {
	
	public Expr expr, args;
	
	public List<Expr> params;

	public FunctionCall(Position pos, Expr expr, Expr args) {
		super(pos);
		this.expr = expr;
		this.args = args;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		expr.accept(visitor);
		args.accept(visitor);
		visitor.visit(this);
	}
}
