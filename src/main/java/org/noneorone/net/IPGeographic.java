package org.noneorone.net;

import java.io.BufferedOutputStream;      
import java.io.BufferedReader;      
import java.io.File;      
import java.io.FileOutputStream;      
import java.io.IOException;      
import java.io.InputStreamReader;      
import java.net.URL;      
import java.util.ArrayList;      
import java.util.HashMap;      
import java.util.Set;

/**
* Title: JavaTech<br>
* Description: Fetch IP and geographic datum via specified IP LIB with URL<br>
* Copyright: Copyright (c) 2011 <br>
* Create DateTime: Aug 18, 2011 3:31:34 PM <br>
* @author wangmeng
 */
public class IPGeographic {      
    /**    
     * 通过url，得到url源文件    
     * @param url    
     * @return    
     * @throws IOException    
     */     
    private static String getURLContent(String url) throws IOException {      
        URL ipListUrl = new URL(url);      
        BufferedReader in = new BufferedReader(new InputStreamReader(ipListUrl.openStream()));      
        String str = null;      
        String html = "";      
        while ((str = in.readLine()) != null) {      
            html += str;      
        }      
        in.close();      
        return html;      
    }      
    /**    
     *     
     * @param htmlContent    
     * @param fileName    
     * @return ip地址和ip地理信息    
     * @throws IOException    
     */     
    private static HashMap<String,String> getIpList(String htmlContent,String fileName) throws IOException {      
        String str1[] = htmlContent.split("<tr><td width=\"140\">IP:Port</td><td width=\"40\">Type</td><td width=\"90\">Speed</td><td width=\"160\"> Country/Area</td></tr>");      
        String str2 = str1[1];// ip及余下部分      
        String str3[] = str2.split("</table>");      
        String str4 = str3[0];// <tr><td>24.25.26.128<SCRIPT type=text/javascript>document.write(":"+q+d)</SCRIPT></td><td>HTTP</td><td>296,984,984</td><td>美国 维吉尼亚州</td></tr>      
              
        String str5[]=str4.split("<tr><td>");      
        HashMap<String,String> map=new HashMap<String,String>();      
        int len=str5.length;      
        String forIPArray[];      
        String forAddrArray[];      
        for(int i=0;i<len;i++){      
            forIPArray=str5[i].split("<");      
            forAddrArray=str5[i].split("<td>");      
            int len1=forAddrArray.length;      
            String str=forAddrArray[len1-1];      
            String addrBeforeTD[]=str.split("</td></tr>");      
            map.put(forIPArray[0], addrBeforeTD[0]);      
        }      
        return map;      
    }      
    /**    
     * 根据ip.cn数据库，获得ip地理信息    
     * @param ipList    
     * @throws IOException    
     */     
    @SuppressWarnings("unused")
	private static void showAddress(ArrayList<String> ipList) throws IOException {      
        File file = new File("c:/IpAddrInfo.html");      
        FileOutputStream out = new FileOutputStream(file);      
        BufferedOutputStream os = new BufferedOutputStream(out);      
              
        for(String ip:ipList){      
            URL ipListUrl = new URL("http://www.ip.cn/getip.php?action=queryip&ip_url="+ip);      
            BufferedReader in = new BufferedReader(new InputStreamReader(ipListUrl.openStream()));      
            String str = null;      
            while ((str = in.readLine()) != null) {      
                os.write((str+"</br>").getBytes());      
            }      
            in.close();      
        }      
        os.close();      
        out.close();      
    }      
    /**    
     * @param args    
     * @throws IOException    
     */     
    public static void main(String[] args) throws IOException {      
        String html=null;      
        String htmlContent;      
        HashMap<String,String> map = new HashMap<String,String>();      
        for(int i=1;i<13;i++){      
            if(i<11){      
                html="http://www.cnproxy.com/proxy" + i + ".html";      
                      
            }else{      
                html="http://www.cnproxy.com/proxyedu"+(i-10)+".html";      
            }      
            htmlContent=getURLContent(html);      
            map.putAll(getIpList(htmlContent,"c:/ip"+i+".html"));      
        }      
        File file = new File("c:/allIP.txt");      
        FileOutputStream out = new FileOutputStream(file);      
        BufferedOutputStream os = new BufferedOutputStream(out);      
        Set<String> ipSet=map.keySet();      
        int count=0;      
        for(String ip:ipSet){      
            os.write((ip+"  "+map.get(ip)+"\r\n").getBytes());      
            count++;      
        }      
        os.close();      
        out.close();  
        
        System.out.println("操作完成，共获取"+count+"个IP及其地址信息");      
    }      
}   