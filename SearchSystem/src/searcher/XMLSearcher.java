package searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.SearchResult;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import xmlTagSearcher.Tag.TagOptions;

public class XMLSearcher extends Searcher {

	ArrayList<Boolean> validity = new ArrayList<Boolean>();

	/**
	 * Creates a new XMLSearcher for a search space see{@Searcher.java} for more impl. details 
	 * @param root -The absolute path the of the users root 
	 * folder to the file corpus to be searched upon.
	 * @param queryString -  some XML TagOption term to search the documents upon
	 * @throws NonXMLException The query string must consist of only TagOption XML terms.
	 */
	public XMLSearcher(String root, String queryString)throws NonXMLException {
		super(root, queryString);
		// Perhaps alter the query string here to conform to the language or have another language conformer class
		for(TagOptions t : TagOptions.values() ){
			if(queryString == t.toString()){
				validity.add(true);
			}else{validity.add(false);}
		}
		if(validity.contains(true)){}
		else {throw new NonXMLException("You have Entered a " +
				"TagOption that our program doesn't handle");}
	}
	public List<SearchResult>  TagSearch(){
		try {
			return	search();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		return null;//should never happen

	}
}
