package dictionaryParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import xmlParser.FileFinder;

/**
 *  
 * Just a class I coded to extract terms from that stupid dictionary with all the weird punctuation and nonsense.
 * The coding is extremely ugly so any questions ask me instead.
 * It's not really needed unless the theologicaldictionary.txt.txt vanishes from your hard drive/github. Then you can just run this
 * class on the file and reparse it. I won't be cleaning up the code here because it's not needed.
 * -SeeMai
 */

public class capitalSearcher {
	private static File mainFile;
	private static ArrayList<File> parsedFiles;
	static String newline = "\n";
	static StandardAnalyzer analyze;

	public static void main(String[] args) {
		String filePath = "C:\\Users\\SeeMai\\git\\CS412Project\\SearchSystem\\data set\\theologicaldictionary.txt";
		mainFile = new File(filePath);
		parsedFiles=new ArrayList<File>();
		prepareCorpus();
		analyze = new StandardAnalyzer(Version.LUCENE_40);

	}
	
	private static void prepareCorpus(){


			try{	

				BufferedReader br = new BufferedReader(new FileReader(mainFile));		//Reads the XML file
				String line;															//Current line read from fxmlFile
				ArrayList<String> sourcelist=new ArrayList<String>();						//Represents the formatted xmlfile
				ArrayList<String> content=new ArrayList<String>();						//Where the parsed lines go			
				ArrayList<String> sourcefilter=new ArrayList<String>();	
				ArrayList<String> source=new ArrayList<String>();	
				ArrayList<String> newest=new ArrayList<String>();	
				ArrayList<String> test=new ArrayList<String>();
				
				while((line=br.readLine()) != null){

					//Extracts words from line
					//originalsource = tokenizeString(analyze, line);
					sourcelist = extractWords(line);
					sourcefilter = removePunctuation (sourcelist);
					
					newest = removeStopWords(sourcefilter);
//					for (String word : newest){
//						System.out.println(word);
//					}
					source = removeDuplicates(newest);
					
					test = removeComma(source);
					
					for (String word : test){
						System.out.println(word);
					}

					StringBuilder sb = new StringBuilder(50);
					for (String word : test){
						sb.append(' ');                        
						sb.append(word);
					} 
					content.add(sb.toString());	
				}
				
				
				/*
				 * Creates a .txt file from the parsed content, and adds it to a list of such files.
				 */
				parsedFiles.add(outputToFile(mainFile.getName(), content));
				br.close();
				
			}catch (Exception e){
				e.printStackTrace();
			}
			
			
				
	}
	/**
	 * A simple filewriter to output provided Arraylist of type String to the desired file
	 * 
	 * @param filename Desired filename of file **Note: file extension not required**
	 * @param content ArrayList<String> containing the content to be written to the file
	 */
	private static File outputToFile(String filename, ArrayList<String> content){
		File file = new File(filename+".txt");
		try{
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for(String str : content){
				output.write(str + newline);
			}
			output.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return file;

	}


	//extract all useful words
	private static ArrayList<String> extractWords(String s){
		ArrayList<String> contents = new ArrayList<String>();

		//tokenize the line
				StringTokenizer st = new StringTokenizer(s);

				//for every token
				while (st.hasMoreTokens()) {
					//we extract the word
					String word = st.nextToken();
			if(word.length()!=1){
				
					//check if the first letter of the word is a capital
					if(Character.isUpperCase(word.charAt(0))){
						//if it is we check the second letter too
						if(Character.isUpperCase(word.charAt(1))){
							contents.add(word);
						}
					}
			
				}
		}
		return contents;
	} 
	
	private static ArrayList<String> removePunctuation(ArrayList<String> uncleanContents){
		ArrayList<String> cleanContents = new ArrayList<String>();
		
		for (String s : uncleanContents){
			if(s.contains("^") || s.contains("\"") || s.contains("/") || s.contains("?")){
				//do nothing
			} else {
				cleanContents.add(s);
			}
		}
		return cleanContents;
	}
	
	
//	private static ArrayList<String> removeDuplicates(ArrayList<String> duplicatedContents){
//		
//		ArrayList<String> filteredContents = new ArrayList<String>();
//		
//		for (int i=0; i< duplicatedContents.size(); i++){
//			for(int j=1; j<duplicatedContents.size()-1; j++){
//				String base = duplicatedContents.get(i);
//				String tobechecked = duplicatedContents.get(j);
//				if (tobechecked.equals(base)){
//					
//				}
//			}
//		}
//		
//		return filteredContents;
//	}
	public static ArrayList<String> removeDuplicates(ArrayList<String> strings) {

	    int size = strings.size();
	

	    // not using a method in the check also speeds up the execution
	    // also i must be less that size-1 so that j doesn't
	    // throw IndexOutOfBoundsException
	    for (int i = 0; i < size - 1; i++) {
	        // start from the next item after strings[i]
	        // since the ones before are checked
	        for (int j = i + 1; j < size; j++) {
	            // no need for if ( i == j ) here
	            if (!strings.get(j).equals(strings.get(i)))
	                continue;
	        
	            strings.remove(j);
	            // decrease j because the array got re-indexed
	            j--;
	            // decrease the size of the array
	            size--;
	        } // for j
	    } // for i

	    return strings;

	}
	
//	public static ArrayList<String> tokenizeString(StandardAnalyzer analyzer, String string) {
//		System.out.println("string: " + string);
//	    ArrayList<String> result = new ArrayList<String>();
//	    try {
//	      TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
//	      stream.reset();
//	      while (stream.incrementToken()) {
//	        result.add(stream.getAttribute(CharTermAttribute.class).toString());
//	      }
//	    } catch (IOException e) {
//	      // not thrown b/c we're using a string reader...
//	      throw new RuntimeException(e);
//	    }
//	    return result;
//	  }
private static ArrayList<String> removeStopWords(ArrayList<String> finalContents){
		
		ArrayList<String> usefulContents = new ArrayList<String>();
		
		/*"a", "an", "and", "are", "as", "at", "be", "but", "by",
		"for", "if", "in", "into", "is", "it",
		"no", "not", "of", "on", "or", "such",
		"that", "the", "their", "then", "there", "these",
		"they", "this", "to", "was", "will", "with" */
		
		for(String s: finalContents){
			if((s.equals("OF")) || s.equals("ALL") || s.equals("THE") || s.length()< 5 || s.equals("MET") || s.contains("XX")
					|| s.equals("AND") || s.equals("CAL") || s.equals("CON") || s.contains("XV")){
				//System.out.println("stopword");
			} else{
				usefulContents.add(s);
			}
		}return usefulContents;
	}

private static ArrayList<String> removeComma(ArrayList<String> dictionaryContents){
	
	ArrayList<String> compiled = new ArrayList<String>();
	
	for (String s : dictionaryContents){
		if(s.contains(",")){
			int location = s.indexOf(",");
			//System.out.println("location: " + location);
			String newS = s.substring(0, location-1);
		//	System.out.println("Substring: " + newS);
			compiled.add(newS);
		} 
			else if (s.contains(".")){
			int location = s.indexOf(".");
			String newS = s.substring(0, location-1);
			compiled.add(newS);
			}
		} return compiled;
	}

}








