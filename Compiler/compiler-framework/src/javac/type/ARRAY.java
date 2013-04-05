package javac.type;

public final class ARRAY extends Type {

	public Type elementType;

	public ARRAY(Type type) {
		elementType = type;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ARRAY) {
			return elementType.equals(((ARRAY) other).elementType);
		}
		return false;
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public boolean isRecord() {
		return false;
	}
	
	
}
