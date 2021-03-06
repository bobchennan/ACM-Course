package cnx.temp;

import cnx.symbol.Symbol;

public class Label implements Addr{
	public static int count = 0;
	private String name = "";

	@Override
	public Label clone(){
		return this;
	}
	
	public String toString() {
		return name;
	}

	public Label(String n) {
		name = n;
	}

	public Label() {
		this("L" + count++);
	}
	
	public static Label forFunction(Symbol name) {
		Label l = new Label();
		l.name = name.toString();
		return l;
	}
	
	public static Label forFunctionExit(Symbol name){
		Label l = new Label();
		l.name = "__" + name;
		return l;
	}
	
	public static Label forFunctionExit(String name){
		Label l = new Label();
		l.name = "__" + name;
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
