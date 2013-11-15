
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class XMLParser {
              
                /**
                 * David here,
                 * For some reason this completely bombs the speed at which this runs. Will investigate it
                 * tomorrow, but for now i've committed it as a separate file. If anyone can see why this is the case
                 * then please amend as required or suggest that I do it. 
                 */
                        
        public static void main(String args[]) {
            boolean fileOutput = false;
           	BufferedWriter output=null;
            File file=null;
            switch(args.length){                   
                 case(1):
                        System.out.println("Parsed content follows:");
                        break;
                 case(2):
                	 	fileOutput=true; 
                        break;
                 default:
                         System.out.println("Invalid argument provided - Arguments should be either of the form:\n"
                                         + "'XMLParser <xml filepath>' if command line output desired \n\tor\n"
                                         + "Should output be desired in the form of a plain file:\n"
                                         + "'XMLparser <xmlfilepath> <outputfilepath>'");
                         System.exit(0);
                 
            }        
            String fileName = args[0];
            File fXmlFile = new File(fileName);
            try{
	            BufferedReader br = new BufferedReader(new FileReader(fXmlFile));
	            String line;
	            ArrayList<String> source=new ArrayList<String>();
	            //could get it to read             
	            if(fileOutput){ 
	            	 file = new File(args[1]);
	                 output = new BufferedWriter(new  FileWriter(file));
	                 System.out.println("Parsed file can be found: " +args[1]);
	                     }
	
	                      
	              while((line=br.readLine())!= null){
	              
	                    //get the words from the line that we want to keep
	                          source = extractWords(line);                
	                                      
	                    StringBuilder sb = new StringBuilder(50);
	                    for (String word : source){
	                        sb.append(' ');                        
	                        sb.append(word);
	                       
	                    } 
	                    if(fileOutput){                    	
	                    	  output.write(sb.toString());
	                          output.write("\n");
	                          output.flush();
	                    }
	                  
	                    else{
	                    	System.out.println(sb.toString());
	                    	}
	              		}
	              		br.close();
	                 }catch(Exception e){
	                         e.printStackTrace();
	                 }                       
	          }
        
      
        /**
         * A simple filewriter to output provided Arraylist of type String to the desired file
         * @param path Path identifying the location of the file
         * @param filename Desired filename of file **Note: file extension not required**
         * @param content ArrayList<String> containing the content to be written to the file
         */
        public static void outputToFile(String path, String filename, String str){
                try{
                        File file = new File(filename+".txt");
                        BufferedWriter output = new BufferedWriter(new FileWriter(file));
                        output.write(str);
                        output.write("\n");
                        output.close();
                }catch(Exception e){
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
