package searcher;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
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

	private String root;
	private String queryString;
	private Directory directory;
	private Analyzer analyzer;
	private TopDocs searchResult;
	private IndexSearcher isearcher;

	/**
	 * Creates an instance of searcher , given the root absolute path and a 
	 * query the user wish to perform upon the system.
	 * @param root - the absolute path the of the users root 
	 * folder to the file corpus to be searched upon.
	 * 
	 * @param queryString the query the user wishes to check exists 
	 * within the file corpus.
	 */
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
	/**
	 * Creates the initial index from the users given root file directory path.
	 * @throws IOException  the given root may contain no documents.
	 */
	private void setupIndex() throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(Searcher.LUCENE_VERSION, analyzer);
		IndexWriter iwriter = new IndexWriter(this.directory, config);
		this.parseDocuments(iwriter);
		iwriter.close();
	}
	/**
	 * Using a given indexWriter creates a list of all files contained below the root directory path.
	 * @param iwriter - the given root directory's indexWriter
	 * @throws IOException the given root may contain no documents.
	 */
	private void parseDocuments(IndexWriter iwriter) throws IOException {
		List<File> files = new XMLParser(this.root).getParsedFiles();
		List<Document> docs = new SearcherDocuments(files).getDocuments();
		for (Document doc : docs) {
			iwriter.addDocument(doc);
		}
	}
	/**
	 * Inspects an index of documents given a users query.
	 * @throws IOException
	 * @throws ParseException
	 */
	private void queryIndex() throws IOException, ParseException {
		DirectoryReader ireader = DirectoryReader.open(this.directory);
		this.isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Searcher.LUCENE_VERSION, SearcherDocuments.FIELD_CONTENT, this.analyzer);
		Query query = parser.parse(this.queryString);
		this.searchResult = this.isearcher.search(query, null, 1000);
		this.handleResults();
		ireader.close();
		directory.close();
	}

	/**
	 * Sorts the search results of a user query by scoring them in terms of appropriateness to the user query
	 * @throws IOException
	 */
	private void handleResults() throws IOException {
		ScoreDoc[] hits = this.searchResult.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			System.out.printf("%s: %s\n", SearcherDocuments.FIELD_FILENAME, hitDoc.get(SearcherDocuments.FIELD_FILENAME));
		}
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