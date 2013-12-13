package xmlTagSearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;

import xmlTagSearcher.Tag.TagOptions;
import controller.Searcher;

public class XMLSearcher extends Searcher {

	List<Boolean> validity = new ArrayList<Boolean>();

	public XMLSearcher() {
	}

	/**
	 * Creates a new XMLSearcher for a search space see{@Searcher.java} for more impl. details
	 * 
	 * @param queryString
	 *            - some XML TagOption term to search the documents upon
	 * @throws NonXMLException
	 *             The query string must consist of only TagOption XML terms.
	 */
	public List<SearchResult> tagQuery(String queryString) throws NonXMLException {
		List<SearchResult> results = null;
		// Perhaps alter the query string here to conform to the language or have another language conformer class
		for (TagOptions t : TagOptions.values()) {
			validity.add(queryString.equals(t.toString()));
		}
		if (validity.contains(true)) {
		} else {
			throw new NonXMLException("You have Entered a TagOption that our program doesn't handle");
		}
		try {
			results = this.query(queryString);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return results;
	}

}
