package controller;

import java.util.List;

import model.SearchResult;
import view.FileDisplayTab;
import view.SearchResultTab;
import view.SearchSystemView;

public class SearchSystemController {

	private SearchSystemView view;

	public SearchSystemController(SearchSystemView view) {
		this.view = view;
	}

	public void handleSearchResults(String queryString, List<SearchResult> results) {
		SearchResultTab tab = view.getSearchResultTab(queryString);
		if (tab == null) {
			tab = new SearchResultTab(queryString, results, this);
			view.addTab(queryString, tab);
		}
		view.setSelectedTab(tab);
	}

	public void handleOpenFile(SearchResult result) {
		String filepath = result.getFilePath();
		FileDisplayTab tab = view.getOpenFileTab(filepath);
		if (tab == null) {
			tab = new FileDisplayTab(filepath);
			view.addTab(result.getFileName(), tab);
		}
		view.setSelectedTab(tab);
	}

}
