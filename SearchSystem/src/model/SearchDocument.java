package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;

public class SearchDocument {

	public static final String FIELD_FILENAME = "filename";
	public static final String FIELD_CONTENT  = "content";
	public static final String FIELD_CONTENT_TV = "content_tv";

	private File file;
	private Document document;

	public SearchDocument(File file) {
		this.file = file;
		this.document = new Document();
		this.fileToDocument();
	}

	public File getFile() {
		return file;
	}

	public Document getDocument() {
		return document;
	}

	private void fileToDocument() {
		document.add(new Field(SearchDocument.FIELD_FILENAME, file.getAbsolutePath(), StoredField.TYPE));

		String content = this.fileToString();
		document.add(new Field(SearchDocument.FIELD_CONTENT, content, TextField.TYPE_STORED));

		FieldType type = new FieldType();
		type.setIndexed(true);
		type.setStored(true);
		type.setStoreTermVectors(true);
		type.setStoreTermVectorOffsets(true);
		type.setStoreTermVectorPayloads(true);
		type.setStoreTermVectorPositions(true);
		document.add(new Field(SearchDocument.FIELD_CONTENT_TV, content, type));
	}

	private String fileToString() {
		String content = "";
		try {
			content = new Scanner(new File(file.getAbsolutePath())).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			System.err.printf("Error reading file %s: %s\n", file.getName(), e.getMessage());
		}
		return content;
	}

}
