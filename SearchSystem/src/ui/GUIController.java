package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTextArea;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import searcher.Searcher;

public class GUIController implements ActionListener{

	private GUI guiobject;
	private ArrayList<SearchResult> results;
	private ArrayList<String> searchTerms;
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
			String query = guiobject.getTxtpnSearchGui();

			Searcher searcher = new Searcher("C:\\Users\\SeeMai\\git\\CS412Project\\SearchSystem\\data set\\rel200",query);
			
			
			
		


			try {
				results = (ArrayList)searcher.search();
			} catch (IOException | ParseException
					| InvalidTokenOffsetsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			
			}
			
			guiobject.addNewTab(query, guiobject.printResults(results));
			searchTerms.add(query); //add the term to a list so we can use again later
			 //this displays query to right
			guiobject.setTabsPane(results); //this ADDS document names to the eventually browsable left pane
			
			

			break;
		case "nquery":
			//Create a new query view
			break;
		}
	}

}
