package xmlTagSearcher;
/**
 * Allows user to create XML tag objects for representation within the Searcher system. 
 * @author Iain 
 *
 */
public class Tag {

	private String startBrace ="<";
	private String endBrace=">";
	private String initialBehaviourTag = "None";
	
	/**
	 * Tags we allow the user to search the document for using our built-in query language.
	 */
	public enum TagOptions{
		p,b,title,subtitle,titlepage, coverpage//add more as appropriate 
	}
	/**
	 * A tag is a singular Instance of one part of a tag pair, 
	 * used for distilling information for formatting rules within the XML files.
	 * @param desiredTagSymbol - The symbol of the XML tag this instance of Tag will represent.
	 */
	public Tag(String desiredTagSymbol) {
		initialBehaviourTag = desiredTagSymbol;
		
	}

	/**
	 * Checks whether the entered string is an xml Start tag of the form <"someTag">
	 * @param proposedTag - a tag which requires certification.
	 * @return - True iff a start tag is a TagOption , otherwise false.
	 */
	public boolean isStartTag(String proposedTag){
		if(proposedTag.startsWith(startBrace)&& proposedTag.endsWith(endBrace) 
				&& proposedTag.charAt(1)!='/' ){
			for(TagOptions t :TagOptions.values()){
				if(proposedTag == t.toString()){
					return true;
				}
			}
		}  
		return false;		
	}

	/**
	 * 
	 * @param proposedTag - a tag which requires certification.
	 * @return - True iff a end tag is as a TagOption , otherwise false.
	 */
	public boolean isEndTag(String proposedTag){
		if(proposedTag.startsWith(startBrace)&& proposedTag.endsWith(endBrace) 
				&& proposedTag.charAt(1)=='/' ){
			for(TagOptions t :TagOptions.values()){
				if(proposedTag == t.toString()){
					return true;
				}
			}
		}  
		return false;		
	}
}
