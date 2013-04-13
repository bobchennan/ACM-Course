package cnx.symbol;

class Binder {
	Object value;
	Symbol prevtop;
	Binder tail;
	int lv;

	Binder(Object v, Symbol p, Binder t,int level) {
		value = v;
		prevtop = p;
		tail = t;
		lv = level;
	}
}

/**
 * The Table class is similar to java.util.Dictionary, except that each key must
 * be a Symbol and there is a scope mechanism.
 */

public class Table {

	private java.util.Dictionary<Symbol, Binder> dict = new java.util.Hashtable<Symbol, Binder>();
	private Symbol top = null;
	private Binder marks = null;
	private int level = 0;

	/**
	 * Gets the object associated with the specified symbol in the Table.
	 */
	public Object get(Symbol key) {
		Binder e = dict.get(key);
		if (e == null)
			return null;
		else
			return e.value;
	}

	/**
	 * Puts the specified value into the Table, bound to the specified Symbol.
	 */
	public boolean put(Symbol key, Object value) {
		Object tmp = dict.get(key);
		dict.put(key, new Binder(value, top, dict.get(key), level));
		top = key;
		if(tmp instanceof Binder)
			if(((Binder)tmp).lv == level)
				return false;
		return true;
	}

	/**
	 * Remembers the current state of the Table.
	 */
	public void beginScope() {
		++ level;
		marks = new Binder(null, top, marks, level);
		top = null;
	}

	/**
	 * Restores the table to what it was at the most recent beginScope that has
	 * not already been ended.
	 */
	public void endScope() {
		-- level;
		while (top != null) {
			Binder e = dict.get(top);
			if (e.tail != null)
				dict.put(top, e.tail);
			else
				dict.remove(top);
			top = e.prevtop;
		}
		top = marks.prevtop;
		marks = marks.tail;
	}

	/**
	 * Returns an enumeration of the Table's symbols.
	 */
	public java.util.Enumeration<Symbol> keys() {
		return dict.keys();
	}
}
