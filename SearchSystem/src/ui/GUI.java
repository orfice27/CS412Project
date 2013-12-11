package ui;


import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;


	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.JButton;
	import javax.swing.JSplitPane;
	import javax.swing.JTextField;
	import javax.swing.JTextPane;
	import javax.swing.JMenuBar;
	import javax.swing.JMenu;
	import javax.swing.JMenuItem;
	import javax.swing.text.Document;
	import javax.swing.text.JTextComponent;

import wordAutocomplete.AutoCompleteDocument;
import wordAutocomplete.CompletionService;

/**
 * A {@link Document} performing auto completion on the inserted text. This
 * document can be used on any {@link JTextComponent}.
 * <p>
 * The completion will only happen for inserts, that is, when characters are
 * typed. If characters are erased, no new completion is suggested until a new
 * character is typed.
 * 
 * @see CompletionService and AutoCompleteDocument classes
 * 
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * Modified for use in CS412 Lucene Search System Project by SeeMai Chan
 * @version 2.0.0
 */

public class GUI {

	private JFrame frame;
	private GUIController controller;
	private JTextField txtpnSearchGui;
	JTextPane resultsPane;
	JTextPane tabPane;
	String newline = "\n";

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
		
		
		controller = new GUIController(this);
		
		//Initialise frame
		frame = new JFrame();
		frame.setBounds(100, 100, 630, 507);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		
		//Create Jpanel, add it to contentpane
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		
		//Text field for query, triggers a search when enter is pressed
		txtpnSearchGui = new JTextField(40);
		txtpnSearchGui.setText("Search GUI");
        txtpnSearchGui.addActionListener(controller);
        txtpnSearchGui.setActionCommand("query");
        buttonPanel.add(txtpnSearchGui);		
		
		//Button to trigger query
		JButton button = new JButton("Query");
		buttonPanel.add(button);		
		button.setActionCommand("query");
		button.addActionListener(controller);		

		//Results Text Area
		resultsPane = new JTextPane(); //2
		resultsPane.setEditable(false);
		
		//TabPane
		tabPane = new JTextPane(); //1
		tabPane.setEditable(false);
		
		
		//Splits tabPane andd results pane
		JSplitPane textSplitPane= new JSplitPane(1,tabPane,resultsPane);
		textSplitPane.setDividerLocation(80);		
		//Splits buttonPanel and textSplitPanel vertically
		JSplitPane verticalPane= new JSplitPane(0,buttonPanel,textSplitPane);
		verticalPane.setEnabled(false);
		frame.add(verticalPane);
		
		//MenuBar to provide added functionality
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		//Creates a 'New' menu, with the ability to create a new query window
		JMenu mnMenu = new JMenu("New");
		menuBar.add(mnMenu);
		JMenuItem newQuery = new JMenuItem("New Query Window");
		mnMenu.add(newQuery);
		newQuery.setActionCommand("nwindow");
		newQuery.addActionListener(controller);
		
		JMenu mnMenu_1 = new JMenu("Options");
		menuBar.add(mnMenu_1);
		
		JMenuItem mntmMenuitem_1 = new JMenuItem("MenuItem2");
		mnMenu_1.add(mntmMenuitem_1);
		
		/*
		 * this onwards is the word autocompletion code
		 */
		
		 // Create the completion service.
        SearchService nameService = new SearchService();

       
        JTextField input = this.txtpnSearchGui;

        // Create the auto completing document model with a reference to the
        // service and the input field.
        Document autoCompleteDocument = new AutoCompleteDocument(nameService,
                input);

        // Set the auto completing document as the document model on our input
        // field.
        input.setDocument(autoCompleteDocument);
        
        
	}
	private static class SearchService implements CompletionService<String> {

        /** Our search data. */
        private List<String> data;

        /**
         * Create a new <code>SearchService</code> and populate it.
         * 
         */
        
        /*This is pretty much the "dictionary". You can add as many
         * as you want but I reckon the popular terms for religious 
         * texts is enough for our project - SeeMai
         * Also, it's case sensitive.
         * */
        public SearchService() {
            data = Arrays.asList("Quran", "Old Testament", "New Testament", "Testament",
                    "Mormon", "The Book Of Mormon");
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (String o : data) {
                b.append(o).append("\n");
            }
            return b.toString();
        }

        /** {@inheritDoc} */
        public String autoComplete(String startsWith) {
            // Naive implementation, but good enough for the sample
            String hit = null;
            for (String o : data) {
                if (o.startsWith(startsWith)) {
                    // CompletionService contract states that we only
                    // should return completion for unique hits.
                    if (hit == null) {
                        hit = o;
                    } else {
                        hit = null;
                        break;
                    }
                }
            }
            return hit;
        }

    }
	
	public String getTxtpnSearchGui() {
		return txtpnSearchGui.getText();
	}

	
	public void setTextArea(String s){
		this.resultsPane.setText(s);
	}
	
	public void setTextPane(String s){
		this.tabPane.setText(this.tabPane.getText()+s+"\n");
		
	}
	
	
}
