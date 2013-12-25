package view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.History;
import model.HistoryEntry;

public class HistoryTab extends JScrollPane {

	private static final long serialVersionUID = 740032827501268132L;

	private JPanel container;
	private JTextArea textArea;

	public HistoryTab(History history) {
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		setViewportView(container);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		createTextView();
	}

	private void createTextView() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		container.add(textArea);
	}

	public void appendHistoryEntry(HistoryEntry historyEntry) {
		textArea.append(String.format("%s @ %s%n", historyEntry.getQuery(), historyEntry.getTime()));
	}

	public void clearHistory() {
		textArea.setText("");
	}

}
