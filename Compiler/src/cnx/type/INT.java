package cnx.type;

public final class INT extends Type {

	private static INT instance = null;

	public static synchronized INT getInstance() {
		if (instance == null) {
			instance = new INT();
		}
		return instance;
	}

	private INT() {
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof INT;
	}
}
