
package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
	ArrayList<String> xmlTags;
	public static final String TITLE_XML  = "title";
	public static final String PARAGRAPH_XML = "p";
	public static final String BOLD_XML ="b";
	public static final String SUBTITLE_XML = "subtitle"; 
	public static final String COVERPAGE_XML = "coverpage";

	private Path ParsedFile;
	private Path unParsedFile;

	private Document document;

	public SearchDocument(Path file, Path unparsedFile) {
		xmlTags = new ArrayList<String>();
		xmlTags.add(TITLE_XML);
		xmlTags.add(SUBTITLE_XML);
		xmlTags.add(BOLD_XML);
		xmlTags.add(PARAGRAPH_XML);
		xmlTags.add(COVERPAGE_XML);

		this.ParsedFile = file;
		this.unParsedFile = unparsedFile;
		document = new Document();
		fileToDocument();
	}

	public Document getDocument() {
		return document;
	}

	private void fileToDocument() {
		document.add(new Field(SearchDocument.FIELD_FILENAME, ParsedFile.toAbsolutePath().toString(), StoredField.TYPE));

		String content = this.fileToString(ParsedFile);
		document.add(new Field(SearchDocument.FIELD_CONTENT, content, TextField.TYPE_STORED));
		for(String item : xmlTags){
			Pattern p = Pattern.compile("<" + item + ">(.+?)</" + item + ">");
			Matcher m = p.matcher(this.fileToString(unParsedFile));
			while (m.find()) {
				document.add(new Field(item, m.group(1), TextField.TYPE_STORED));
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
			System.err.printf("Error reading file %s: %s\n", ParsedFile.getFileName(), e.getMessage());
		}
		return new String(bytes);
	}

}
