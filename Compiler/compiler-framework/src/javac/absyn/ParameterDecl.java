package javac.absyn;

import javac.symbol.Symbol;
import javac.util.Position;

public class ParameterDecl extends Node {
	
	public TypeSpecifier type;
	public Symbol name;

	public ParameterDecl(Position pos, TypeSpecifier type, Symbol name) {
		super(pos);
		this.type = type;
		this.name = name;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		type.accept(visitor);
		visitor.visit(this);
	}
}
