package javac.absyn;

import javac.symbol.Symbol;
import javac.util.Position;

public class FunctionHead extends Node {
	
	public TypeSpecifier type;
	public Symbol functionName;
	public ParameterList parameterList;

	public FunctionHead(Position pos, TypeSpecifier type, Symbol functionName, ParameterList parameterList) {
		super(pos);
		this.type = type;
		this.functionName = functionName;
		this.parameterList = parameterList;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		type.accept(visitor);
		parameterList.accept(visitor);
		visitor.visit(this);
	}
}
