package xmlTagSearcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TagSearcher {
	FileReader xmlSearcher;
	
	public TagSearcher(File f) throws FileNotFoundException {
	 xmlSearcher = new FileReader(f);
	}
	
	
	public boolean changeFilePointer(File f) throws FileNotFoundException{
		xmlSearcher = new FileReader(f);		
		return true;
	}
}
