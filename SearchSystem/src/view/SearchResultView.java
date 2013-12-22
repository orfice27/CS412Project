package view;

import java.awt.Color;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.GUIController;
import controller.SearchResultController;
import model.SearchResult;

public class SearchResultView extends JPanel {

	private static final long serialVersionUID = 5711966432694795611L;

	private static final String TEMPLATE_FILENAME = "<html><span style='color: blue; text-decoration: underline; font-size: 16px;'>%s</span></html>";
	private static final String TEMPLATE_FILEPATH = "<html><span style='color: green; font-size: 14px;'>%s</span></html>";
	private static final String TEMPLATE_RESULT = "<html><span style='color: #444; font-size: 13px;'>%d. %s</span></html>";
	private static final String TEMPLATE_HIGHLIGHT_START = "<span style='background-color:yellow;'>";
	private static final String TEMPLATE_HIGHLIGHT_END = "</span>";
	private static final String TOOLTIP_FILENAME = "Open file '%s'";
	private static final String TOOLTIP_FILEPATH = "Open file '%s'";
	private static final String TOOLTIP_RESULT = "Open result in file '%s'";

	private SearchResult searchResult;
	private JButton fileNameView;
	private JButton filePathView;
	private List<JButton> resultsView;
	private SearchResultController controller;
	GUI view;
	GUIController control;

	public SearchResultView(SearchResult searchResult, GUI view, GUIController control) {
		this.view = view;
		this.searchResult = searchResult;
		this.control = control;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(5, 10, 5, 10));
		controller = new SearchResultController(searchResult, view,control);
		createFileName();
		createFilePath();
		createResults();
	}
	
	public GUI returnGUI(){
		return view;
	}

	private void createFileName() {
		String fileName = searchResult.getFileName();
		fileNameView = new JButton();
		commomButton(fileNameView);
		fileNameView.setText(String.format(TEMPLATE_FILENAME, fileName));
		fileNameView.setToolTipText(String.format(TOOLTIP_FILENAME, fileName));
		add(fileNameView);
	}

	private void createFilePath() {
		String fileName = searchResult.getFileName();
		String filePath = searchResult.getFilePath();
		filePathView = new JButton();
		commomButton(filePathView);
		filePathView.setText(String.format(TEMPLATE_FILEPATH, filePath));
		filePathView.setToolTipText(String.format(TOOLTIP_FILEPATH, fileName));
		add(filePathView);
	}

	private void createResults() {
		String fileName = searchResult.getFileName();
		List<String> results = searchResult.getResults();
		resultsView = new ArrayList<JButton>();
		String result;
		JButton resultView;
		for (int i = 0; i < results.size(); i++) {
			result = results.get(i);
			result = result.replace("<highlight>", TEMPLATE_HIGHLIGHT_START);
			result = result.replace("</highlight>", TEMPLATE_HIGHLIGHT_END);
			resultView = new JButton();
			commomButton(resultView);
			resultView.setText(String.format(TEMPLATE_RESULT, i + 1, result));
			resultView.setToolTipText(String.format(TOOLTIP_RESULT, fileName));
			resultView.setMargin(new Insets(0,30,0,0));
			resultsView.add(resultView);
			add(resultView);
		}
	}

	private void commomButton(JButton button) {
		button.setMargin(new Insets(0,0,0,0));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setBackground(Color.WHITE);
		button.addActionListener(controller);
		button.setActionCommand("opentext");
	}

}
