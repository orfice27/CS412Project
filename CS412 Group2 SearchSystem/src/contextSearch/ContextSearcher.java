package contextSearch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  
 * @author Iain Dobbin
 * Allows the return of query contextual highlited  data to be used further within project.
 */
public class ContextSearcher {

	private ArrayList<File> searchDomain;
	ArrayList<Map<Integer,File>> contextSearchResults = new ArrayList<Map<Integer,File>>();
	HashMap<Integer,File > contextSearchResult = new HashMap<Integer, File>();
	private ArrayList<File>foundDomain;

	/**
	 * Creates a context searcher pointing to the proposed corpus for searching.
	 * @param proposedFileCorpus  - contains multiple file corpus upon which search terms are examined.
	 */
	public ContextSearcher(ArrayList<File> proposedFileCorpus){
		searchDomain = proposedFileCorpus;
	}
	/**
	 * Creates a context searcher pointing to the proposed corpus for searching.
	 * @param proposedSearchCorpus  - contains single file corpus upon which search terms are examined.
	 */
	public ContextSearcher(File proposedSearchFile){
		searchDomain.add(proposedSearchFile);
	}

	/*
	 * - come up with a way to use find a words
	 * - store those found words in some context
	 */
	/**
	 * Produces a set for examination for a single word String search term and returns a valid set of occurrences within the file corpus.
	 * @param searchTerm - the proposed single word search term to produce the search results.
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Map<Integer, File>> Search(String searchTerm) throws IOException{
		//maybe some form of xml tag removal
		// should find all the files and make a stack and then and them all to the found domain then trim
		for(File f : searchDomain){
			contextSearchResult.clear();// make sure no previous search results exist
			contextSearchResult = assessFileCandidacy(f, searchTerm);
			HashMap<Integer,File> current = contextSearchResult;
			contextSearchResults.add(current);
		}
		return contextSearchResults;
	}

	/**
	 * Assesses whether a file contains a search term and add this to the appropriate structures-iff relevant.
	 * @param f - proposed file containing search term
	 * @param searchTerm - the proposed search term
	 * @return 
	 */
	private HashMap<Integer, File> assessFileCandidacy(File f , String searchTerm){

		FileReader flrdr;
		try {
			flrdr = new FileReader(f);

			BufferedReader reader = new BufferedReader(flrdr);
			String line = " ";
			int contextLine = 0;
			boolean found = false ;
			while(!line.isEmpty()){// nasty Bug??
				contextLine++;
				line= reader.readLine();

				if (line.contains(searchTerm)){
					addtoFound(f);
					contextSearchResult.put(contextLine, f);
					found =true;
				}
				else if(!found && foundDomain.contains(f)){foundDomain.remove(f);}
			}
			contextLine =0;
			reader.close();
		} 

		catch (FileNotFoundException e) {System.out.println("File" + f +" could not be found within the corupus.Aborting search.");} 
		catch (IOException e) {System.out.println("File" + f +" contains unreadable sequenceS. Aborting search.");}
		return contextSearchResult;
	}
	/**
	 * **EXAMPLE**
	 * @return
	 */
	public Map<String,File> returnInContext(){// potential EPIC fail
		Map<String,File> results =  new HashMap<String, File>();


		return results;
	}
	/**
	 * Simply adds a file to the found domain if it does not exist.
	 * @param f - proposed file to be added to the fou
	 */
	private void addtoFound(File f){
		if(!foundDomain.contains(f)){foundDomain.add(f);}
	}


	public HashMap<Integer, File> getContextSearchResult() {
		HashMap<Integer,File> csr = contextSearchResult;
		return csr;
	}
	public ArrayList<File> getFoundDomain() {
		ArrayList<File> fd =  foundDomain;
		return fd;
	}

}