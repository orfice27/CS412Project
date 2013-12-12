package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import searcher.Searcher;

public class GUIController implements ActionListener{

	private GUI guiobject;
	private ArrayList<SearchResult> results;
	private ArrayList<String> searchTerms;
	private int tabNumber;
	private String title;
	private ArrayList<Tab> tabList;
	public GUIController(GUI gui){
		guiobject = gui;
		results = new ArrayList<SearchResult>();
		searchTerms=new ArrayList<String>();
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
			tabList = new ArrayList<Tab>();
			searchTerms.add(query); //add the term to a list so we can use again later
			if(results.isEmpty()){
				System.out.println("empty results");

				
				JFrame frame = guiobject.getFrame();
				//custom title, error icon
				JOptionPane.showMessageDialog(frame,
					    "No results were found");
			} else {
			
			tabList.add(new Tab(tabList.size(), results, query));
	
			guiobject.printResults(tabList.get(tabList.size() - 1).getResults()); //this displays results to right pane
			
			guiobject.addNewTab(query, guiobject.printResults(results));
			
			
			
			
			//this adds a new tab for each query 
			guiobject.setTabsPane(results);
			//this ADDS document names to the eventually browsable left pane
			
			break;
			}
			}
			
			for (String s: searchTerms){
				System.out.println(s);
			}
			
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

}
