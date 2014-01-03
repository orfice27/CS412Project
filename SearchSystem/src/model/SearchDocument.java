package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;

/**
 * Converts a file to a document that can be used with Lucene. Sets the file up
 * to be used with the Lucene highlighter for contextual results and stores the
 * filename but does not make it searchable.
 */
public class SearchDocument {

	public static final String FIELD_FILENAME = "filename";
	public static final String FIELD_CONTENT  = "content";
	public static final String FIELD_CONTENT_TV = "content_tv";

	private Path file;
	private Document document;

	public SearchDocument(Path file) {
		this.file = file;
		document = new Document();
		fileToDocument();
	}

	public Document getDocument() {
		return document;
	}

	private void fileToDocument() {
		document.add(new Field(SearchDocument.FIELD_FILENAME, file.toAbsolutePath().toString(), StoredField.TYPE));

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
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(file);
		} catch (IOException e) {
			System.err.printf("Error reading file %s: %s\n", file.getFileName(), e.getMessage());
		}
		return new String(bytes);
	}

}
