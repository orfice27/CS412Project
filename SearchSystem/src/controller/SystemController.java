package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import model.History;
import model.Result;
import view.FileDisplayTab;
import view.HistoryTab;
import view.ResultTab;
import view.SystemView;

/**
 * Manages which tabs are open and loading and saving of query history
 */
public class SystemController {

	private static final Path HISTORY = Paths.get("history", "history.json");
	private History history;
	private SystemView view;

	public SystemController(SystemView view) {
		this.view = view;
		this.history = new History(HISTORY);
	}

	public void addListeners() {
		view.addCloseTabListener(new CloseTabListener());
		view.addClearHistoryListener(new ClearHistoryListener());
		view.addViewHistoryListener(new ViewHistoryListener());
		view.addWindowListener(new WindowListener());
	}

	public void handleSearchResults(String queryString, List<Result> results) {
		ResultTab tab = view.getSearchResultTab(queryString);
		if (tab == null) {
			tab = new ResultTab(queryString);
			new ResultTabController(tab, results, this);
			view.addTab(queryString, tab);
		}
		view.setSelectedTab(tab);
	}

	public void handleOpenFile(Result result)  {
		String filepath = result.getFilePath();
		FileDisplayTab tab = view.getOpenFileTab(filepath);
		if (tab == null) {
			tab = new FileDisplayTab(filepath, result.getResults());
			view.addTab(result.getFileName(), tab);
		}
		tab.prepareResultsText(result);
		view.setSelectedTab(tab);
	}

	public void addQuery(String queryString) {
		history.addQuery(queryString);
	}

	class CloseTabListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Component component = view.getSelectedTab();
			if (component == null) {
				view.dialogNoTabs();
			} else {
				view.closeTab(view.getSelectedTab());
			}
		}

	}

	private void saveHistory() {
		try {
			history.save();
		} catch (IOException ex) {
			System.err.printf("Error saving history: %s%n", ex.getMessage());
		}
	}

	class ClearHistoryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			history.clear();
			saveHistory();
		}

	}

	class ViewHistoryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			HistoryTab tab = view.getOpenHistoryTab();
			if (tab == null) {
				tab = new HistoryTab();
				new HistoryTabController(history, tab);
				view.addTab("History", tab);
			}
			view.setSelectedTab(tab);
		}

	}

	class WindowListener extends WindowAdapter {

		@Override
		public void windowOpened(WindowEvent e) {
			try {
				history.load();
			} catch (IOException ex) {
				System.err.printf("Error loading history: %s%n", ex.getMessage());
			}
		}

		@Override
		public void windowClosing(WindowEvent e) {
			saveHistory();
		}

	}

}
