package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.QueryController;
import controller.SystemController;

public class SystemView extends JFrame {

	private static final long serialVersionUID = -6488174892752959409L;

	private static final int FRAME_SIZE = 800;

	private QueryView queryView;
	private JTabbedPane tabPane;
	private JMenuItem closeTabItem;
	private JMenuItem clearHistoryItem;
	private JMenuItem viewHistoryItem;

	public SystemView() {
		SystemController controller = new SystemController(this);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.printf("Error setting UI look and fell: %s%n", e.getMessage());
		}

		setSize(FRAME_SIZE, FRAME_SIZE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(true);

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		queryView = new QueryView();
		new QueryController(queryView, controller);
		container.add(queryView, BorderLayout.PAGE_START);

		tabPane = new JTabbedPane();
		container.add(tabPane, BorderLayout.CENTER);

		createMenus();

		setContentPane(container);

		controller.addListeners();
	}

	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);

		closeTabItem = new JMenuItem("Close tab");
		optionsMenu.add(closeTabItem);

		viewHistoryItem = new JMenuItem("View History");
		optionsMenu.add(viewHistoryItem);

		clearHistoryItem = new JMenuItem("Clear History");
		optionsMenu.add(clearHistoryItem);

		setJMenuBar(menuBar);
	}

	public FileDisplayTab getOpenFileTab(String file) {
		for (Component componenet : tabPane.getComponents()) {
			if (componenet instanceof FileDisplayTab) {
				FileDisplayTab tab = (FileDisplayTab) componenet;
				if (file.equals(tab.getFile())) {
					return tab;
				}
			}
		}
		return null;
	}

	public ResultTab getSearchResultTab(String queryString) {
		for (Component componenet : tabPane.getComponents()) {
			if (componenet instanceof ResultTab) {
				ResultTab tab = (ResultTab) componenet;
				if (queryString.equals(tab.getQueryString())) {
					return tab;
				}
			}
		}
		return null;
	}

	public HistoryTab getOpenHistoryTab() {
		for (Component componenet : tabPane.getComponents()) {
			if (componenet instanceof HistoryTab) {
				return (HistoryTab)componenet;
			}
		}
		return null;
	}

	public void setSelectedTab(Component component) {
		tabPane.setSelectedComponent(component);
	}

	public Component getSelectedTab() {
		return tabPane.getSelectedComponent();
	}

	public void addTab(String title, Component component) {
		tabPane.add(title, component);
		ensureCloseTabState();
	}

	public void closeTab(Component component) {
		tabPane.remove(component);
		ensureCloseTabState();
	}

	public void dialogNoTabs() {
		JOptionPane.showMessageDialog(this,
				"No tabs to close",
				"No tabs",
				JOptionPane.ERROR_MESSAGE);
	}

	public void ensureCloseTabState() {
		closeTabItem.setEnabled(tabPane.getComponentCount() > 0);
	}

	public void addCloseTabListener(ActionListener listener) {
		closeTabItem.addActionListener(listener);
	}

	public void addClearHistoryListener(ActionListener listener) {
		clearHistoryItem.addActionListener(listener);
	}

	public void addViewHistoryListener(ActionListener listener) {
		viewHistoryItem.addActionListener(listener);
	}

}
