package xmlTagSearcher;

public class Tag {

	private String startTag ="<";
	private String endTag=">";
	private String initialBehaviourTag = "None";
	
	/**
	 * Tags we allow the user to search the document for using our built-in query language.
	 *
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
	 * 
	 * @param proposedTag - a tag which requires certification.
	 * @return - True iff a start tag , otherwise false.
	 */
	public boolean isStartTag(String proposedTag){
		if(proposedTag.startsWith(startTag)&& proposedTag.endsWith(endTag) 
				&& proposedTag.charAt(1)!='/' ){} // should add tag options to here 
		return false;		
	}
}
