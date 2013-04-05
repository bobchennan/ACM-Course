package javac.absyn;

import java.util.LinkedList;
import java.util.List;

import javac.util.Position;

public class ParameterList extends Node {
	
	public List<ParameterDecl> parameterDeclarations = new LinkedList<ParameterDecl>();

	public ParameterList(Position pos) {
		super(pos);
	}
	
	public void add(ParameterDecl decl) {
		parameterDeclarations.add(decl);
	}

	@Override
	public void accept(NodeVisitor visitor) {
		for (ParameterDecl decl : parameterDeclarations) {
			decl.accept(visitor);
		}
		visitor.visit(this);
	}
}
