package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import view.GUI;
import view.Tab;
import model.SearchResult;

public class SearchResultController implements ActionListener {

	private SearchResult searchResult;
	GUI viewComponent;
	GUIController guiControl;
	List<Tab> tabList;
	int tabCounter;
	

	public SearchResultController(SearchResult searchResult, GUI view, GUIController control) {
		this.viewComponent = view;
		this.searchResult = searchResult;
		this.guiControl = control;
		tabList = guiControl.returnTabList();
		tabCounter = guiControl.returnTabCounter();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("opentext")){
			System.out.println(searchResult.getFileName());
			String name = searchResult.getFileName();
			if (checkForFile(name)) {							// check if we already had file opened - no point reopening same file multiple times
				openTabWithName(name);							// if it's opened then just select tab
			} else {
				tabList.add(new Tab(tabCounter, name, name));	// add this tab to our tablist as its a new file we have not opened yet
				readFile(new File(name));						// pass this to an internal method to be opened
			}
			
			
			}
		}
	
	public void readFile(File file) {
		if (file.canRead()) {
			try {
				FileInputStream input_file = new FileInputStream(file); // Read the contents of the file into a byte[] object.
				byte[] file_data = new byte[(int) file.length()];
				input_file.read(file_data);
				input_file.close();

				JTextArea text_area = new JTextArea(); // Create a text area to hold the contents of the file. 
				text_area.setEditable(false);
				text_area.insert(new String(file_data), 0);

				/*
				 * Create a scroll pane to hold the text area; add it to the
				 * tabbed pane with all the other previously loaded scroll
				 * panes; and make the new scroll pane the selected component.
				 */
				JScrollPane text_comp = new JScrollPane(text_area);
				viewComponent.returnTabViewer().add(text_comp, file.getName());
				viewComponent.returnTabViewer().setSelectedComponent(text_comp);
			} catch (FileNotFoundException ex) {
				JOptionPane.showMessageDialog(null,
						"Cannot find '" + file.getAbsolutePath() + "'",
						"Read Error",
						JOptionPane.ERROR_MESSAGE); // error message for when we can't find the file
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null,
						"Error reading from '" + file.getAbsolutePath() + "':" + ex.getMessage(),
						"Read Error",
						JOptionPane.ERROR_MESSAGE); // error message for when system is unable to read file
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Cannot read from file '" + file.getAbsolutePath() + "'",
					"Read Error",
					JOptionPane.ERROR_MESSAGE); // general error message if error occurs but is not caught by above two exceptions
		}

	}

	/**
	 * A small internal method just to check for the existence of an opened file
	 * Aka, has this file already been opened.
	 * It'll loop through our tabList which holds all newly created tabs
	 * and checks each's title to see if it matches the filename.
	 * The title of the tab is the filename
	 * The title of the tab is also the query (labeled as such for easier remembering)
	 * --NOW MODIFIED TO PUBLIC FOR ACCESS IN SEARCHRESULTCONTROLLER
	 * @param filename - the name of the file
	 * @return the value of existencechecker as an integer. 1 = existence found
	 */
	public boolean checkForFile(String filename) {
		for (Tab tab : tabList) {
			if (tab.getTitle().equals(filename)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Opens tab with given name
	 * Open = select. 
	 * This method will change the currently selected tab to one with the name given.
	 * @param name
	 */
	private void openTabWithName(String name) {
		int pnumber = viewComponent.returnIndexOfTabWithTitle(name);
		viewComponent.setCurrentSelection(pnumber);
	}

}

	


