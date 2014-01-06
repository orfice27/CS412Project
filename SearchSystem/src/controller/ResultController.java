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
		for (int i =0 ; i< model.getResults().size() && i < 10 ; i++) {
			view.appendResult(fileName, model.getResults().get(i));
		}
	}

	private void addListeners() {
		view.addOpenFileListener(new OpenFileListener());
	}

	class OpenFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			List<JButton> buttons =view.getResultsViews();
			for(int i = 0;i < buttons.size(); i++){
				if(buttons.get(i) == (JButton)e.getSource()){
					parentController.handleOpenFile(model,i);
				}
			}
		}

	}

}
