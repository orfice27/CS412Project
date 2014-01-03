package view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Tab for displaying a full file from the index
 */
public class FileDisplayTab extends JScrollPane {

	private static final long serialVersionUID = -8170495304477233177L;

	private String file;
	private JPanel container;
	private JTextArea textArea;

	public FileDisplayTab(String file) {
		this.file = file;
		container = new JPanel();
	    container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

		setViewportView(container);
	    setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	    createTextView();
	}

	private void createTextView() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		try {
			textArea.insert(new String(readFile()), 0);
		} catch (IOException e) {
			System.err.printf("Error reading file %s%n", file);
			textArea.insert(new String("Error reading file"), 0);
		}
		container.add(textArea);
	}

	private byte[] readFile() throws IOException {
		return Files.readAllBytes(Paths.get(file));
	}

	public String getFile() {
		return file;
	}

}
