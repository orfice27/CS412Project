package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIController implements ActionListener{
	
	private GUI guiobject;
	
	public GUIController(GUI gui){
		guiobject = gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "query":	
				//Run context searcher on query
				System.out.println("triggered");
				String query = guiobject.getTxtpnSearchGui();
				System.out.println("Query: " + query);
				break;
			case "nquery":
				//Create a new query view
			break;
		}
	}

}
