package javac.absyn;

import javac.util.Position;

public class PrototypeDecl extends ExternalDecl {
	
	public FunctionHead head;

	public PrototypeDecl(Position pos, FunctionHead head) {
		super(pos);
		this.head = head;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		head.accept(visitor);
		visitor.visit(this);
	}
}
