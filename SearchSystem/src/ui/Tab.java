package ui;
import java.util.ArrayList;

import model.SearchResult;


public class Tab {
	private int tabLocation;
	private ArrayList<SearchResult> results;
	private String title;
	private ArrayList<String> documentNameList;
	private String filename;
	
	
	public Tab(int tabLocation, ArrayList<SearchResult> results, String title){
		this.tabLocation = tabLocation;
		this.results = results;
		this.title = title;
		documentNameList = new ArrayList<String>();
		for(SearchResult poof : results){
			documentNameList.add(poof.getFileName());
		}
	}
	
	public Tab(int tabLocation, String filename, String title){
		this.tabLocation = tabLocation;
		this.filename = filename;
		this.title = title;
	}
	
	public int getTabLocation(){
		return tabLocation;
	}
	
	public ArrayList<SearchResult> getResults(){
		return results;
	}
	
	public String getTitle(){
		return title;
	}
	
	public ArrayList<String> getDocumentName(){
		return documentNameList;
		
	}
	
	public String getFilename(){
		return filename;
	}
	
	

}
