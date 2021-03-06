package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Result view on a single file for a query
 */
public class ResultView extends JPanel {

	private static final long serialVersionUID = 8564824276005076581L;

	private static final String TEMPLATE_FILENAME = "<html><span style='color: blue; text-decoration: underline; font-size: 16px;'>%s</span></html>";
	private static final String TEMPLATE_FILEPATH = "<html><span style='color: green; font-size: 14px;'>%s</span></html>";
	private static final String TEMPLATE_RESULT = "<html><span style='color: rgb(68, 68, 68); font-size: 13px;'>%d. %s</span></html>";
	private static final String TEMPLATE_HIGHLIGHT_START = "<span style='background-color:yellow;'>";
	private static final String TEMPLATE_HIGHLIGHT_END = "</span>";
	private static final String TEMPLATE_ADDITIONAL_RESULTS = "<html><span style='color: rgb(102, 102, 102); font-size: 11px;'>+ %d more results. Display %d more...</span></html>";
	private static final String TOOLTIP_FILENAME = "Open file '%s'";
	private static final String TOOLTIP_FILEPATH = "Open file '%s'";
	private static final String TOOLTIP_RESULT = "Open result in file '%s'";
	private static final int RESULTS_PAGE_SIZE = 10;

	private JButton fileNameView;
	private JButton filePathView;
	private List<JButton> resultsViews;
	private JButton extras;

	public ResultView() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(5, 10, 5, 10));
		setOpaque(false);

		fileNameView = new JButton();
		commomButton(fileNameView);
		add(fileNameView);

		filePathView = new JButton();
		commomButton(filePathView);
		add(filePathView);

		extras = new JButton();
		commomButton(extras);

		resultsViews = new ArrayList<JButton>();
	}

	public void updateFileName(String fileName) {
		fileNameView.setText(String.format(TEMPLATE_FILENAME, fileName));
		fileNameView.setToolTipText(String.format(TOOLTIP_FILENAME, fileName));
	}

	public void updateFilePath(String fileName, String filePath) {
		filePathView.setText(String.format(TEMPLATE_FILEPATH, filePath));
		filePathView.setToolTipText(String.format(TOOLTIP_FILEPATH, fileName));
	}

	public void appendResultPage(String fileName, List<String> results) {
		int offset = resultsViews.size();
		int loopEnd = Math.min(offset + RESULTS_PAGE_SIZE, results.size());
		for (int i = offset; i < loopEnd; i++) {
			appendResult(fileName, results.get(i));
		}
		remove(extras);
		if (resultsViews.size() < results.size()) {
			int more = results.size() - resultsViews.size();
			extras.setText(String.format(TEMPLATE_ADDITIONAL_RESULTS, more, Math.min(more, RESULTS_PAGE_SIZE)));
			add(extras);
		}
		Container parent = getParent();
		if (parent != null) {
			parent.validate();
		}
	}

	public void appendResult(String fileName, String result) {
		JButton resultView = new JButton();
		result = result.replace("<highlight>", TEMPLATE_HIGHLIGHT_START);
		result = result.replace("</highlight>", TEMPLATE_HIGHLIGHT_END);
		commomButton(resultView);
		resultView.setText(String.format(TEMPLATE_RESULT, resultsViews.size() + 1, result));
		resultView.setToolTipText(String.format(TOOLTIP_RESULT, fileName));
		resultView.setMargin(new Insets(0, 30, 0, 0));
		resultsViews.add(resultView);
		add(resultView);
	}

	private void commomButton(JButton button) {
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setBackground(Color.WHITE);
	}

	public void addOpenFileListener(ActionListener listener) {
		fileNameView.addActionListener(listener);
		filePathView.addActionListener(listener);
		for (JButton resultsView : resultsViews) {
			resultsView.addActionListener(listener);
		}
	}

	public void addMoreResultsListener(ActionListener listener) {
		extras.addActionListener(listener);
	}

	public List<JButton> getResultsViews() {
		return resultsViews;
	}

}
