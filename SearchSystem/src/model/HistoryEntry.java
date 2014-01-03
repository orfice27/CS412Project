package model;

import java.util.Date;

/**
 * Container for the query string and time stamp the query occurred
 * 
 */
public class HistoryEntry implements Comparable<HistoryEntry> {

	private String query;
	private Date time;

	public HistoryEntry(String query, Date time) {
		this.query = query;
		this.time = time;
	}

	public String getQuery() {
		return query;
	}

	public Date getTime() {
		return time;
	}

	@Override
	public int compareTo(HistoryEntry o) {
		int comp = time.compareTo(o.time);
		if (comp != 0) {
			return comp;
		} else {
			return query.compareTo(o.query);
		}
	}

}
