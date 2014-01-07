package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

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
		view.appendResultPage(fileName, model.getResults());
	}

	private void addListeners() {
		view.addOpenFileListener(new OpenFileListener());
		view.addMoreResultsListener(new MoreResultsListener());
	}

	class OpenFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			List<JButton> buttons = view.getResultsViews();
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i) == source) {
					parentController.handleOpenFile(model, i);
					return;
				}
			}
			parentController.handleOpenFile(model, -1);
		}

	}

	class MoreResultsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.appendResultPage(model.getFileName(), model.getResults());
		}
		
	}

}
