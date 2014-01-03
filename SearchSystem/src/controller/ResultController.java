package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Result;
import view.ResultView;

/**
 * Manages individual results. Creates listeners for opening files related to
 * results
 */
public class ResultController {

	private Result model;
	private ResultView view;
	private SystemController parentController;

	public ResultController(ResultView view, Result model, SystemController parentController) {
		this.model = model;
		this.view = view;
		this.parentController = parentController;
		updateView();
		addListeners();
	}

	private void updateView() {
		String fileName = model.getFileName();
		view.updateFileName(fileName);
		view.updateFilePath(fileName, model.getFilePath());
		for (String result : model.getResults()) {
			view.appendResult(fileName, result);
		}
	}

	private void addListeners() {
		view.addOpenFileListener(new OpenFileListener());
	}

	class OpenFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			parentController.handleOpenFile(model);
		}

	}

}
