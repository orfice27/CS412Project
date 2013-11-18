package ui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class GUI {

	private JFrame frame;

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
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 630, 507);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 16, 1, 345);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JTextPane txtpnSearchGui = new JTextPane();
		txtpnSearchGui.setBounds(12, 11, 169, 20);
		txtpnSearchGui.setText("Search GUI");
	
		frame.getContentPane().add(txtpnSearchGui);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(193, 6, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(313, 42, 303, 414);
		frame.getContentPane().add(textArea);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(13, 42, 273, 414);
		frame.getContentPane().add(textPane);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu1");
		menuBar.add(mnMenu);
		
		JMenuItem mntmMenuitem = new JMenuItem("MenuItem1");
		mnMenu.add(mntmMenuitem);
		
		JMenu mnMenu_1 = new JMenu("Menu2");
		menuBar.add(mnMenu_1);
		
		JMenuItem mntmMenuitem_1 = new JMenuItem("MenuItem2");
		mnMenu_1.add(mntmMenuitem_1);
	}
}
