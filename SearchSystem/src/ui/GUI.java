package ui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUI {

	private JFrame frame;
	private GUIController controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		controller = new GUIController();
		
		//Initialise frame
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 630, 507);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Create Jpanel, add it to contentpane
		JPanel panel = new JPanel();
		panel.setBounds(0, 16, 1, 345);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		
		//Text field for query, triggers a search when enter is pressed
		JTextField txtpnSearchGui = new JTextField();
		txtpnSearchGui.setBounds(12, 11, 169, 20);
		txtpnSearchGui.setText("Search GUI");
		txtpnSearchGui.addActionListener(controller);
		txtpnSearchGui.setActionCommand("query");
		frame.getContentPane().add(txtpnSearchGui);
		
		
		//Button to trigger query
		JButton button = new JButton("Query");
		button.setBounds(193, 6, 117, 29);
		frame.getContentPane().add(button);		
		button.setActionCommand("query");
		button.addActionListener(controller);
		
		
		//Results Text Area
		JTextPane textArea = new JTextPane();
		textArea.setEditable(false);
		textArea.setBounds(313, 42, 303, 414);
		frame.getContentPane().add(textArea);
		
		//
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(13, 42, 273, 414);
		frame.getContentPane().add(textPane);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("New");
		menuBar.add(mnMenu);
		
		JMenuItem newQuery = new JMenuItem("New Query");
		mnMenu.add(newQuery);
		newQuery.setActionCommand("nquery");
		newQuery.addActionListener(controller);
		
		JMenu mnMenu_1 = new JMenu("Options");
		menuBar.add(mnMenu_1);
		
		JMenuItem mntmMenuitem_1 = new JMenuItem("MenuItem2");
		mnMenu_1.add(mntmMenuitem_1);
	}
}
