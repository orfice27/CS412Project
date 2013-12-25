package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Result;
import view.ResultView;

public class ResultController {

	private Result model;
	private SystemController parentController;

	public ResultController(ResultView view, Result model, SystemController parentController) {
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
