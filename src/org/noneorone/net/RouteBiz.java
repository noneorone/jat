package org.noneorone.net;




import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class RouteBiz {

  public String sendPOST(String url) throws Exception {
    PrintWriter out = null;
	BufferedReader in = null;
	String result = "";
	URL realURL = new URL(url);
	URLConnection conn = realURL.openConnection();
	conn.setRequestProperty("accept", "*/*");
	conn.setRequestProperty("connection", "Keep-Alive");
	conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
	conn.setRequestProperty("Accept-Charset", "UTF-8");
	conn.setDoOutput(true);
	conn.setDoInput(true);
	out = new PrintWriter(conn.getOutputStream());
	out.print("");
	out.flush();
	InputStreamReader is = new InputStreamReader(conn.getInputStream(), "UTF-8");
	in = new BufferedReader(is);
	String line;
	while ((line = in.readLine()) != null) {
	  result += "\n" + line;
	}
	if (out != null) {
	  out.close();
	}
	if (in != null) {
	  in.close();
	}
	return result;
  }

  @SuppressWarnings("deprecation")
  public List<String> parserXml(String xmlData) {
    LinkedList<String> list = new LinkedList<String>();
	String item = "";
	String time = "";
	String road = "";
	String li = "";
	String start = "";
	String end = "";
	try {
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder builder = factory.newDocumentBuilder();
	  StringBufferInputStream sbis = new StringBufferInputStream(new String(xmlData.trim().getBytes("UTF-8"), "ISO-8859-1"));
	  Document doc = builder.parse(sbis);
	  NodeList n1 = doc.getElementsByTagName("leg");
	  for (int i = 0; i < n1.getLength(); i++) {
	    NodeList n2 = n1.item(i).getChildNodes();
		for (int j = 0; j < n2.getLength(); j++) {
		  if (n2.item(j).getNodeName().equals("start_address")) {
		    start = "���?"+n2.item(j).getTextContent();
			list.add(start);
		  }
		  if (n2.item(j).getNodeName().equals("end_address")) {
		    // System.out.println("�յ㣺" + n2.item(j).getTextContent());
			end = "�յ㣺"+n2.item(j).getTextContent();
			list.add(end);
		  }
		  NodeList n3 = n2.item(j).getChildNodes();
		  for (int k = 0; k < n3.getLength(); k++) {
		    if(n3.item(k).getNodeName().equals("text")&&n3.item(k).getParentNode().getNodeName().equals("duration")){
			  list.add("����ʱ�䣺"+n3.item(k).getTextContent());
			}
			if(n3.item(k).getNodeName().equals("text")&&n3.item(k).getParentNode().getNodeName().equals("distance")){
			  list.add("��·�̣�"+n3.item(k).getTextContent());
			}
				  
		    if (n3.item(k).getNodeName().equals("html_instructions")) {
			  // System.out.println("����"+ n3.item(k).getTextContent());
			  road = n3.item(k).getTextContent().replaceAll("<div", "<sapn").replaceAll("</div>", "</span>");
			}
			NodeList n4 = n3.item(k).getChildNodes();
			for (int l = 0; l < n4.getLength(); l++) {
			  if (n4.item(l).getNodeName().equals("text")&& n4.item(l).getParentNode().getNodeName().equals("distance")) {
			    // System.out.println("·�̣�" + n4.item(l).getTextContent());
				li = n4.item(l).getTextContent();
				item = road + time + li;
				list.add(item);
			  }
			  if (n4.item(l).getNodeName().equals("text")&& n4.item(l).getParentNode().getNodeName().equals("duration")) {
			    // System.out.println("ʱ�䣺" + n4.item(l).getTextContent());
				time = n4.item(l).getTextContent();
			  }
			}
		  }
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return list;
  }

  public List<String> getResult(String start, String end, String type) {
    List<String> list =null;
	try {
		//System.out.println(start+end+type);
	 // String address1=new String(start.trim().getBytes("ISO-8859-1"),"UTF-8");
	 // String address2=new String(end.trim().getBytes("ISO-8859-1"),"UTF-8");
	 // String mode=new String(type.trim().getBytes("ISO-8859-1"),"UTF-8");
	  String url  = "http://maps.google.com/maps/api/directions/xml?origin=" + URLEncoder.encode(start, "UTF-8") + "&destination="	+ URLEncoder.encode(end, "UTF-8") + "&language=zh-CN&mode="	+ type + "&sensor=false";
	  String result = sendPOST(url);
	  list = this.parserXml(result);
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return list;
  }

  public boolean changyong(String userID,String start,String end,String type,String cityID,String visitorcity ){
	  return false;
  }
 
  public static void main(String[] args) {
	 System.out.println(new RouteBiz().getResult("宝安区西乡镇宝城花园","南山区深圳大�?", "walking"));
}
  
}
