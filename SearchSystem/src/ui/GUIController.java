package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUIController implements ActionListener{
	
	private GUI guiobject;
	ArrayList<String> searchTerms;
	
	public GUIController(GUI gui){
		guiobject = gui;
		searchTerms = new ArrayList<String>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "query":	
				//Run context searcher on query
				System.out.println("triggered");
				String query = guiobject.getTxtpnSearchGui();
				System.out.println("Query: " + query);
				
				
				searchTerms.add(query); //add the term to a list so we can use again later
				
				
				guiobject.setTextArea(query); //this displays query to right
				System.out.println(searchTerms);
				
				guiobject.setTextPane(query); //this ADDS query to existing list on left
				
				
				break;
			case "nquery":
				//Create a new query view
			break;
		}
	}

}
