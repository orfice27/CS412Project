package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;

import view.SearchQueryView;

public class SearchQueryController {

	private static final Path DICTIONARY = Paths.get("theologicaldictionary.txt.txt");
	private static final Path INDEX_DIRECTORY = Paths.get("data set", "rel200");

	private SearchQueryView view;
	private Searcher searcher;
	private SearchSystemController parentController;

	public SearchQueryController(SearchQueryView view, SearchSystemController parentController) {
		this.view = view;
		this.parentController = parentController;
		this.view.setAutoCompleteDictionary(getDictionary());
		view.addSubmitQueryListener(new SubmitQueryListener());
		searcher = new Searcher();
		try {
			searcher.index(INDEX_DIRECTORY);
		} catch (IOException e) {
			System.err.printf("Error indexing directory '%s': %s%n", INDEX_DIRECTORY, e.getMessage());
		}
	}

	class SubmitQueryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String queryString = view.getQueryString();
			if (queryString.isEmpty()) {
				view.dialogQueryEmpty();
			} else if (queryString.matches(".*[0-9].*")) {
				view.dialogQueryDigits();
			} else if (!queryString.matches(".*[A-Za-z].*")) {
				view.dialogQueryLetters();
			} else if (queryString.isEmpty()) { // TODO remove isEmpty() replace with punctuation check
				view.dialogQueryPuncutation();
			} else {
				List<SearchResult> results = new ArrayList<SearchResult>();
				try {
					results = searcher.query(queryString);
				} catch (IOException | ParseException ex) {
					System.err.printf("Error searching index for '%s': %s%n", queryString, ex.getMessage());
				}
				if (results.isEmpty()) {
					view.dialogNoResults();
				} else {
					parentController.handleSearchResults(queryString, results);
				}
 			}
		}

	}

	private Set<String> getDictionary() {
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(DICTIONARY, Charset.defaultCharset());
		} catch (IOException e) {
			System.err.printf("Error reading dictionary: %s%n", e.getMessage());
		}
		return new HashSet<String>(lines);
	}

}
