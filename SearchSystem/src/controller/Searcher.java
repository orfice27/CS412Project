package controller;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import model.SearchDocument;
import model.Result;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
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
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

	private static final Version LUCENE_VERSION = Version.LUCENE_46;
	private static final String PARSED_DIRECTORY = "parsed";

	private Directory directory;
	private Analyzer analyzer;
	private IndexSearcher isearcher;

	public Searcher() {
	}

	/**
	 * Creates the initial index from the users given root file directory path.
	 * @throws IOException  the given root may contain no documents.
	 */
	public void index(Path root) throws IOException {
		this.directory = new RAMDirectory();
		this.analyzer = new StandardAnalyzer(Searcher.LUCENE_VERSION);
		IndexWriterConfig config = new IndexWriterConfig(Searcher.LUCENE_VERSION, analyzer);
		IndexWriter iwriter = new IndexWriter(this.directory, config);
		for (Path file : parseFiles(root)) {
			SearchDocument sDoc = new SearchDocument(file);
			iwriter.addDocument(sDoc.getDocument());
		}
		iwriter.close();
	}

	private List<Path> parseFiles(final Path root) {
		final List<Path> parsed = new ArrayList<Path>();
		try {
			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".xml") && Files.isReadable(file)) {
						Path parsedPath = FileSystems.getDefault().getPath(PARSED_DIRECTORY, file.toString().replaceFirst(root.toString(), ""));
						try {
							String parsedContents = new String(Files.readAllBytes(file)).replaceAll("\\<.*?\\>", "");
							Files.createDirectories(parsedPath.getParent());
							parsed.add(Files.write(parsedPath, parsedContents.getBytes(), StandardOpenOption.CREATE));
						} catch (IOException e) {
							System.err.printf("Error parsing file %s: %s%n", file, e.getMessage());
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			System.err.printf("Error parsing files: %s%n", e.getMessage());
		}
		return parsed;
	}

	/**
	 * Inspects an index of documents given a users query.
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<Result> query(String queryString) throws IOException, ParseException {
		DirectoryReader ireader = DirectoryReader.open(this.directory);
		this.isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Searcher.LUCENE_VERSION, SearchDocument.FIELD_CONTENT, this.analyzer);
		Query query = parser.parse(queryString);
		List<Result> searchResults = this.handleResults(query, this.isearcher.search(query, null, 1000));
		ireader.close();
//		this.directory.close();
		return searchResults;
	}

	/**
	 * Sorts the search results of a user query by scoring them in terms of appropriateness to the user query
	 * @throws IOException
	 */
	private List<Result> handleResults(Query query, TopDocs topDocs) throws IOException {
		List<Result> searchResults = new ArrayList<Result>();
		SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<highlight>", "</highlight>");
		Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));

		ScoreDoc[] hits = topDocs.scoreDocs;
		int id;
		Document doc;
		TokenStream tokenStream;
		Result result;
		List<String> results;

		for (int i = 0; i < hits.length; i++) {
			results = new ArrayList<String>();
			id = hits[i].doc;
			doc = isearcher.doc(id);
			tokenStream = TokenSources.getAnyTokenStream(isearcher.getIndexReader(), id, SearchDocument.FIELD_CONTENT, analyzer);
			try {
				for (TextFragment frag : highlighter.getBestTextFragments(tokenStream, doc.get(SearchDocument.FIELD_CONTENT), false, 10)) {
					if ((frag != null) && (frag.getScore() > 0)) {
						results.add((frag.toString()));
					}
				}
			} catch (InvalidTokenOffsetsException e) {
				System.err.printf("Invalid token offsets: %s\n", e.getMessage());
			}
			result = new Result(i + 1, doc.get(SearchDocument.FIELD_FILENAME), results);
			searchResults.add(result);
		}

		return searchResults;
	}


	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.printf("Usage: %s [query] [file]\n", Searcher.class.getSimpleName());
			System.exit(0);
		}

		String queryString = args[0];
		Path fileOrDir = Paths.get(args[1]);

		Searcher s = new Searcher();
		try {
			s.index(fileOrDir);
			s.query(queryString);
		} catch (IOException | ParseException e) {
			System.err.printf("Error search '%s' for '%s': %s\n", fileOrDir, queryString, e.getMessage());
		}
	}

}