package ui;


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
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;


import wordAutocomplete.AutoCompleteTextField;


/**
 * A {@link Document} performing auto completion on the inserted text. This
 * document can be used on any {@link JTextComponent}.
 * <p>
 * The completion will only happen for inserts, that is, when characters are
 * typed. If characters are erased, no new completion is suggested until a new
 * character is typed.
 * 
 * @see CompletionService and AutoCompleteDocument classes
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * Modified for use in CS412 Lucene Search System Project by SeeMai Chan
 * @version 2.0.0
 */

public class GUI {

	private JFrame frame;
	private GUIController controller;
	private AutoCompleteTextField txtpnSearchGui;
	private	JTextArea resultsArea;
	private JTextPane tabPane;
	private JTabbedPane tabViewer;
	ArrayList<String> words;

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
	
	private void readAndPopulateDictionary(){
		
		String filePath = "C:\\Users\\SeeMai\\git\\CS412Project\\SearchSystem\\theologicaldictionary.txt.txt";
		File mainFile = new File(filePath);
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
		
		for(String s: words){
			txtpnSearchGui.addPossibility(s);
			//System.out.println(s);
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
		
		//tab viewer
				tabViewer = new JTabbedPane();

		JScrollPane documentScroll = new JScrollPane(tabPane, 
				   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane resultsScroll = new JScrollPane(tabViewer, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		

		

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


	
	public JTextArea printResults(ArrayList<SearchResult> r){
		
		JTextArea resultDisplayArea = new JTextArea();
		resultDisplayArea.setText("");
		//resultsPane.setText("");
		for(SearchResult s: r){
			resultDisplayArea.append("Results Found in "+s.getFileName()+" at "+s.getFilePath());
			resultDisplayArea.append("\n-----------------------------------------------------\n\n");
			for(String str : s.getResults()){
				resultDisplayArea.append(str+"\n");
			}
			
		}
		return resultDisplayArea;
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
	
	public void addNewTab(String title, JComponent component){
		tabViewer.addTab(title, component);
	}
	
	public JFrame getFrame(){
		return frame;
	}
	

}
