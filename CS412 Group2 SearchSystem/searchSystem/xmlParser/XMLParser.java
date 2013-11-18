package xmlParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class XMLParser {

	private File mainFile;
	private ArrayList<File> parsedFiles;


	/**
	 * An XML parser which removes all xml formatting 
	 * @param filePath
	 */
	public XMLParser(String filePath){
		mainFile = new File(filePath);
		parsedFiles=new ArrayList<File>();
		prepareCorpus();
	}

	
	/**
	 * 
	 * @return ArrayList of .txt files parsed by the XMLParser
	 */
	public ArrayList<File> getParsedFiles(){
		return parsedFiles;
	}



	/**
	 * Removes XML formatting from file(s) directed to by the filePath 
	 */
	private void prepareCorpus(){
		ArrayList<File> files= new ArrayList<File>();

		/*
		 * If the given path directs to a directory rather than a single file, 
		 * use the FileFinder class to return an ArrayList of xml Files contained in 
		 * that folder
		 */
		if(mainFile.isDirectory()){			
			FileFinder finder = new FileFinder(mainFile.getAbsolutePath(), ".xml");
			files = finder.getSelectedFiles();
		}
		else{
			files.add(mainFile);
		}

		for(File xmlFile: files){
			try{	

				BufferedReader br = new BufferedReader(new FileReader(xmlFile));		//Reads the XML file
				String line;															//Current line read from fxmlFile
				ArrayList<String> source=new ArrayList<String>();						//Represents the formatted xmlfile
				ArrayList<String> content=new ArrayList<String>();						//Where the parsed lines go			

				while((line=br.readLine()) != null){

					//Extracts words from line
					source = extractWords(line);

					StringBuilder sb = new StringBuilder(50);
					for (String word : source){
						sb.append(' ');                        
						sb.append(word);
					} 
					content.add(sb.toString());	
				}
				
				/*
				 * Creates a .txt file from the parsed content, and adds it to a list of such files.
				 */
				parsedFiles.add(outputToFile(xmlFile.getName(), content));
				br.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}		
	}





	/**
	 * A simple filewriter to output provided Arraylist of type String to the desired file
	 * 
	 * @param filename Desired filename of file **Note: file extension not required**
	 * @param content ArrayList<String> containing the content to be written to the file
	 */
	private File outputToFile(String filename, ArrayList<String> content){
		File file = new File(filename+".txt");
		try{
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for(String str : content){
				output.write(str);
				output.write("\n");
			}
			output.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return file;

	}



	private ArrayList<String> extractWords(String s){
		ArrayList<String> contents = new ArrayList<String>();

		//tokenize the line
		StringTokenizer st = new StringTokenizer(s);

		//for every token
		while (st.hasMoreTokens()) {
			//we extract the word
			String word = st.nextToken();
			//then extract the first character of the word
			char firstchar = word.charAt(0);
			//check to see if it is a tag
			if(firstchar=='<'){
				//if it's an opening tag, we need to find where it ends
				for(int i=1; i<word.length(); i++){
					//if the next char is the right arrow
					if(word.charAt(i)=='>'){
						//we make a new substring with the remaining contents
						String sub = word.substring(i+1);
						contents.add(sub);
					}
				}
			} else if (word.contains("/")){
				//then we gotta find where the arrows are
				for (int i = 0; i<word.length(); i++){
					if(word.charAt(i)=='<'){
						String sub = word.substring(0,i);
						contents.add(sub);
					}
				}
			}
			else {
				//it's not a tag. so we keep it as it is.
				contents.add(word);
			}
		}
		return contents;
	}    
}





