package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.SearchResult;
import view.SearchResultView;

public class SearchResultController {

	private SearchResult model;
	private SearchSystemController parentController;

	public SearchResultController(SearchResultView view, SearchResult model, SearchSystemController parentController) {
		this.model = model;
		this.parentController = parentController;
		view.addOpenFileListener(new OpenFileListener());
	}

	class OpenFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			parentController.handleOpenFile(model);
		}

	}

}
