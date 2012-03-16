package org.noneorone.xml;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserXML extends DefaultHandler{

	private Vector<String> tagName;
	private Vector<String> tagValue;
	private int step;
	
	@Override
	public void startDocument() throws SAXException {
		tagName = new Vector<String>();
		tagValue = new Vector<String>();
		step = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName.add(qName);
		for(int i=0;i<attributes.getLength();i++){
			System.out.println("attrName--> "+attributes.getQName(i)+"\tattrValue--> "+attributes.getValue(attributes.getQName(i)));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		step  = step + 1;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(tagName.size()-1 == tagValue.size()){
			tagValue.add(new String(ch,start,length));
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		for(int i=0;i<tagName.size();i++){
			if(!tagName.get(i).equals("") || tagName.get(i)!=null){
				System.out.println("tagNameEnd--> "+tagName.get(i)+"\ttagValueEnd--> "+tagValue.get(i));
			}
		}
		
		System.out.println("step---> "+step);
	}

	public static void main(String[] args){
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = spf.newSAXParser();
			saxParser.parse(new File("F:/LSPFix.xml"), new SAXParserXML());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
