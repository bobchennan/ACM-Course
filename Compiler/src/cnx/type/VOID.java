package cnx.type;

public final class VOID extends Type {

	private static VOID instance = null;

	public static synchronized VOID getInstance() {
		if (instance == null) {
			instance = new VOID();
		}
		return instance;
	}

	private VOID() {
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof VOID;
	}
}
