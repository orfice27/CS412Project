package OurSearchers;
import java.io.File;
import java.util.ArrayList;


public class ContextSearchResult {

	private File f;
	private ArrayList<String> fileResults;

	public ContextSearchResult(File termWithin, ArrayList<String> termInstances){
		 f= termWithin;
		 fileResults = termInstances;
	}

	public File getFileInstance() {
		File fileInstance = f;
		return fileInstance;
	}

	public ArrayList<String> getFileResults() {
		ArrayList<String>  flrs = fileResults;
		return flrs;
	}
	public boolean addTermInstance(String termContext){
		return fileResults.add(termContext);
	}
}
