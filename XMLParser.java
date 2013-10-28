import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class XMLParser {
	

	public XMLParser(){
		//empty constructor
	}
	
	public static void main(String argv[]) {
		 
	    try {
	    	
	    //could get it to read 
	    File fXmlFile = new File("/Users/Seemai/Desktop/rel200/quran/quran.xml");
	    BufferedReader br = new BufferedReader(new FileReader(fXmlFile));
	    String line;
	    	
	    	//while there's still lines to be read
	    	while((line=br.readLine())!= null){
	    	  
	    		//get the words from the line that we want to keep
	    		ArrayList<String> wordstowrite = extractWords(line);
	    		
	    		
	    		boolean firstTime = true;
	    		StringBuilder sb = new StringBuilder(50);
	    		for (String word : wordstowrite) {
	    		    if (firstTime) {
	    		        firstTime = false;
	    		        
	    		    } else {
	    		        sb.append(' ');
	    		    }
	    		    sb.append(word);
	    		}

	    		String finalResult = sb.toString();
	    		System.out.println(finalResult);
	    		
	    		
	    	}
	    	
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	  }
	
	public static ArrayList<String> extractWords(String s){
		
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
			   if(isLeftArrow(firstchar)==true){
				
					   
					   //if it's an opening tag, we need to find where it ends
					   for(int i=1; i<word.length(); i++){
						   
						   //if the next char is the right arrow
						   if(isRightArrow(word.charAt(i))==true){
							   
							   //we make a new substring with the remaining contents
							   String sub = word.substring(i+1);
							   contents.add(sub);
							   
						   }
					   }
				   
				   
		   } else if (isClosingTag(word) ==true){
			   
			   //then we gotta find where the arrows are
				
			   
			   for (int i = 0; i<word.length(); i++){
				   if(isLeftArrow(word.charAt(i)) ==true){
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
	 
	
	public static boolean isLeftArrow(Character c){
	
		boolean answer;
		if(c.equals('<')){
			answer = true;
			} else{
				answer = false;
			}
		return answer;
	}
	
	public static boolean isClosingTag(String s){
		
		boolean answer;
		if(s.contains("/")){
			answer = true;
			} else{
				answer = false;
			}
		return answer;
		
		
	}
	
	public static boolean isRightArrow(Character c){
		
		boolean answer;
		if(c.equals('>')){
			answer = true;
			} else{
				answer = false;
			}
		return answer;
	}
	
	public static boolean isBlankSpace(Character c){
		if(c.equals(' ')){
			return true;
			} else return false;
	
	}
	
	
	
}




