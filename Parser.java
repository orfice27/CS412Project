
		import javax.xml.parsers.DocumentBuilderFactory;
		import javax.xml.parsers.DocumentBuilder;
		import org.w3c.dom.Document;
		import org.w3c.dom.NodeList;
		import org.w3c.dom.Node;
		import org.w3c.dom.Element;
		import java.io.File;
		 
		public class Parser {
		 
		  public static void main(String argv[]) {
		 
		    try {
		 
			File fXmlFile = new File("/Users/Seemai/Desktop/rel200/quran/quran.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("tstmt");
		 
			System.out.println("----------------------------");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					//System.out.println("Title : " + eElement.getAttribute("title"));
					System.out.println("Title : " + eElement.getElementsByTagName("title").item(0).getTextContent());
					System.out.println("Text : " + eElement.getElementsByTagName("p").item(0).getTextContent());
					System.out.println("Subtitle : " + eElement.getElementsByTagName("subtitle").item(0).getTextContent());
					System.out.println("Preface : " + eElement.getElementsByTagName("preface").item(0).getTextContent());
		 
				}
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		  }
		 
		
	
}

