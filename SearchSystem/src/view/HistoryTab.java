package view;

import java.awt.Color;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Tab for displaying query history
 */
public class HistoryTab extends JScrollPane {

	private static final long serialVersionUID = 740032827501268132L;
	private static final String TEMPLATE_HISTORY_ENTRY = "<html><span style='color: rgb(102, 102, 102); font-size: 13px;'>%s</span> - <span style='color: rgb(68, 68, 68); font-size: 13px;'>%s</span></html>";
	private static final String DATE_FORMAT = "EEEE, MMMMM dd, yyyy hh:mm a";

	private JPanel container;
	private List<JButton> historyEntryViews;
	private SimpleDateFormat dateFormat;

	public HistoryTab() {
		historyEntryViews = new ArrayList<JButton>();
		dateFormat = new SimpleDateFormat(DATE_FORMAT);

		container = new JPanel();
		container.setBackground(Color.WHITE);
		container.setBorder(new EmptyBorder(10, 10, 10, 10));
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		setViewportView(container);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	}

	public void appendHistoryEntry(String query, Date time) {
		JButton historyEntryView = new JButton();
		historyEntryView.setMargin(new Insets(0, 0, 0, 0));
		historyEntryView.setHorizontalAlignment(SwingConstants.LEFT);
		historyEntryView.setBorderPainted(false);
		historyEntryView.setOpaque(false);
		historyEntryView.setBackground(Color.WHITE);
		historyEntryView.setText(String.format(TEMPLATE_HISTORY_ENTRY, dateFormat.format(time), query));
		historyEntryViews.add(historyEntryView);
		container.add(historyEntryView);
		validate();
	}

	public void clearHistory() {
		for (JButton historyEntry : historyEntryViews) {
			container.remove(historyEntry);
		}
		repaint();
	}

}
