package searcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import xmlParser.XMLParser;

public class Searcher {

	private static final Version LUCENE_VERSION = Version.LUCENE_45;
	private static final String FIELD_CONTENT = "CONTENT";
	private static final String FIELD_FILENAME = "FILENAME";

	private String root;
	private String queryString;
	private Directory directory;
	private Analyzer analyzer;
	private TopDocs searchResult;
	private IndexSearcher isearcher;

	public Searcher(String root, String queryString) {
		this.root = root;
		this.queryString = queryString;
		this.directory = new RAMDirectory();
		this.analyzer = new StandardAnalyzer(Searcher.LUCENE_VERSION);
	}

	public void search() throws IOException, ParseException {
		this.setupIndex();
		this.queryIndex();
	}

	private void setupIndex() throws IOException {
	    IndexWriterConfig config = new IndexWriterConfig(Searcher.LUCENE_VERSION, analyzer);
	    IndexWriter iwriter = new IndexWriter(this.directory, config);
	    this.parseDocuments(iwriter);
	    iwriter.close();
	}

	private void queryIndex() throws IOException, ParseException {
		DirectoryReader ireader = DirectoryReader.open(this.directory);
	    this.isearcher = new IndexSearcher(ireader);
	    QueryParser parser = new QueryParser(Searcher.LUCENE_VERSION, Searcher.FIELD_CONTENT, this.analyzer);
	    Query query = parser.parse(this.queryString);
	    this.searchResult = this.isearcher.search(query, null, 1000);
	    this.handleResults();
	    ireader.close();
	    directory.close();
	}

	private void handleResults() throws IOException {
		ScoreDoc[] hits = this.searchResult.scoreDocs;
	    for (int i = 0; i < hits.length; i++) {
	      Document hitDoc = isearcher.doc(hits[i].doc);
	      System.out.printf("%s: %s\n", Searcher.FIELD_FILENAME, hitDoc.get(Searcher.FIELD_FILENAME));
	    }
	}

	private void parseDocuments(IndexWriter iwriter) throws IOException {
		List<File> files = new XMLParser(this.root).getParsedFiles();
		List<Document> docs = this.filesToDocuments(files);
	    for (Document doc : docs) {
		    iwriter.addDocument(doc);
	    }		
	}

	private List<Document> filesToDocuments(List<File> files) {
		List<Document> docs = new ArrayList<Document>();
		for (File file : files) {
			Document doc = new Document();
		    doc.add(new Field(Searcher.FIELD_FILENAME, file.getName(), StoredField.TYPE));
		    doc.add(new Field(Searcher.FIELD_CONTENT, this.fileToString(file), TextField.TYPE_STORED));
		    docs.add(doc);
		}
		return docs;
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


	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.printf("Usage: %s [query] [file]\n", Searcher.class.getSimpleName());
			System.exit(0);
		}
		
		String queryString = args[0];
		String fileOrDir = args[1];

		Searcher s = new Searcher(fileOrDir, queryString);
		try {
			s.search();
		} catch (IOException | ParseException e) {
			System.err.printf("Error search '%s' for '%s': %s\n", fileOrDir, queryString, e.getMessage());
		}
	}

}