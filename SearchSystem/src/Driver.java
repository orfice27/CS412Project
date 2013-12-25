import java.awt.EventQueue;

import view.SearchSystemView;

public class Driver {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchSystemView view = new SearchSystemView();
					view.setVisible(true);
				} catch (Exception e) {
					System.err.printf("Error running search system%n");
				}
			}
		});
	}

}
