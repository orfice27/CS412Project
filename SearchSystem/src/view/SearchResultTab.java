package view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.SearchResult;
import controller.SearchResultController;
import controller.SearchSystemController;

public class SearchResultTab extends JScrollPane {

	private static final long serialVersionUID = -7415080002662301890L;

	private List<SearchResult> results;
	private String queryString;
	private JPanel container;
	private SearchSystemController parentController;

	public SearchResultTab(String queryString, List<SearchResult> results, SearchSystemController parentController) {
		this.results = results;
		this.queryString = queryString;
		this.parentController = parentController;
		container = new JPanel();
	    container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		setViewportView(container);
	    setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	    createResultsViews();
	}

	public void createResultsViews() {
		SearchResultView searchResultView;
		for (SearchResult searchResult : results) {
			searchResultView = new SearchResultView(searchResult);
			new SearchResultController(searchResultView, searchResult, parentController);
			container.add(searchResultView);
		}
	}

	public String getQueryString() {
		return queryString;
	}

}
