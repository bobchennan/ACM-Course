package javac.symbol;

public class Symbol {

	private String name;
	
	private Symbol(String n) {
		name = n;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Symbol) {
			return name.equals(((Symbol) other).name);
		}
		return false;
	}
	
	private static java.util.Map<String, Symbol> dict = new java.util.HashMap<String, Symbol>();
	
	public static synchronized Symbol valueOf(String n) {
		final String u = n.intern();
		Symbol s = dict.get(u);
		if (s == null) {
			s = new Symbol(u);
			dict.put(u, s);
		}
		return s;
	}
}
