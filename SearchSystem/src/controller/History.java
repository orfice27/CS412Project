package controller;

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

import model.HistoryEntry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class History {

	private static final String DATE_FORMAT = "Y/M/d H:m:s:S Z";

	private SortedSet<HistoryEntry> history;
	private SimpleDateFormat dateFormat;
	private String fileName;

	public History(String fileName) {
		this.history = new TreeSet<HistoryEntry>();
		this.fileName = fileName;
		this.dateFormat = new SimpleDateFormat(History.DATE_FORMAT);
	}

	public void addQuery(String query) {
		HistoryEntry hq = new HistoryEntry(query, new Date());
		history.add(hq);
	}

	public SortedSet<HistoryEntry> getQueries() {
		return this.history;
	}

	public void clear() {
		this.history.clear();
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

	@SuppressWarnings("unchecked")
	private JSONArray toJSON() {
		JSONArray c = new JSONArray();
		JSONObject o;
		for (HistoryEntry hq : this.history) {
			o = new JSONObject();
			o.put("query", hq.getQuery());
			o.put("time", dateFormat.format(hq.getTime()));
			c.add(o);
		}
		return c;
	}

	private void fromJSON(String json) {
		JSONArray c = (JSONArray)JSONValue.parse(json);
		JSONObject o;
		String query = null;
		Date time = null;
		for (Object oc : c) {
			o = (JSONObject)oc;
			query = (String) o.get("query");
			try {
				time = dateFormat.parse((String) o.get("time"));
			} catch (ParseException e) {
				System.err.printf("Error parsing time '%s' of query '%s': %s\n",  time, query, e.getMessage());
			}
			this.history.add(new HistoryEntry(query, time));
		}
	}

}
