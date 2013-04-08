package cnx.type;

public class POINTER extends Type {
	public Type elementType;
	
	public POINTER(Type type) {
		elementType = type;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof POINTER) {
			return elementType.equals(((POINTER) other).elementType);
		}
		return false;
	}
}