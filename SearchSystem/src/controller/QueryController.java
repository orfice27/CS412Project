package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Result;

import org.apache.lucene.queryparser.classic.ParseException;

import view.QueryView;

/**
 * Manages the running of searches on the index
 */
public class QueryController {

	private static final Path DICTIONARY = Paths.get("dictionary", "theological.txt");
	private static final Path INDEX_DIRECTORY = Paths.get("data set", "rel200");

	private QueryView view;
	private Searcher searcher;
	private SystemController parentController;

	public QueryController(QueryView view, SystemController parentController) {
		this.view = view;
		this.parentController = parentController;
		view.addSubmitQueryListener(new SubmitQueryListener());
		for (String possibility : getDictionary()) {
			this.view.addAutoComplete(possibility);
		}
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
			} else if (!queryString.matches(".*[A-Za-z].*")) {
				view.dialogQueryLetters();
			} else {
				List<Result> results = new ArrayList<Result>();
				try {
					results = searcher.query(queryString);
					parentController.addQuery(queryString);
					addAutoComplete(queryString);
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

	private void addAutoComplete(String queryString) {
		view.addAutoComplete(queryString);
		try {
			Files.write(DICTIONARY, ("\n" + queryString).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.err.printf("Error saving query '%s' to auto complete dictionary: %s%n", queryString, e.getMessage());
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
