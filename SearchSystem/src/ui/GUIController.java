package ui;

import java.awt.Point;

/**
 * This class controls all events related to the GUI including
 * button presses, changes made to the GUI and any mouse clicks.
 * It will then notify the GUI to update relative components with
 * the new results/content.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import searcher.Searcher;

public class GUIController implements ActionListener, ChangeListener, MouseListener{

	private int tabCounter;
	private int index;
	private GUI guiobject;
	private ArrayList<SearchResult> results;
	private ArrayList<String> searchTerms;
	private ArrayList<Tab> tabList;
	private ArrayList<String> documents;
	private int existencechecker;
	
	public GUIController(GUI gui){
		guiobject = gui;
		results = new ArrayList<SearchResult>();
		searchTerms=new ArrayList<String>();
		tabCounter = 0;
		tabList = new ArrayList<Tab>();
		documents = new ArrayList<String>();
		existencechecker = 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "query":	
			
			//Run context searcher on query
			if(guiobject.getTxtpnSearchGui().isEmpty()){ //make sure the text field isn't empty!
				
				JFrame frame = guiobject.getFrame();
				JOptionPane.showMessageDialog(frame,
				    "No query was entered.",
				    "Empty query",
				    JOptionPane.ERROR_MESSAGE); //otherwise display an error dialogue
				
				
			} else if (containsDigit(guiobject.getTxtpnSearchGui())==true){ //make sure the user didn't enter numbers in the text field
				
				JFrame frame = guiobject.getFrame();
				JOptionPane.showMessageDialog(frame,
				    "Numbers are not accepted. Try again.",
				    "Invalid query",
				    JOptionPane.ERROR_MESSAGE); //otherwise display an error dialogue
				
			} else if (isAlpha(guiobject.getTxtpnSearchGui())==false){ //check to see if query contains at least a letter (religious terms may use -)
				
				JFrame frame = guiobject.getFrame();
				JOptionPane.showMessageDialog(frame,
				    "Please enter a query with letters.",
				    "Invalid query",
				    JOptionPane.ERROR_MESSAGE); //otherwise display an error dialogue
				
			} else if(containsPunct(guiobject.getTxtpnSearchGui()) == true) { //check for punctuation
				
				
				if (containsValidPunctuation(guiobject.getTxtpnSearchGui()) == false){ //then check for dodgy punctuation
				
				JFrame frame = guiobject.getFrame();
				JOptionPane.showMessageDialog(frame,
					"Please enter a sensible query.",
					"Invalid query",
					JOptionPane.ERROR_MESSAGE); //otherwise display an error dialogue
					
			} 
			}else {
			
			String query = guiobject.getTxtpnSearchGui(); //query has passed our checks to extract query from the text field
			
			//pass the query into searcher
			Searcher searcher = new Searcher("data set" + File.separator + "rel200", query);

			try {
				
				results = (ArrayList)searcher.search(); //store the results in an arraylist
			} catch (IOException | ParseException
					| InvalidTokenOffsetsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			searchTerms.add(query); //add the term to a list so we can use again later
			
			
			if(results.isEmpty()){ //check to see if any results were returned for the query
				JFrame frame = guiobject.getFrame();
				JOptionPane.showMessageDialog(frame,
					    "No results were found"); //return a message if there were no results found
			} else {
			
				
			/*create a new Tab object to store query related information
			 * Each tab contains a counter (the number of tab upon creation), 
			 * the results for that query 
			 * and the query term itself
			 */
			tabList.add(new Tab(tabCounter, results, query)); 
			//guiobject.printResults(tabList.get(tabList.size() - 1).getResults()); //this displays results to right pane
			
			
			guiobject.insertNewTab(query, guiobject.printResults(results),0); //inserts a new tab with the query and results
			guiobject.setCurrentSelection(0); //forces selection to 0 so that newest query is always the current tab
			}
			
			}
			
			tabCounter++; //keeps a counter of the number of tabs created so far
			
			break;
			
		case "nquery":
			//Create a new query view
			break;
	}
		
	}
	
	/**
	 * Just an internal method for checking numbers
	 * @param s - the string to be checked
	 * @return true if numbers exist else false
	 */
	private boolean containsDigit(String s){  
	    boolean containsDigit = false;

	    if(s != null){
	        for(char c : s.toCharArray()){
	            if(containsDigit = Character.isDigit(c)){
	                break;
	            }
	        }
	    }

	    return containsDigit;
	}
	
	/**
	 * Just to see if string contains any letters
	 * @param str -string to be checked
	 * @return true if it contains a letter else false
	 */
	private boolean isAlpha(String str) {
	    char[] chars = str.toCharArray();

	    for (char c :chars) {
	        if(Character.isLetter(c)) {
	            return true;
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
	private boolean containsValidPunctuation(String str){
		char [] chars = str.toCharArray();
		
		for (char c :chars) {
	        if((c=='-') || (c=='\'')) {
	            return true;
	        }
	    }

	    return false;
		
	}
	
	/**
	 * 
	 */
	
	private boolean containsPunct(String s){
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(s);
		boolean b = m.find();
		return b;
	}

	/**
	 * The change listener's main method
	 * This checks to see if the tabbed panes state has been changed
	 * Aka, whether we have switched viewing to another tab so the
	 * system knows to update the file listing to the left
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		
		String title = guiobject.getCurrentTabTitle(); //get the title of the current tab selected
		
		if(title.contains("txt")){ //if its a file document (one we opened)
			guiobject.setTabsPaneWithTitle(title); //then only display document title on the results on left pane
		} else {
			
		for (int i=0; i<tabList.size(); i++){ //now we look through list of tabs
			Tab tabtocheck = tabList.get(i); //get the first tab in the list
			if(tabtocheck.getTitle().equals(title)){ //check the tabs title is equal to our current tab title
				documents = tabtocheck.getDocumentName(); //if it is then get the document names for that tab
			}
		}
		guiobject.setTabsPaneWithStrings(documents); //then display these document names to the results on the left pane
		
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
		
		Point p = new Point(e.getX(), e.getY()); //creates a new Point object storing x/y coordinates
		String name = guiobject.returnDocumentClicked(p); //returns the document name at that point
		
		switch(name){ //check string name against filenames and open relevant document
		
		case "quran.xml.txt":
			
			int checker = 0;
			
			checker = checkForFile("quran.xml.txt"); //check if we already had file opened - no point reopening same file multiple times
			
			if(checker == 1){
				openTabWithName("quran.xml.txt"); //if it's opened then just select tab
			} else {
			
			tabList.add(new Tab(tabCounter, name, name)); //add this tab to our tablist as its a new file we have not opened yet
			File filetoread = new File("quran.xml.txt"); //create a file object with the document name
			readAFile(filetoread); //pass this to an internal method to be opened
			}  
			break;
			
		case "nt.xml.txt":
			
			int checkerNT = 0;
			
			checkerNT = checkForFile("nt.xml.txt");
			
			if(checkerNT == 1){
				openTabWithName("nt.xml.txt");
			} else {
			
			tabList.add(new Tab(tabCounter, name, name));
			File filetoread = new File("nt.xml.txt"); 
			readAFile(filetoread);      
			} 
			
			break;
			
		case "ot.xml.txt":
			
			int checkerOT = 0;
			
			checkerOT = checkForFile("ot.xml.txt");
			
			if(checkerOT == 1){
				openTabWithName("ot.xml.txt");
			} else {
			
			tabList.add(new Tab(tabCounter, name, name));
			File filetoread = new File("ot.xml.txt");
			readAFile(filetoread);      
			} 
			
			break;
			
		case "bom.xml.txt":
			
			int checkerBOM =0;
				
			checkerBOM = checkForFile("bom.xml.txt");
				
			if(checkerBOM == 1){
				openTabWithName("bom.xml.txt");
			} else {
				
			tabList.add(new Tab(tabCounter, name, name));
			File filetoread = new File("bom.xml.txt");
			readAFile(filetoread);  
			} 
				
			break;
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
	 * 
	 * @param filetoread
	 */
	private void readAFile(File filetoread){
		 if ( filetoread.canRead() )                                     
         {
            try
            {
               
               FileInputStream input_file = 
                 new FileInputStream(filetoread); // Read the contents of the file into a byte[] object.
               byte[] file_data = new byte[(int) filetoread.length()];
               input_file.read(file_data);                         
 
               
               JTextArea text_area = new JTextArea(); // Create a text area to hold the contents of the file.          
               text_area.setEditable(false);
               text_area.insert(new String(file_data), 0);
   
               /* Create a scroll pane to hold the text area; add it to the tabbed pane with all the other
             	previously loaded scroll panes; and make the new
             	scroll pane the selected component. */
               
               JScrollPane text_comp = new JScrollPane(text_area); 
               guiobject.returnTabViewer().add(text_comp, filetoread.getName());
               guiobject.returnTabViewer().setSelectedComponent(text_comp);
   
        
            }
            catch (java.io.FileNotFoundException ex)
          {
               JOptionPane.showMessageDialog(
                  null,
                  "Cannot find '" + filetoread.getAbsolutePath() + "'",
                  "Read Error", JOptionPane.ERROR_MESSAGE //error message for when we can't find the file
             );
            }
            catch (java.io.IOException ex)
            {
               JOptionPane.showMessageDialog(
                null,
                  "Error reading from '" + filetoread.getAbsolutePath() +
                     "':" + ex.getMessage(),
                  "Read Error", JOptionPane.ERROR_MESSAGE //error message for when system is unable to read file
               );
          }
         }
         else
         {
            JOptionPane.showMessageDialog(
             null,
               "Cannot read from file '" +
                  filetoread.getAbsolutePath() + "'",
               "Read Error", JOptionPane.ERROR_MESSAGE //general error message if error occurs but is not caught by above two exceptions
            );
       }
      
	}
	
	/**
	 * A small internal method just to check for the existence of an opened file
	 * Aka, has this file already been opened.
	 * It'll loop through our tabList which holds all newly created tabs
	 * and checks each's title to see if it matches the filename.
	 * The title of the tab is the filename
	 * The title of the tab is also the query (labeled as such for easier remembering)
	 * @param filename - the name of the file
	 * @return the value of existencechecker as an integer. 1 = existence found
	 */
	private int checkForFile(String filename){
		
		existencechecker=0;
		
		for (int i=0; i<tabList.size();i++){
			if(tabList.get(i).getTitle().equals(filename)){
				existencechecker=1;
			}
		}
		return existencechecker;
	}
	
	/**
	 * Opens tab with given name
	 * Open = select. 
	 * This method will change the currently selected tab to one with the name given.
	 * @param name
	 */
	private void openTabWithName(String name){
	
		int pnumber = guiobject.returnIndexOfTabWithTitle(name);
		guiobject.setCurrentSelection(pnumber);
	}
	

}
