package javac.util;

public final class Position {

	private int line;
	private int column;
	
	public static final Position EmptyPostion = new Position(-1, -1);
	
	public static boolean isEmptyPostion(Position pos){
		return pos == EmptyPostion;
	}
	
	private Position(int l, int c) {
		line = l;
		column = c;
	}
	
	public static Position valueOf(int line, int column) {
		return new Position(line, column);
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}
	
	@Override
	public String toString() {
		return String.format("(%d;%d)", line, column);
	}
}
