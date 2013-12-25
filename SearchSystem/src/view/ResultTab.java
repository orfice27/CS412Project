package view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Result;
import controller.ResultController;
import controller.SystemController;

public class ResultTab extends JScrollPane {

	private static final long serialVersionUID = -7415080002662301890L;

	private List<Result> results;
	private String queryString;
	private JPanel container;
	private SystemController parentController;

	public ResultTab(String queryString, List<Result> results, SystemController parentController) {
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
		ResultView resultView;
		for (Result result : results) {
			resultView = new ResultView(result);
			new ResultController(resultView, result, parentController);
			container.add(resultView);
		}
	}

	public String getQueryString() {
		return queryString;
	}

}
