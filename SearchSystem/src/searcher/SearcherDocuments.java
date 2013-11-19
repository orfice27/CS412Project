package searcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;

public class SearcherDocuments {

	public static final String FIELD_FILENAME = "filename";
	public static final String FIELD_CONTENT  = "content";

	private List<File> files;
	private List<Document> documents;

	public SearcherDocuments(List<File> files) {
		this.files = files;
		this.documents = new ArrayList<Document>();
		this.filesToDocuments();
	}

	public List<Document> getDocuments() {
		return this.documents;
	}

	public List<File> getFiles() {
		return this.files;
	}

	private void filesToDocuments() {
		for (File file : this.files) {
			this.documents.add(this.fileToDocument(file));
		}
	}

	private Document fileToDocument(File file) {
		Document doc = new Document();
	    doc.add(new Field(SearcherDocuments.FIELD_FILENAME, file.getName(), StoredField.TYPE));
	    doc.add(new Field(SearcherDocuments.FIELD_CONTENT, this.fileToString(file), TextField.TYPE_STORED));
	    return doc;
	}

	private String fileToString(File file) {
		String content = "";
		try {
			content = new Scanner(new File(file.getAbsolutePath())).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			System.err.printf("Error reading file %s: %s\n", file.getName(), e.getMessage());
		}
		return content;
	}

}