package controller;

import java.util.Observable;
import java.util.Observer;

import model.History;
import model.HistoryEntry;
import view.HistoryTab;

public class HistoryTabController implements Observer {

	private History model;
	private HistoryTab view;

	public HistoryTabController(History model, HistoryTab view) {
		this.model = model;
		this.view = view;
		for (HistoryEntry historyEntry : this.model.getQueries()) {
			view.appendHistoryEntry(historyEntry);
		}
		model.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == null) {
			view.clearHistory();
		} else {
			HistoryEntry historyEntry = (HistoryEntry) arg;
			view.appendHistoryEntry(historyEntry);
		}
	}

}
