package ui;

import java.awt.Point;
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
	private int tabNumber;
	private String title;
	private ArrayList<Tab> tabList;
	private ArrayList<String> documents;
	private String newline;
	private ArrayList<String> fileContentsToDisplay;
	private int existencechecker;
	
	private String username;
	
	public GUIController(GUI gui){
		guiobject = gui;
		results = new ArrayList<SearchResult>();
		searchTerms=new ArrayList<String>();
		tabCounter = 0;
		tabList = new ArrayList<Tab>();
		documents = new ArrayList<String>();
		newline = "\n";
		fileContentsToDisplay = new ArrayList<String>();
		existencechecker = 0;
		username = "SeeMai";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "query":	
			//Run context searcher on query
		
			if(guiobject.getTxtpnSearchGui().isEmpty()){
				System.out.println("error");
				
				JFrame frame = guiobject.getFrame();
				//custom title, error icon
				JOptionPane.showMessageDialog(frame,
				    "No query was entered.",
				    "Empty query",
				    JOptionPane.ERROR_MESSAGE);
			} else if (containsDigit(guiobject.getTxtpnSearchGui())==true){
				System.out.println("number error");
				
				JFrame frame = guiobject.getFrame();
				//custom title, error icon
				JOptionPane.showMessageDialog(frame,
				    "Numbers are not accepted. Try again.",
				    "Invalid query",
				    JOptionPane.ERROR_MESSAGE);
			}else {
			
				
			
			String query = guiobject.getTxtpnSearchGui();
			
			
			
			//you need to change to the name here to work, so it points to your local dataset folder
			
			Searcher searcher = new Searcher("C:\\Users\\" + username + "\\git\\CS412Project\\SearchSystem\\data set\\rel200",query);

			try {
				results = (ArrayList)searcher.search();
			} catch (IOException | ParseException
					| InvalidTokenOffsetsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			searchTerms.add(query); //add the term to a list so we can use again later
			if(results.isEmpty()){
				System.out.println("empty results");

				
				JFrame frame = guiobject.getFrame();
				//custom title, error icon
				JOptionPane.showMessageDialog(frame,
					    "No results were found");
			} else {
			
			tabList.add(new Tab(tabCounter, results, query));
	
			guiobject.printResults(tabList.get(tabList.size() - 1).getResults(),query); //this displays results to right pane
			
	
			guiobject.insertNewTab(query, guiobject.printResults(results, query),0);
			 
			guiobject.setCurrentSelection(0);
			}
			
			
			for (Tab t:tabList)
			{
				System.out.println("tab no: " + t.getTabLocation() + " tab results: " + t.getDocumentName() + " tab title: " + t.getTitle() );
				System.out.println("Current tab title: " + guiobject.getCurrentTabTitle());
			}
			
			
			//this adds a new tab for each query 
			guiobject.setTabsPane(results);
			//this ADDS document names to the eventually browsable left pane
			}
			
			for (String s: searchTerms){
				//System.out.println(s);
			}
			tabCounter++;
			
			break;
			
			
			
	
			
		case "nquery":
			//Create a new query view
			break;
	}
		
	}
	
	//just an internal method for checking numbers
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

	@Override
	public void stateChanged(ChangeEvent arg0) {
		//System.out.println("tab changed");
		
		//get the title of the current tab selected
		String title = guiobject.getCurrentTabTitle();
		
		//if its an opened document, just make the section blank
		if(title.contains("txt")){
			guiobject.setTabsPaneWithTitle(title);
		} else {
		
		//now we look through list of tabs
		for (int i=0; i<tabList.size(); i++){
			Tab tabtocheck = tabList.get(i);
			if(tabtocheck.getTitle().equals(title)){
				index = i;
				documents = tabtocheck.getDocumentName();
			}
		}
		
		System.out.println("Tab index selected: " + index);
		
		guiobject.setTabsPaneWithStrings(documents);
		JTextArea area = guiobject.resultDisplayArea;
		//guiobject.rehighlightarea(area,title);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouse clicked");
		Point p = new Point(e.getX(), e.getY());
		String name = guiobject.returnDocumentClicked(p);
		System.out.println("name: " + name);
		
		//check string name against filenames and open relevant document
		switch(name){
		
		case "quran.xml.txt":
			
			int checker =0;
			
			//do stuff
			
			System.out.println("Open Quran");
			
			//check if we already had file opened - no point reopening
			checker = checkForFile("quran.xml.txt");
			
			if(checker == 1){
				//if so then just select tab
				openTabWithName("quran.xml.txt");
				
			} else {
			
			//add this tab to our tablist
			tabList.add(new Tab(tabCounter, name, name));
			
			//now we read contents of file into a new jtextpane
			//for testing purposes the file is too large to be read so I have included a snippet of the original in place
			//you still need to change the name to load it though
			File filetoread = new File("C:\\Users\\" + username + "\\git\\CS412Project\\SearchSystem\\quran.xml.txt");
			
		    
			readAFile(filetoread);      
			    
			
			}  
			
			break;
			
		case "nt.xml.txt":
			
			
			System.out.println("Open New Testament");
			
			int checkerNT =0;
			
			//check if we already had file opened - no point reopening
			checkerNT = checkForFile("nt.xml.txt");
			
			if(checkerNT == 1){
				//if so then just select tab
				openTabWithName("nt.xml.txt");
				
			} else {
			
			//add this tab to our tablist
			tabList.add(new Tab(tabCounter, name, name));
			
			//now we read contents of file into a new jtextpane
			//for testing purposes the file is too large to be read so I have included a snippet of the original in place
			//you still need to change the name to load it though
			File filetoread = new File("C:\\Users\\" + username + "\\git\\CS412Project\\SearchSystem\\nt.xml.txt");
			
		    
			readAFile(filetoread);      
			    
			
			} 
			
			break;
			
			case "ot.xml.txt":
			
			
			System.out.println("Open Old Testament");
			
			int checkerOT =0;
			
			//check if we already had file opened - no point reopening
			checkerOT = checkForFile("ot.xml.txt");
			
			if(checkerOT == 1){
				//if so then just select tab
				openTabWithName("ot.xml.txt");
				
			} else {
			
			//add this tab to our tablist
			tabList.add(new Tab(tabCounter, name, name));
			
			//now we read contents of file into a new jtextpane
			//for testing purposes the file is too large to be read so I have included a snippet of the original in place
			//you still need to change the name to load it though
			File filetoread = new File("C:\\Users\\" + username + "\\git\\CS412Project\\SearchSystem\\ot.xml.txt");
			
		    
			readAFile(filetoread);      
			    
			
			} 
			
			break;
			
			case "bom.xml.txt":
				
				
				System.out.println("Open BOOK OF MORMON");
				
				int checkerBOM =0;
				
				//check if we already had file opened - no point reopening
				checkerBOM = checkForFile("bom.xml.txt");
				
				if(checkerBOM == 1){
					//if so then just select tab
					openTabWithName("bom.xml.txt");
					
				} else {
				
				//add this tab to our tablist
				tabList.add(new Tab(tabCounter, name, name));
				
				//now we read contents of file into a new jtextpane
				//for testing purposes the file is too large to be read so I have included a snippet of the original in place
				//you still need to change the name to load it though
				
				File filetoread = new File("C:\\Users\\" + username + "\\git\\CS412Project\\SearchSystem\\bom.xml.txt");
				
			    
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
	
	private void readAFile(File filetoread){
		 if ( filetoread.canRead() )                                     
         {
            try
            {
               // Read the contents of the file into a byte[]
             // object.
               FileInputStream input_file =
                 new FileInputStream(filetoread);
               byte[] file_data = new byte[(int) filetoread.length()];
               input_file.read(file_data);                         
 
               // Create a text area to hold the contents of the
               // file.
               JTextArea text_area = new JTextArea();              
               text_area.setEditable(false);
               text_area.insert(new String(file_data), 0);
   
               // Create a scroll pane to hold the text area; add
               // it to the tabbed pane with all the other
               // previously loaded scroll panes; and make the new
             // scroll pane the selected component.
               JScrollPane text_comp = new JScrollPane(text_area); 
               guiobject.returnTabViewer().add(text_comp, filetoread.getName());
               guiobject.returnTabViewer().setSelectedComponent(text_comp);
   
        
            }
            catch (java.io.FileNotFoundException ex)
          {
               JOptionPane.showMessageDialog(
                  null,
                  "Cannot find '" + filetoread.getAbsolutePath() + "'",
                  "Read Error", JOptionPane.ERROR_MESSAGE
             );
            }
            catch (java.io.IOException ex)
            {
               JOptionPane.showMessageDialog(
                null,
                  "Error reading from '" + filetoread.getAbsolutePath() +
                     "':" + ex.getMessage(),
                  "Read Error", JOptionPane.ERROR_MESSAGE
               );
          }
         }
         else
         {
            JOptionPane.showMessageDialog(
             null,
               "Cannot read from file '" +
                  filetoread.getAbsolutePath() + "'",
               "Read Error", JOptionPane.ERROR_MESSAGE
            );
       }
      
	}
	
	private int checkForFile(String filename){
		
		existencechecker=0;
		
		for (int i=0; i<tabList.size();i++){
			if(tabList.get(i).getTitle().equals(filename)){
				existencechecker=1;
				System.out.println("ALREADY EXISTS");
				
		} else {
			System.out.println("DOES NOT EXIST YET");
		}
		}
		
		return existencechecker;
	}
	
	private void openTabWithName(String name){
	
	
		System.out.println("existence: " + existencechecker);
		int pnumber = guiobject.returnIndexOfTabWithTitle(name);
		System.out.println("Tab no: " + pnumber);
		guiobject.setCurrentSelection(pnumber);
	}
	

}
