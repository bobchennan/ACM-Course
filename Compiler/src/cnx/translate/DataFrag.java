package cnx.translate;

import cnx.temp.*;
import cnx.quad.*;

public class DataFrag extends Quad {

	Label label = null;
	String value = null;
	
	public DataFrag(Label l, String s) {
		label = l;
		value = s;
	}
	
	public String toString(){
		return label + ": " + escapeSpecialChars(value);
	}
	
	private static String escapeSpecialChars(String s) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\n') {
				buf.append("\\n");
			}
			else if (c == '\t') {
				buf.append("\\t");
			}
			else if (c == '\"') {
				buf.append("\\\"");
			}
			else if (c == '\r') {
				buf.append("\\r");
			}
			else {
				buf.append(c);
			}
		}
		return buf.toString();
	}
}
