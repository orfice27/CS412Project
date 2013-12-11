package model;

import java.io.File;
import java.util.List;

public class SearchResult {

	private int position;
	private String filePath;
	private List<String> results;

	public SearchResult(int position, String filePath, List<String> results) {
		this.position = position;
		this.filePath = filePath;
		this.results = results;
	}

	public final List<String> getResults() {
		return this.results;
	}

	public final int getPosition() {
		return position;
	}

	public final String getFileName() {
		return this.filePath.substring(this.filePath.lastIndexOf(File.separatorChar) + 1);
	}

	public final String getFilePath() {
		return this.filePath.substring(0, this.filePath.lastIndexOf(File.separatorChar));
	}

}
