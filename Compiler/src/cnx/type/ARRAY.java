package cnx.type;

public final class ARRAY extends POINTER {

	public int capacity;

	public ARRAY(Type type, int cap) {
		super(type);
		capacity = cap;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ARRAY) {
			return elementType.equals(((ARRAY) other).elementType);
		}
		return false;
	}
}
