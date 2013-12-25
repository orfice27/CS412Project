package model;

import java.io.File;
import java.util.List;

public class Result {

	private int position;
	private String filePath;
	private List<String> results;

	public Result(int position, String filePath, List<String> results) {
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

	public final String getFilePath() {
		return this.filePath;
	}

	public final String getFileName() {
		return this.filePath.substring(this.filePath.lastIndexOf(File.separatorChar) + 1);
	}

	public final String getFileDirectory() {
		return this.filePath.substring(0, this.filePath.lastIndexOf(File.separatorChar));
	}

}
