package cnx.type;

public final class ARRAY extends POINTER {

	public Object capacity, other;

	public ARRAY(Type type, Object cap) {
		super(type);
		capacity = cap;
	}
	public ARRAY(Type type, Object cap, Object other){
		super(type);
		capacity = cap;
		this.other = other;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ARRAY) {
			return elementType.equals(((ARRAY) other).elementType);
		}
		return false;
	}
}
