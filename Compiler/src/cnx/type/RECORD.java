package cnx.type;

import java.util.ArrayList;
import java.util.List;

import cnx.symbol.Symbol;

public class RECORD extends Type {

	public static final class RecordField {

		public Type type;
		public Symbol name;
		public int index;

		public RecordField(Type type, Symbol name, int index) {
			this.type = type;
			this.name = name;
			this.index = index;
		}
	}

	public List<RecordField> fields;
	public Symbol name;

	public RECORD(Symbol name) {
		fields = new ArrayList<RecordField>();
		this.name = name;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof RECORD) {
			return name.equals(((RECORD) other).name);
		}
		return false;
	}

}
