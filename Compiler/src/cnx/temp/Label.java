package cnx.temp;

import cnx.symbol.Symbol;

public class Label implements Addr{
	public static int count = 0;
	private String name = "";

	public String toString() {
		return name;
	}

	public Label(String n) {
		name = n;
	}

	public Label() {
		this("L" + count++);
		forFunc = false;
	}
	
	public boolean forFunc = true;
	
	public static Label forFunction(Symbol name) {
		Label l = new Label();
		l.name += "_" + name;
		l.forFunc = true;
		return l;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Label) {
			return name.equals(((Label) object).name);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
