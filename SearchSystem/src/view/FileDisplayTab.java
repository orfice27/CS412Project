package view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import model.Result;

/**
 * Tab for displaying a full file from the index
 */
public class FileDisplayTab extends JScrollPane {

	private static final long serialVersionUID = -8170495304477233177L;

	private String fileName;
	private JPanel container;
	private JTextArea textArea;
	private String fileContent;

	public FileDisplayTab(String fileName, List<String> resultsList) {
		this.fileName = fileName;
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		setViewportView(container);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		createTextView();
	}

	private void createTextView() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		try {
			fileContent = new String(readFile());
			textArea.insert(fileContent, 0);
		} catch (IOException e) {
			System.err.printf("Error reading file %s%n", fileName);
			textArea.insert("Error reading file", 0);
		}
		container.add(textArea);
	}

	private byte[] readFile() throws IOException {
		return Files.readAllBytes(Paths.get(fileName));
	}

	public String getFile() {
		return fileName;
	}

	public void highlightResults(Result result, int selectedResultIndex) {
		Highlighter highlighter = textArea.getHighlighter();
		highlighter.removeAllHighlights();
		for (int i = 0; i < result.getResults().size(); i++) {
			String r = result.getResults().get(i).replaceAll("<highlight>", "").replaceAll("</highlight>", "");
			int position = 0;
			while ((position = fileContent.indexOf(r, position)) >= 0) {
				try {
					highlighter.addHighlight(position, position + r.length(), DefaultHighlighter.DefaultPainter);
				} catch (BadLocationException e) {
					System.err.printf("Error applying highlight at position %d: %s%n", position, e.getMessage());
				}
				if (i == selectedResultIndex) {
					textArea.setCaretPosition(position);
				}
				position += r.length();
			}
		}
	}

}
