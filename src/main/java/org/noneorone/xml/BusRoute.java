package org.noneorone.xml;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** 
 * Title: base<br> 
 * Description: DOM Parser<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 14, 2011 5:46:40 PM <br> 
 * @author wangmeng
 */
public class BusRoute {

	
	private static String interceptBlankChar(String content){
		//定义空格、制表符、换行符、回车符匹配规则
		Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
		//通过匹配器验证文本内容
		Matcher matcher = pattern.matcher(content);
		//替换所有的上述匹配规则的字符为空字符，并截取两端空格
		return matcher.replaceAll("").trim();
	}
	
	public static void main(String[] args){
		
		//构造器工厂类
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//构造器
		DocumentBuilder builder = null;
		//文本对象
		Document doc = null;
		
		try {
			//通过工厂类创建构造器
			builder = factory.newDocumentBuilder();
			//通过构造器解析指定路径的文件，并返回该文本对象doc
			doc = builder.parse("F:/bus_travel.xml");
			//获取指定标签节点
			NodeList nodeList = doc.getElementsByTagName("card");
			for(int i=0; i<nodeList.getLength(); i++){
				//获取该节点下所有子节点
				NodeList childNodes = nodeList.item(i).getChildNodes();
				int count = 0;
				for(int j=0; j<childNodes.getLength(); j++){
					//获取p标签节点
					if(childNodes.item(j).getNodeName().equalsIgnoreCase("p")){
						count ++;
						//取第三个p标签内部
						if(count == 3){
							NodeList pNodes = childNodes.item(j).getChildNodes();
							for(int k=0; k<pNodes.getLength(); k++){
								//移除内部的img节点
								if(pNodes.item(k).getNodeName().equalsIgnoreCase("img")){
									childNodes.item(j).removeChild(pNodes.item(k));
								}
							}
							//循环移除img标签后的所有子节点
							for(int m=0; m<pNodes.getLength(); m++){
								Node node = pNodes.item(m);
								//获取内部文本内容不为null对象或为空的所有节点
								if(null != node.getTextContent() && node.getTextContent().trim().length() > 0){
									if(null != node.getBaseURI()){
										System.out.println("***"+interceptBlankChar(node.getTextContent())+"***");
//										System.out.println("***"+interceptBlankChar(node.getNodeValue())+"***##"+pNodes.item(m+1).getNodeName()+"####");
									}
//									System.out.println("*********"+node.getBaseURI()+"***********");
									if(node.getNextSibling().getNodeName().equalsIgnoreCase("a")){
//										System.out.println("next is a link ...");
									}
//									if(node.getNodeName().equalsIgnoreCase("a")){
//									}
									System.out.println(node.getNodeName() + "=========" + interceptBlankChar(node.getTextContent()));
								}
							}
						}
					}
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
