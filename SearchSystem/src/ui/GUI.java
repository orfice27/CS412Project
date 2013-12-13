package ui;


import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import model.SearchResult;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.Utilities;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import wordAutocomplete.AutoCompleteTextField;


/**
 *  
 * Front end of the GUI
 */

public class GUI {

	private JFrame frame;
	private GUIController controller;
	private AutoCompleteTextField txtpnSearchGui;
	private	JTextArea resultsArea;
	private JTextPane tabPane;
	private JTabbedPane tabViewer;
	ArrayList<String> words;
	
	JTextArea resultDisplayArea;
	String highlighterm;
	
    // An instance of the subclass of the default highlight painter
    MyHighlightPainter myHighlightPainter = new MyHighlightPainter(Color.yellow);
    
    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
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

		//Initialise frame
		frame = new JFrame();
		frame.setSize(800,800);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);


		//Create Jpanel, add it to contentpane
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));


		//Text field for query, triggers a search when enter is pressed
		txtpnSearchGui = new AutoCompleteTextField(60);
		
		//this loop goes through our list of dictionary terms and adds them as possibilities for autocomplete
		for(String s: words){
			txtpnSearchGui.addPossibility(s);
		}
		
		
		//txtpnSearchGui.setText("Search GUI");
		txtpnSearchGui.addActionListener(controller);
		txtpnSearchGui.setActionCommand("query");
		buttonPanel.add(txtpnSearchGui);		

		//Button to trigger query
		JButton button = new JButton("Query");
		buttonPanel.add(button);		
		button.setActionCommand("query");
		button.addActionListener(controller);	
		
		

		//Results Text Area
		resultsArea = new JTextArea(); //2
		resultsArea.setEditable(false);	
		
		
		
		tabPane= new JTextPane();
		tabPane.setEditable(false);
		tabPane.addMouseListener(controller);
		
		//tab viewer - The tabbed pane holder who does all tab related activities
		tabViewer = new JTabbedPane();

		JScrollPane documentScroll = new JScrollPane(tabPane, 
				   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane resultsScroll = new JScrollPane(tabViewer, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		/*a listener to check for changes made to the tabbedpane such as selecting a different tab
		 	this allows us to update the file lister to the left appropriately to match the correct
		 	query/tab being selected. */
		
		tabViewer.addChangeListener(controller);
		
		//Splits tabPane and results pane
		JSplitPane textSplitPane= new JSplitPane(1,documentScroll,resultsScroll);
		textSplitPane.setDividerLocation(120);		

		//Splits buttonPanel and textSplitPanel vertically, locks the divider in place
		JSplitPane verticalPane= new JSplitPane(0,buttonPanel,textSplitPane);
		verticalPane.setEnabled(false);
		frame.add(verticalPane);

		//MenuBar to provide added functionality
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		//Creates a 'New' menu, with the ability to create a new query window
		JMenu mnMenu = new JMenu("New");
		menuBar.add(mnMenu);
		JMenuItem newQuery = new JMenuItem("New Query Window");
		mnMenu.add(newQuery);
		newQuery.setActionCommand("nwindow");
		newQuery.addActionListener(controller);

		JMenu mnMenu_1 = new JMenu("Options");
		menuBar.add(mnMenu_1);

		JMenuItem mntmMenuitem_1 = new JMenuItem("MenuItem2");
		mnMenu_1.add(mntmMenuitem_1);
	}

		
	public String getTxtpnSearchGui() {
		return txtpnSearchGui.getText();
	}

	
	/**
	 * Takes a list of search results and formats them appropriately for display
	 * on the GUI. This includes parsing the contents to find where the contextual
	 * <highlight> tags are located and extract the terms referenced to be highlighted 
	 * in yellow. It then removes the <highlight> tags from the GUI display but
	 * keeps them in the original string from the results file.
	 * @param r - the list of searchresults to be displayed on the GUI
	 */
	
	public JTextArea printResults(ArrayList<SearchResult> r){
		
		
		resultDisplayArea = new JTextArea();
		resultDisplayArea.setText("");
		
		for(SearchResult s: r){
			resultDisplayArea.append("Results Found in "+s.getFileName()+" at "+s.getFilePath());
			resultDisplayArea.append("\n-----------------------------------------------------\n\n");
			
			
			//we loop through the results
			for(String str : s.getResults()){
				
				/*str contains the FULL string obtained from results
				 * this means it includes the <highlight> tags
				 * we need to extract everything BUT the highlight tags 
				 * then reformat this for GUI display
				 */
				
				highlighterm =""; //resetting the highlighterm variable so we don't get the last one we used for highlighting!
				String firstpart; //first part of the string (everything before the highlight tag)
				String secondpart; //second part of the string (everything after the highlight tag)
				
				//tokenize the line so we get individual "words"
				StringTokenizer st = new StringTokenizer(str);

				//for every token
				while (st.hasMoreTokens()) {
				//we extract the word
				String word = st.nextToken();
				
				
				//check if HIGHLIGHT exists within the word
				if(word.contains("highlight")){
					
					
					//if it does we extract the location of the > and / characters
					//these symbols represent the end of the start tag and the end of the end tag
					//<highlight>WORD</highlight>
					int startindex = word.indexOf('>');
					int endindex = word.indexOf('/');
					
					
					startindex = startindex+1; //we extract one place after the > start tag
					endindex = endindex-1; //and one place before the / end tag
					
					highlighterm = word.substring(startindex, endindex); //this creates the term to be highlighted
					
					highlight(resultDisplayArea, highlighterm); //this highlights the term
				}
				}
				
				/*
				 * Because our parsed religious texts still have some strange tags included,
				 * using a search on the > and / characters will end up removing/highlighting
				 * terms that are NOT supposed to be highlighted.
				 * The following section of code fixes this by recreating the string that
				 * is to be displayed on the GUI.
				 */
				
				
				int tagstart = str.indexOf("<highlight"); //returns the position of the < in this string
				int tagend = str.indexOf("/highlight>"); //returns the position of the / in this string
				
				
				firstpart= str.substring(0,tagstart); //first part of the string is from the start (0) until the < start
				secondpart=str.substring(tagend+11,str.length()); //second part of the string is +11 from tagend (/) until the end of the string
				
				
				String finalstring = firstpart + highlighterm +secondpart; //join all three parts to get the final string
			
				resultDisplayArea.append(finalstring+"\n"); //then display this string on the GUI
//				resultDisplayArea.append(str +"\n"); //this returns the ORIGINAL string (without the tag removal)
				
			}
			}
		return resultDisplayArea;
		}
	
	/**
	 * @return the display area object for the results
	 */
	
	public JTextArea returnDisplay(){
		return resultDisplayArea;
	}
	
	/**
	 * Sample obtained from: http://stackoverflow.com/questions/5674128/jtextpane-highlight-text
	 * This highlights the string pattern specified within a TextComponent
	 * @param textComp - the text component
	 * @param pattern - the string pattern to be highlighted
	 */
	// Creates highlights around all occurrences of pattern in textComp
	public void highlight(JTextComponent textComp, String pattern) {
		
		// First remove all old highlights
	    removeHighlights(textComp);

	try {
	    Highlighter hilite = textComp.getHighlighter();
	    Document doc = textComp.getDocument();
	    String text = doc.getText(0, doc.getLength());
	    int pos = 0;

	    // Search for pattern
	    // see I have updated now its not case sensitive 
	    while ((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >= 0) {
	        // Create highlighter using private painter and apply around pattern
	        hilite.addHighlight(pos, pos+pattern.length(), myHighlightPainter);
	        pos += pattern.length();
	    }
	} catch (BadLocationException e) {
	}
	}
	
	/**
	 * Sample obtained from: http://stackoverflow.com/questions/5674128/jtextpane-highlight-text
	 * This removes old highlights to avoid conflicts when rehighlighting a new set of results
	 * @param textComp - the text component to remove highlighting from
	 */
	
	public void removeHighlights(JTextComponent textComp) {
	    Highlighter hilite = textComp.getHighlighter();
	    Highlighter.Highlight[] hilites = hilite.getHighlights();
	    for (int i=0; i<hilites.length; i++) {
	    	if (hilites[i].getPainter() instanceof MyHighlightPainter) {
	        hilite.removeHighlight(hilites[i]);
	    	}	
	    }
	}
	
	
	/**
	 * Sets the text of the left text pane, the tab pane
	 * @param s Search Result to be added to the pane.
	 */
	public void setTabsPane(ArrayList<SearchResult> r){
		tabPane.setText("");
		for(SearchResult s: r){
			tabPane.setText(tabPane.getText()+s.getFileName()+"\n");
		}
	}
	
	/**
	 * Sets the text of the left text pane, the tab pane
	 * Exactly the same as above but with a list of strings instead
	 * @param s - the list of strings to be displayed
	 */
	
	public void setTabsPaneWithStrings(ArrayList<String> s){
		tabPane.setText("");
		for (String str : s){
			tabPane.setText(tabPane.getText() + str + "\n");
		}
	}
	
	/**
	 * Sets the text of the left text pane, the tab pane
	 * Exactly the same as above two but with a single string
	 * @param s - the single string to be displayed
	 */
	
	public void setTabsPaneWithTitle(String s){
		tabPane.setText(s);
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
		tabViewer.insertTab(title, null, component, null, position);
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
	 * @return the frame of the GUI 
	 */
	public JFrame getFrame(){
		return frame;
	}
	
	/**
	 * 
	 * @return the tabbed panes
	 */
	public JTabbedPane returnTabViewer(){
		return tabViewer;
	}
	
	/**
	 * This method takes a mouse point and converts this to an offset
	 * relative to the document model (the GUI).
	 * Using the offset, it then extracts the word displayed on the
	 * document using the getWordStart and getWordEnd methods.
	 * @param e - the location of the mouse
	 * @return the name of the document clicked
	 */
	public String returnDocumentClicked(Point e){
		
		int offset = tabPane.viewToModel(e);
		
		String text=null;
		try {
			int start = Utilities.getWordStart(tabPane, offset);
			int end = Utilities.getWordEnd(tabPane, offset);
			
			text = tabPane.getText(start, end-start);
			
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return text;
	}
	
	
	
	// A private subclass of the default highlight painter
	class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
	    public MyHighlightPainter(Color color) {
	        super(color);
	    }
	}
	

}
