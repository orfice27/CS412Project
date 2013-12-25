import java.awt.EventQueue;

import view.SystemView;

public class Driver {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SystemView view = new SystemView();
					view.setVisible(true);
				} catch (Exception e) {
					System.err.printf("Error running search system%n");
				}
			}
		});
	}

}
