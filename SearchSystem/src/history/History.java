package history;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class History {

	private static final String DATE_FORMAT = "Y/M/d H:m:s:S Z";

	private SortedSet<HistoryQuery> history;
	private SimpleDateFormat dateFormat;
	private String fileName;

	public History(String fileName) {
		this.history = new TreeSet<HistoryQuery>();
		this.fileName = fileName;
		this.dateFormat = new SimpleDateFormat(History.DATE_FORMAT);
	}

	public void addQuery(String query) {
		HistoryQuery hq = new HistoryQuery();
		hq.query = query;
		hq.time = new Date();
		history.add(hq);
	}

	public SortedSet<HistoryQuery> getQueries() {
		return this.history;
	}

	public void clear() {
		this.history.clear();;
	}

	public void save() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.fileName));
			out.write(JSONValue.toJSONString(this.toJSON()));
	        out.close();
		} catch (IOException e) {
			System.err.printf("Error writing file '%s': %s\n",  this.fileName, e.getMessage());
		}
	}

	public void load() {
		String json = "";
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(this.fileName));
			json = Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
		} catch (IOException e) {
			System.err.printf("Error reading file '%s': %s\n",  this.fileName, e.getMessage());
		}
		this.fromJSON(json);
	}

	private JSONArray toJSON() {
		JSONArray c = new JSONArray();
		JSONObject o;
		for (HistoryQuery hq : this.history) {
			o = new JSONObject();
			o.put("query", hq.query);
			o.put("time", dateFormat.format(hq.time));
			c.add(o);
		}
		return c;
	}

	private void fromJSON(String json) {
		JSONArray c = (JSONArray)JSONValue.parse(json);
		JSONObject o;
		HistoryQuery hq;
		for (Object oc : c) {
			o = (JSONObject)oc;
			hq = new HistoryQuery();
			hq.query = (String) o.get("query");
			try {
				hq.time = dateFormat.parse((String) o.get("time"));
			} catch (ParseException e) {
				System.err.printf("Error parsing time '%s' of query '%s': %s\n",  hq.time, hq.query, e.getMessage());
			}
			this.history.add(hq);
		}
	}

	private class HistoryQuery implements Comparable<HistoryQuery> {
		public String query;
		public Date time;

		@Override
		public int compareTo(HistoryQuery o) {
			int comp = time.compareTo(o.time);
			if (comp != 0) {
				return comp;
			} else {
				return query.compareTo(o.query);
			}
		}
	}

}