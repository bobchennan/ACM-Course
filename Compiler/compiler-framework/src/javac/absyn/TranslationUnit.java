package javac.absyn;

import java.util.LinkedList;
import java.util.List;

import javac.util.Position;

public class TranslationUnit extends Node {
	
	public List<ExternalDecl> externalDeclarations = new LinkedList<ExternalDecl>();

	public TranslationUnit(Position pos) {
		super(pos);
	}
	
	public void add(ExternalDecl decl) {
		externalDeclarations.add(decl);
	}

	@Override
	public void accept(NodeVisitor visitor) {
		for (ExternalDecl decl : externalDeclarations) {
			decl.accept(visitor);
		}
		visitor.visit(this);
	}
}
