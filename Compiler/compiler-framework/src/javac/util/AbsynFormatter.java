package javac.util;

import java.util.ArrayList;
import java.util.List;

public class AbsynFormatter {

	public static String format(String s) {
		return indent(split(s));
	}
	
	private static List<String> split(String s) {
		final List<String> lines = new ArrayList<String>();
		final StringBuffer currentLine = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ']') {
				lines.add(currentLine.toString());
				currentLine.setLength(0);
				currentLine.append(']');
			} else {
				currentLine.append(s.charAt(i));
				if (s.charAt(i) == '[' || s.charAt(i) == ',') {
					lines.add(currentLine.toString());
					currentLine.setLength(0);
				}
			}
		}
		if (currentLine.length() > 0) {
			lines.add(currentLine.toString());
		}
		return lines;
	}
	
	private static String indent(List<String> lines) {
		final StringBuffer s = new StringBuffer();
		int k = 0;
		for (String line : lines) {
			if (line.startsWith("]")) {
				k--;
			}
			s.append(indentSpaces(k));
			s.append(line.trim());
			s.append('\n');
			if (line.endsWith("[")) {
				k++;
			}
		}
		return s.toString();
	}
	
	private static String indentSpaces(int k) {
		final StringBuffer s = new StringBuffer();
		for (int i = 0; i < k; i++) {
			s.append("  ");
		}
		return s.toString();
	}
}
