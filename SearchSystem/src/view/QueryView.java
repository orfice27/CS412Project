package view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Panel to allow user input of a query and initiation of search on the index
 */
public class QueryView extends JPanel {

	private static final long serialVersionUID = -8381053061395185964L;

	private static final int INPUT_FIELD_SIZE = 50;

	private AutoCompleteTextField inputField;
	private JButton submitButton;

	public QueryView() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setOpaque(true);
		createInputField();
		createSubmitButton();
	}

	private void createInputField() {
		inputField = new AutoCompleteTextField(INPUT_FIELD_SIZE);
		add(inputField);
	}

	private void createSubmitButton() {
		submitButton = new JButton("Query");
		add(submitButton);
	}

	public String getQueryString() {
		return inputField.getText();
	}

	public void addAutoComplete(String possibility) {
		inputField.addPossibility(possibility);
	}

	public void dialogQueryEmpty() {
		JOptionPane.showMessageDialog(this,
				"No query was entered.",
				"Empty query",
				JOptionPane.ERROR_MESSAGE);
	}

	public void dialogQueryLetters() {
		JOptionPane.showMessageDialog(this,
				"Please enter a query with letters.",
				"Invalid query",
				JOptionPane.ERROR_MESSAGE);
	}

	public void dialogNoResults() {
		JOptionPane.showMessageDialog(this,
				"No results were found");
	}

	public void addSubmitQueryListener(ActionListener listener) {
		inputField.addActionListener(listener);
		submitButton.addActionListener(listener);
	}

}
