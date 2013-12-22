package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.SearchResult;
import controller.GUIController;


/**
 *  
 * Front end of the GUI
 */
@SuppressWarnings("serial")
public class GUI extends JFrame {

	private GUIController controller;
	private AutoCompleteTextField queryTextField;
	private JTabbedPane tabViewer;
	ArrayList<String> words;
	String highlighterm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		readAndPopulateDictionary();
		initialize();
	}

	/**
	 * Reads the parsed dictionary file and adds all terms to the words arraylist for word autocompletion.
	 * 
	 */
	private void readAndPopulateDictionary(){
		
		//The file
		File mainFile = new File("theologicaldictionary.txt.txt");
		
		
		words = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(mainFile));
			String line;		
			while((line=br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line);

				//for every token
				while (st.hasMoreTokens()) {
				//we extract the word
				String word = st.nextToken();
				//add the word to the list
				words.add(word);
				
				} 
			}
			br.close(); 
		}
			catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		//Reads the XML file
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//these are extra words not included in the dictionary but I figured we needed as they're the names of our documents
		words.addAll(Arrays.asList("Quran", "Old Testament", "New Testament", "Testament",
					"Mormon", "The Book Of Mormon"));
		}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}


		controller = new GUIController(this);

		// Initialise frame
		setSize(800,800);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		// query input panel
		JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		queryPanel.setOpaque(true);
		queryTextField = new AutoCompleteTextField(60);

		// this loop goes through our list of dictionary terms and adds them as possibilities for autocomplete
		for(String s: words){
			queryTextField.addPossibility(s);
		}

		queryTextField.addActionListener(controller);
		queryTextField.setActionCommand("query");
		queryPanel.add(queryTextField);

		// Button to trigger query
		JButton queryButton = new JButton("Query");
		queryButton.setActionCommand("query");
		queryButton.addActionListener(controller);
		queryPanel.add(queryButton);
		
		container.add(queryPanel, BorderLayout.PAGE_START);


		//tab viewer - The tabbed pane holder who does all tab related activities
		tabViewer = new JTabbedPane();
		container.add(tabViewer, BorderLayout.CENTER);

		//MenuBar to provide added functionality
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		//Creates a 'New' menu, with the ability to create a new query window
		JMenu mnMenu = new JMenu("New");
		menuBar.add(mnMenu);
		JMenuItem newQuery = new JMenuItem("New Query Window");
		mnMenu.add(newQuery);
		newQuery.setActionCommand("nwindow");
		newQuery.addActionListener(controller);

		JMenu mnMenu_1 = new JMenu("Options");
		menuBar.add(mnMenu_1);

		JMenuItem mntmMenuitem_1 = new JMenuItem("Close tab");
		mntmMenuitem_1.setActionCommand("closetab");
		mntmMenuitem_1.addActionListener(controller);
		mnMenu_1.add(mntmMenuitem_1);
		
		setContentPane(container);
	}

	public String getQueryString() {
		return queryTextField.getText();
	}

	/**
	 * Takes a list of search results and formats them appropriately for display
	 * on the GUI. This includes parsing the contents to find where the contextual
	 * <highlight> tags are located and extract the terms referenced to be highlighted 
	 * in yellow. It then removes the <highlight> tags from the GUI display but
	 * keeps them in the original string from the results file.
	 * @param results - the list of searchresults to be displayed on the GUI
	 */
	public JPanel getResultsPanel(List<SearchResult> results) {
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.PAGE_AXIS));
		JComponent searchResultView;
		for (SearchResult result : results) {
			searchResultView = new SearchResultView(result,this, controller);
			resultsPanel.add(searchResultView);
		}
		return resultsPanel;
	}

	/**
	 * Returns the index number of the tab with the title specified
	 * @param s - the title
	 * @return - the tab number in which the tab has title specified
	 */
	public int returnIndexOfTabWithTitle(String s){
		int number = tabViewer.indexOfTab(s);
		
		return number;
	}

	/**
	 * Adds a new tab with with title and component specified
	 * @param title - the title of the tab
	 * @param component - the component to be added
	 */
	public void addNewTab(String title, JComponent component){
		tabViewer.addTab(title, component);
	}

	/**
	 * Insert a new tab on the tabbed pane at the specified position with provided title and component
	 * @param title - title of the tab
	 * @param component - component to be added
	 * @param position - position to be added at
	 */
	public void insertNewTab(String title, JComponent component, int position){
		JScrollPane pane = new JScrollPane(component, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabViewer.insertTab(title, null, pane, null, position);
	}

	/**
	 * 
	 * @return the number of tabs 
	 */
	public int getTabSize (){
		return tabViewer.getTabCount();
	}

	/**
	 * Select the current tab at given position (number)
	 * @param position - the tab to be selected
	 */
	public void setCurrentSelection(int position){
		tabViewer.setSelectedIndex(position);
	}

	/**
	 * Get the title of the current tab selected
	 * @return the title of the tab
	 */
	public String getCurrentTabTitle(){
		int selection = tabViewer.getSelectedIndex();
		String s = tabViewer.getTitleAt(selection);
		return s;
	}

	/**
	 * 
	 * @return the index value of the current tab selected
	 */
	public int getSelectedTab(){
		int selection = tabViewer.getSelectedIndex();
		return selection;
	}

	/**
	 * removes a tab at specified index
	 * @param index - number of tab to be removed
	 */
	public void removeTab(int index){
		tabViewer.remove(index);
	}

	/**
	 * 
	 * @return the tabbed panes
	 */
	public JTabbedPane returnTabViewer(){
		return tabViewer;
	}

}
