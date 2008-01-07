import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReportXMLParser {

	SocketReaderFrame srf;
	
    public ReportXMLParser(SocketReaderFrame srf) { 
        this.srf = srf;
    }
    
    public static String parse(InputStream is, OutputStream os){
    	String result = "";

    	try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			System.out.println ("Root element of the doc is " + 
					doc.getDocumentElement().getNodeName());

			NodeList listOfMembers = doc.getElementsByTagName("member");
			int totalMembers = listOfMembers.getLength();
			System.out.println("Total no of members : " + totalMembers);
	
			for(int s=0; s<listOfMembers.getLength() ; s++){
				Node epcMemberNode = listOfMembers.item(s);
				if(epcMemberNode.getNodeType() == Node.ELEMENT_NODE){

					Element epcMemberElement = (Element)epcMemberNode;

					//-------
					NodeList epcNameList = epcMemberElement.getElementsByTagName("epc");
					Element firstNameElement = (Element)epcNameList.item(0);

					NodeList textFNList = firstNameElement.getChildNodes();
					
					result = result + (s+1 + ")  " +
						((Node)textFNList.item(0)).getNodeValue().trim()) + "\n";
				}
			}
		}catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line " 
					+ err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());

		}catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();

		}catch (Throwable t) {
			t.printStackTrace ();
		}
		return result;	
    }
}
