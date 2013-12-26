package controller;

import java.util.List;

import model.Result;
import view.ResultTab;
import view.ResultView;

public class ResultTabController {

	public ResultTabController(ResultTab view, List<Result> models, SystemController parentController) {
		ResultView resultView;
		for (Result model : models) {
			resultView = new ResultView();
			new ResultController(resultView, model, parentController);
			view.appendResultView(resultView);
		}
	}

}
