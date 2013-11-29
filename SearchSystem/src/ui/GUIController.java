package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIController implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "query":	
				//Run context searcher on query
				System.out.println("triggered");
				break;
			case "nquery":
				//Create a new query view
			break;
		}
	}

}
