
package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static final String[] XML_TAGS = {
		"title",
		"subtitle",
		"coverpage",
		"p",
		"b"
	};

	private Path parsed;
	private Path unparsed;
	private Document document;

	public SearchDocument(Path parsed, Path unparsed) {
		this.parsed = parsed;
		this.unparsed = unparsed;
		document = new Document();
		fileToDocument();
	}

	public Document getDocument() {
		return document;
	}

	private void fileToDocument() {
		document.add(new Field(SearchDocument.FIELD_FILENAME, parsed.toAbsolutePath().toString(), StoredField.TYPE));

		String content = this.fileToString(parsed);
		document.add(new Field(SearchDocument.FIELD_CONTENT, content, TextField.TYPE_STORED));

		for(String tag : XML_TAGS){
			Pattern p = Pattern.compile("<" + tag + "(.*)>(.+?)</" + tag + "(.*)>");
			Matcher m = p.matcher(this.fileToString(unparsed));
			while (m.find()) {
				document.add(new Field(tag, m.group(2), TextField.TYPE_STORED));
		    }
		}

		FieldType type = new FieldType();
		type.setIndexed(true);
		type.setStored(true);
		type.setStoreTermVectors(true);
		type.setStoreTermVectorOffsets(true);
		type.setStoreTermVectorPayloads(true);
		type.setStoreTermVectorPositions(true);
		document.add(new Field(SearchDocument.FIELD_CONTENT_TV, content, type));
	}

	private String fileToString(Path file) {
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(file);
		} catch (IOException e) {
			System.err.printf("Error reading file %s: %s\n", parsed.getFileName(), e.getMessage());
		}
		return new String(bytes);
	}

}
