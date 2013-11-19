package xmlParser;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author David Coxon
 * 
 * Simple class which takes in two strings, one a file path and the other 
 * an extension, and gathers all files of that extension contained by that
 * filepath
 *
 */
public class FileFinder {
	private String ext;
	private ArrayList<File> filteredFiles;
	
	public FileFinder(String path, String ext){
		this.ext=ext;
		filteredFiles = new ArrayList<File>();
		scanForExt(new File(path));
	}
	
	public ArrayList<File> getSelectedFiles(){
		return filteredFiles;
	}
	
	private void scanForExt(File f){		
			if (f.isDirectory()){
				for(File p: f.listFiles()){
				scanForExt(p);
			}
			}
			else{
				if(f.getName().endsWith(ext)){
					filteredFiles.add(f);
				}			
			}						
	}
}
