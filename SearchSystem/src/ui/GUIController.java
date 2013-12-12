package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import searcher.Searcher;

public class GUIController implements ActionListener, ChangeListener{

	private int tabCounter;
	private int index;
	private GUI guiobject;
	private ArrayList<SearchResult> results;
	private ArrayList<String> searchTerms;
	private int tabNumber;
	private String title;
	private ArrayList<Tab> tabList;
	private ArrayList<String> documents;
	
	
	public GUIController(GUI gui){
		guiobject = gui;
		results = new ArrayList<SearchResult>();
		searchTerms=new ArrayList<String>();
		tabCounter = 0;
		tabList = new ArrayList<Tab>();
		documents = new ArrayList<String>();
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
			
			Searcher searcher = new Searcher("C:\\Users\\SeeMai\\git\\CS412Project\\SearchSystem\\data set\\rel200",query);

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
	
			guiobject.printResults(tabList.get(tabList.size() - 1).getResults()); //this displays results to right pane
			
	
			guiobject.insertNewTab(query, guiobject.printResults(results),0);
			 
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
		
	}

}
