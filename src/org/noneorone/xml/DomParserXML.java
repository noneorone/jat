package org.noneorone.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** 
 * Title: base<br> 
 * Description: DOM Parser<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 14, 2011 5:46:40 PM <br> 
 * @author wangmeng
 */
public class DomParserXML {

	public static void main(String[] args){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse("F:/LSPFix.xml");
			NodeList nodeList = doc.getElementsByTagName("LSPFix");
			for(int i=0;i<nodeList.getLength();i++){
				NodeList childNodes = nodeList.item(i).getChildNodes();
				for(int j=0;j<childNodes.getLength();j++){
					System.out.println(childNodes.item(i));;
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
