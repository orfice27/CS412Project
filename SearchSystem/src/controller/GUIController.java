package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;

import view.GUI;
import view.Tab;

/**
 * This class controls all events related to the GUI including
 * button presses, changes made to the GUI and any mouse clicks.
 * It will then notify the GUI to update relative components with
 * the new results/content.
 */
public class GUIController implements ActionListener, ChangeListener, MouseListener{

	private int tabCounter;
	private GUI view;
	private ArrayList<SearchResult> results;
	private List<String> searchTerms;
	private List<Tab> tabList;
	private ArrayList<String> documents;
	private Searcher searcher;

	public GUIController(GUI gui){
		view = gui;
		results = new ArrayList<SearchResult>();
		searchTerms = new ArrayList<String>();
		tabCounter = 0;
		tabList = new ArrayList<Tab>();
		documents = new ArrayList<String>();
		searcher = new Searcher();
		try {
			searcher.index("data set" + File.separator + "rel200");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = view.getFrame();
		switch(e.getActionCommand()){
		case "query":	
			String query = view.getTxtpnSearchGui();
			// Run context searcher on query
			if (query.isEmpty()) {								// make sure the text // field isn't empty!
				JOptionPane.showMessageDialog(frame,
						"No query was entered.",
						"Empty query",
						JOptionPane.ERROR_MESSAGE);
			} else if (containsDigit(query) == true) {			// make sure the user didn't enter numbers in the text field
				JOptionPane.showMessageDialog(frame,
						"Numbers are not accepted. Try again.",
						"Invalid query",
						JOptionPane.ERROR_MESSAGE);
			} else if (isAlpha(query) == false) {				// check to see if query contains at least a letter (religious terms may use -)
				JOptionPane.showMessageDialog(frame,
						"Please enter a query with letters.",
						"Invalid query",
						JOptionPane.ERROR_MESSAGE);
			} else if (containsPunct(query) == true && containsValidPunctuation(query) == false) { // check for punctuation
					JOptionPane.showMessageDialog(frame,
							"Please enter a sensible query.",
							"Invalid query",
							JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					results = (ArrayList<SearchResult>) searcher.query(query); // store the results in an arraylist
				} catch (IOException | ParseException e1) {
					// TODO Auto-generated catch block
				}

				searchTerms.add(query); // add the term to a list so we can use again later

				if (results.isEmpty()) { // check to see if any results were returned for the query
					JOptionPane.showMessageDialog(frame, "No results were found");
				} else {
					/* create a new Tab object to store query related
					 * information Each tab contains a counter (the number of
					 * tab upon creation), the results for that query and the
					 * query term itself
					 */
					tabList.add(new Tab(tabCounter, results, query));
					// guiobject.printResults(tabList.get(tabList.size() - 1).getResults()); //this displays results to right pane
					view.insertNewTab(query, view.getResultsPanel(results), 0); // inserts a new tab with the query and results
					view.setCurrentSelection(0); // forces selection to 0 so that newest query is always the current tab
				}
			}

			tabCounter++; // keeps a counter of the number of tabs created so far
			break;

		/*
		 * remove this area to get rid of exception being thrown on console
		 * involves removing entire close tab function though
		 */
		case "closetab":
			System.out.println("close tab");
			if (tabList.size() == 0) {
				JOptionPane.showMessageDialog(frame,
						"No tabs to close",
						"No tabs",
						JOptionPane.ERROR_MESSAGE); // otherwise display an error dialogue
			} else {
				// find currently selected tab and delete this from GUI
				int selectedTab = view.getSelectedTab();
				if (selectedTab != -1) {
					view.removeTab(selectedTab);
					tabList.remove(selectedTab);
				} else {
					JOptionPane.showMessageDialog(frame, "All tabs closed");
				}
			}
			break;
		}

	}

	/**
	 * Just an internal method for checking numbers
	 * @param str - the string to be checked
	 * @return true if numbers exist else false
	 */
	private boolean containsDigit(String str) {
		if (str != null) {
			for (char c : str.toCharArray()) {
				if (Character.isDigit(c)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Just to see if string contains any letters
	 * @param str -string to be checked
	 * @return true if it contains a letter else false
	 */
	private boolean isAlpha(String str) {
		if (str != null) {
			for (char c : str.toCharArray()) {
				if (Character.isLetter(c)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Because religious documents may have - or ' in their names
	 * the system should accept these as proper queries
	 * @param str - the string to be checked
	 * @return true if characters are valid else false
	 */
	private boolean containsValidPunctuation(String str) {
		if (str != null) {
			for (char c : str.toCharArray()) {
				if (c == '-' || c == '\'') {
					return true;
				}
			}
		}
		return false;

	}

	private boolean containsPunct(String str){
		return Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(str).find();
	}

	/**
	 * The change listener's main method
	 * This checks to see if the tabbed panes state has been changed
	 * Aka, whether we have switched viewing to another tab so the
	 * system knows to update the file listing to the left
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		String title = view.getCurrentTabTitle();		// get the title of the current tab selected
		if (title.contains("txt")) {					// if its a file document (one we opened)
			view.setTabsPaneWithTitle(title);			// then only display document title on the results on left pane
		} else {
			for (Tab tab : tabList) {					// now we look through list of tabs
				if (tab.getTitle().equals(title)) {		// check the tabs title is equal to our current tab title
					documents = tab.getDocumentName();	// if it is then get the document names for that tab
				}
			}
			view.setTabsPaneWithStrings(documents);		// then display these document names to the results on the left pane
		}

	}

	/**
	 * The mouse listener's maid method for mouse clicks
	 * This checks for any clicks made by the user.
	 * The position of the click is converted into a document offset so we can extract
	 * the filename in which the user has clicked on the left.
	 * Then we open the file with the filename extracted by matching the
	 * name using a switch/case statement.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());			// creates a new Point object storing x/y coordinates
		String name = view.returnDocumentClicked(p);		// returns the document name at that point
		System.out.println(name);
		if (checkForFile(name)) {							// check if we already had file opened - no point reopening same file multiple times
			openTabWithName(name);							// if it's opened then just select tab
		} else {
			tabList.add(new Tab(tabCounter, name, name));	// add this tab to our tablist as its a new file we have not opened yet
			readFile(new File(name));						// pass this to an internal method to be opened
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Read the file specified
	 * --NOW MODIFIED TO PUBLIC FOR ACCESS IN SEARCHRESULTCONTROLLER
	 * @param file - file to be read
	 */

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
				view.returnTabViewer().add(text_comp, file.getName());
				view.returnTabViewer().setSelectedComponent(text_comp);
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
		int pnumber = view.returnIndexOfTabWithTitle(name);
		view.setCurrentSelection(pnumber);
	}
	
	public List<Tab> returnTabList(){
		return tabList;
	}
	
	public int returnTabCounter(){
		return tabCounter;
	}

}
