package xmlTagSearcher;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import searcher.Searcher;
import searcher.XMLSearcher;

import xmlParser.FileFinder;

public class TagSearcher {
	FileFinder xmlFileCollater;
	ArrayList<File> searchSpace ;
	XMLSearcher xmlSearcher;
	/**
	 * A tagSearcher allows you to use this to identify the documents 
	 * within the search space which contain a number of TagOptions we allow users to search upon.
	 * @param f - The some filefinder to allow the user to identify all the documents within the search space.
	 */
	public TagSearcher(FileFinder f) {
		xmlFileCollater = f;
		searchSpace =f.getSelectedFiles();
	}
	public List TagSearch(){
		return null;
	}
}




