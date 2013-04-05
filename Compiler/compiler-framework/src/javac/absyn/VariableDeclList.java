package javac.absyn;

import java.util.LinkedList;
import java.util.List;

import javac.util.Position;

public class VariableDeclList extends Node {
	
	public List<VariableDecl> variableDeclarations = new LinkedList<VariableDecl>();

	public VariableDeclList(Position pos) {
		super(pos);
	}
	
	public void add(VariableDecl decl) {
		variableDeclarations.add(decl);
	}

	@Override
	public void accept(NodeVisitor visitor) {
		for (VariableDecl decl : variableDeclarations) {
			decl.accept(visitor);
		}
		visitor.visit(this);
	}
}
