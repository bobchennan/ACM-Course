package cnx.temp;

public class Const implements Addr{
	public int value = 0;
	
	public Const(int v) {
		value = v;
	}
	
	public String toString() {
		return "" + value;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Const) {
			return value == ((Const) other).value;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return value;
	}
}
