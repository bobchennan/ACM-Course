package cnx.temp;

import cnx.analysis.LiveInterval;

public class Temp implements Addr{
	public static int count = 0;
	public int num = 0;

	public String toString() {
		return "t" + num;
	}
	
	public Temp() {
		num = count++;
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
