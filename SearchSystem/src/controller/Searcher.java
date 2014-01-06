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

/**
 * Indexes the data set and allows for searching over it
 */
public class Searcher {

	public static final Version LUCENE_VERSION = Version.LUCENE_46;
	private static final String PARSED_DIRECTORY = "parsed";

	private Directory directory;
	private Analyzer analyzer;
	private IndexSearcher isearcher;
	private List<Path> parsedFiles;
	private List<Path> unparsedFiles;

	public Searcher() {
	}

	/**
	 * Builds an index from the XML files in the given path.
	 * 
	 * @param root
	 *            Path containing files to be indexed
	 * @throws IOException
	 *             exceptions writing to the index
	 */
	public void index(Path root) throws IOException {
		this.directory = new RAMDirectory();
		this.analyzer = new StandardAnalyzer(Searcher.LUCENE_VERSION);
		IndexWriterConfig config = new IndexWriterConfig(Searcher.LUCENE_VERSION, analyzer);
		IndexWriter iwriter = new IndexWriter(this.directory, config);
		parseFiles(root);
		for (int i = 0; i < parsedFiles.size(); i++) {
			SearchDocument sDoc = new SearchDocument(parsedFiles.get(i), unparsedFiles.get(i));
			iwriter.addDocument(sDoc.getDocument());
		}
		iwriter.close();
	}

	/**
	 * Returns list of paths that contains the files from the root path after
	 * being parsed
	 * 
	 * @param root
	 *            Path containing files to be parsed
	 * @return list of paths that contains the files from the root path after
	 *         being parsed
	 */
	private void parseFiles(final Path root) {
		parsedFiles = new ArrayList<Path>();
		unparsedFiles = new ArrayList<Path>();
		try {
			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().endsWith(".xml") && Files.isReadable(file)) {
						Path parsedPath = FileSystems.getDefault().getPath(PARSED_DIRECTORY, file.toString().replaceFirst(root.toString(), ""));
						try {
							String parsedContents = new String(Files.readAllBytes(file)).replaceAll("\\<.*?\\>", "");
							Files.createDirectories(parsedPath.getParent());
							parsedFiles.add(Files.write(parsedPath, parsedContents.getBytes(), StandardOpenOption.CREATE));
							unparsedFiles.add(file);
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
	}

	/**
	 * Queries the index for the specified queryStirng and return a list of
	 * resulting matches
	 * 
	 * @param queryString
	 *            string to search over the index
	 * @return
	 * @throws IOException
	 *             reading from the index
	 * @throws ParseException
	 *             parsing the queryString
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
	 * Processes the results from Lucene and returns list of {@link Result}s for
	 * the in context matches found in the index
	 * 
	 * @param query
	 *            {@link Query} for this search
	 * @param topDocs
	 *            {@link TopDocs} for this search
	 * @return list of {@link Result}s for the in context matches found in the
	 *         index
	 * @throws IOException
	 *             reading search results from index or highlighing context
	 *             results
	 */
	private List<Result> handleResults(Query query, TopDocs topDocs) throws IOException {
		List<Result> searchResults = new ArrayList<Result>();
		SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<highlight>", "</highlight>");
		Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
        highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);

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
				for (TextFragment frag : highlighter.getBestTextFragments(tokenStream, doc.get(SearchDocument.FIELD_CONTENT), false,10)) {
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