package view;

import javax.swing.JLabel;

import model.SearchResult;

public class SearchResultLabel extends JLabel {

	private static final long serialVersionUID = 5711966432694795611L;

	private static final String TEMPLATE = "<html>\n" +
			"<div style='color: blue; text-decoration: underline; font-size: 16px;'>%s</div>\n" +
			"<div style='color: green; font-size: 14px;'>%s</div>\n" +
			"<ol style='color: #444; font-size: 13px;'>%s</ol>\n" +
			"</html>\n";
	private static final String TEMPLATE_RESULT = "<li>%s</li>\n";
	private static final String TEMPLATE_HIGHLIGHT_START = "<span style='background-color:yellow;'>";
	private static final String TEMPLATE_HIGHLIGHT_END = "</span>";

	private SearchResult searchResult;

	public SearchResultLabel(SearchResult searchResult) {
		this.searchResult = searchResult;
		String labelText = String.format(SearchResultLabel.TEMPLATE, this.searchResult.getFileName(), this.searchResult.getFilePath(), this.getResults());
		this.setText(labelText);
	}

	private String getResults() {
		String output = "";
		for (String result : this.searchResult.getResults()) {
			result = result.replace("<highlight>", SearchResultLabel.TEMPLATE_HIGHLIGHT_START);
			result = result.replace("</highlight>", SearchResultLabel.TEMPLATE_HIGHLIGHT_END);
			output += String.format(SearchResultLabel.TEMPLATE_RESULT, result);
		}
		return output;
	}

}
