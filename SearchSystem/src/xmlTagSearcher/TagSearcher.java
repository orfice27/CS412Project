package xmlTagSearcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import xmlParser.FileFinder;

public class TagSearcher {
	FileFinder xmlFileCollater;

	/**
	 * A tagSearcher allows you to use this to identify the documents 
	 * within the search space which contain a number of TagOptions we allow users to search upon.
	 * @param f - The some filefinder to allow the user to identify all the documents within the search space.
	 */
	public TagSearcher(FileFinder f) {
		xmlFileCollater = f;
	}


}




