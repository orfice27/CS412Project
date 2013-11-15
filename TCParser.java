import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TCParser {

	private final static String PARSED_TAG = "-parsed";

	private List<File> originalFiles;
	private List<File> parsedFiles;
	private List<String> fileExtensions;
	private File root;

	public TCParser(String fileOrDirectory) {
		this.originalFiles = new ArrayList<File>();
		this.parsedFiles = new ArrayList<File>();
		this.fileExtensions = new ArrayList<String>();
		this.root = new File(fileOrDirectory);
	}

	// get all files that have been parsed [after calling this.parse()]
	public List<File> getParsedFiles() {
		return this.parsedFiles;
	}

	// reads the given file(s) with matching extensions, parses and saves new parsed files
	public void parse() {
		this.readFiles(root);
		this.createParsedFiles();
		this.parseFiles();
	}

	// registers a file extension to be matched when searching for files
	public void addFileExtension(String extension) {
		this.fileExtensions.add("." + extension);
	}

	// deletes the parsed files (call the treat parsed files as temporary)
	public void deleteParsedFiles() {
		for (File file : this.parsedFiles) {
			file.delete();
		}
	}

	// reads all files from directory or single file
	private void readFiles(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				this.readFiles(child);
			}
		} else if (file.isFile() && this.isValidType(file)) {
			this.originalFiles.add(file);
		}
	}

	// creates copy of original files with new parsed file names (files empty - call this.parseFiles() to populate)
	private void createParsedFiles() {
		for (File readFile : this.originalFiles) {
			File writeFile = new File(this.getParsedFileName(readFile));
			this.parsedFiles.add(writeFile);
		}
	}

	// creates parsed file name from orginial file name
	private String getParsedFileName(File readFile) {
		String path = readFile.getAbsolutePath();
		int dot = path.lastIndexOf(".");
		return path.substring(0, dot) + TCParser.PARSED_TAG + path.substring(dot);
	}

	// populates parsed files with original content that has been parsed
	private void parseFiles() {
		for (int i = 0; i < this.originalFiles.size(); i++) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(originalFiles.get(i)));
				BufferedWriter bw = new BufferedWriter(new FileWriter(parsedFiles.get(i)));
				String line = null;
				while ((line = br.readLine()) != null) {
					bw.write(parseLine(line));
					bw.newLine();
				}
				br.close();
				bw.close();
			} catch (IOException e) {
				System.err.printf("Error creating temporary file %s: %s\n", originalFiles.get(i).getName(), e.getMessage());
			}
		}
	}

	// removes any <tags> in a string
	private String parseLine(String line) {
		return line.replaceAll("\\<.*?\\>", "");
	}

	// checks if a given file has one of the registered file extensions
	private boolean isValidType(File file) {
		for (String extension : this.fileExtensions) {
			if (file.getName().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}


	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.printf("Usage: Parser [ file | directory ]\n");
			System.exit(0);
		}

		TCParser p = new TCParser(args[0]);
		p.addFileExtension("xml");
		p.parse();

		// print files to console
		for (File file : p.getParsedFiles()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				System.out.printf("\n\nFile %s:\n", file.getName());
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				br.close();
			} catch (IOException e) {
				System.err.printf("Error printing file %s: %s\n", file.getName(), e.getMessage());
			}
		}

		// delete parsed files from file system (could neglect this call to leave parsed output)
		p.deleteParsedFiles();
	}


}