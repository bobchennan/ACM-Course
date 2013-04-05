package javac.absyn;

import javac.symbol.Symbol;
import javac.util.Position;

public class RecordDef extends ExternalDecl {
	
	public Symbol name;
	public VariableDeclList fields;

	public RecordDef(Position pos, Symbol recordName, VariableDeclList fieldList) {
		super(pos);
		name = recordName;
		fields = fieldList;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		fields.accept(visitor);
		visitor.visit(this);
	}
}
