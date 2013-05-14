package cnx.temp;

import cnx.analysis.LiveInterval;
import cnx.env.Constants;
import cnx.translate.Level;

public class Temp implements Addr{
	public static int count = 0;
	public int num = 0;
	public Level home = null;
	public int index = 0;
	public boolean allArea = false;

	public String toString() {
		return "t" + num;
	}
	
	public Temp(Level l, int idx) {
		num = count++;
		home = l;
		index = idx;
		if(l.label.toString() == Constants.top_level)
			allArea = true;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Temp) {
			return num == ((Temp) other).num;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return num;
	}
	
	private LiveInterval interval = null;

	public void expandInterval(int qcount) {
		if (interval == null) {
			interval = new LiveInterval(this, qcount);
		}
		interval.insert(qcount);
	}

	public LiveInterval getLiveInterval() {
		return interval;
	}

	public void clearLiveInterval() {
		interval = null;
	}
}
