package view;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.SearchQueryController;
import controller.SearchSystemController;

public class SearchSystemView extends JFrame {

	private static final long serialVersionUID = -6488174892752959409L;

	private static final int FRAME_SIZE = 800;

	private SearchQueryView searchQueryView;
	private JTabbedPane tabPane;

	public SearchSystemView() {
		SearchSystemController controller = new SearchSystemController(this);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.printf("Error setting UI look and fell: %s%n", e.getMessage());
		}

		setSize(FRAME_SIZE, FRAME_SIZE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		searchQueryView = new SearchQueryView();
		new SearchQueryController(searchQueryView, controller);
		container.add(searchQueryView, BorderLayout.PAGE_START);

		tabPane = new JTabbedPane();
		container.add(tabPane, BorderLayout.CENTER);

		setContentPane(container);
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

	public SearchResultTab getSearchResultTab(String queryString) {
		for (Component componenet : tabPane.getComponents()) {
			if (componenet instanceof SearchResultTab) {
				SearchResultTab tab = (SearchResultTab) componenet;
				if (queryString.equals(tab.getQueryString())) {
					return tab;
				}
			}
		}
		return null;
	}

	public void setSelectedTab(Component component) {
		tabPane.setSelectedComponent(component);
	}

	public void addTab(String title, Component component) {
		tabPane.add(title, component);
	}

}
