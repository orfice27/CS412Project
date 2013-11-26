package contextSearch;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
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


public class HighlighterExample {

	private static final Version LUCENE_VERSION = Version.LUCENE_45;
	private static final String FIELD_TV = "tv";
	private static final String FIELD_NOTV = "notv";
	private static final String QUERY_TERM = "million";

	private static Document createDoc(String preffix) {
		String content = preffix + ": content all million in here";
		Document doc = new Document();
		FieldType type = new FieldType();
		type.setIndexed(true);
		type.setStored(true);
		type.setStoreTermVectors(true);
		type.setStoreTermVectorOffsets(true);
		type.setStoreTermVectorPayloads(true);
		type.setStoreTermVectorPositions(true);
		doc.add(new Field(HighlighterExample.FIELD_TV, content, type));
		doc.add(new Field(HighlighterExample.FIELD_NOTV, content, TextField.TYPE_STORED));
		return doc;
	}

	public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {

		Directory directory = new RAMDirectory();
		Analyzer analyzer = new StandardAnalyzer(HighlighterExample.LUCENE_VERSION);

		IndexWriterConfig config = new IndexWriterConfig(HighlighterExample.LUCENE_VERSION, analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);

		iwriter.addDocument(HighlighterExample.createDoc("doc1"));
		iwriter.addDocument(HighlighterExample.createDoc("doc2"));

		iwriter.close();

		DirectoryReader ireader = DirectoryReader.open(directory);

		// ... Above, create documents with two fields, one with term vectors (tv) and one without (notv)
		IndexSearcher searcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(HighlighterExample.LUCENE_VERSION, HighlighterExample.FIELD_NOTV, analyzer);
		Query query = parser.parse(HighlighterExample.QUERY_TERM);

		TopDocs hits = searcher.search(query, ireader.maxDoc());

		SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
		Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
		for (int i = 0; i < ireader.maxDoc(); i++) {
			int id = hits.scoreDocs[i].doc;
			Document doc = searcher.doc(id);
			String text = doc.get(HighlighterExample.FIELD_NOTV);
			TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), id, HighlighterExample.FIELD_NOTV, analyzer);
			TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 10);// highlighter.getBestFragments(tokenStream, text, 3, "...");
			for (int j = 0; j < frag.length; j++) {
				if ((frag[j] != null) && (frag[j].getScore() > 0)) {
					System.out.println((frag[j].toString()));
				}
			}
			// Term vector
			text = doc.get(HighlighterExample.FIELD_TV);
			tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), hits.scoreDocs[i].doc, HighlighterExample.FIELD_TV, analyzer);
			frag = highlighter.getBestTextFragments(tokenStream, text, false, 10);
			for (int j = 0; j < frag.length; j++) {
				if ((frag[j] != null) && (frag[j].getScore() > 0)) {
					System.out.println((frag[j].toString()));
				}
			}
			System.out.println("-------------");
		}
	}
}