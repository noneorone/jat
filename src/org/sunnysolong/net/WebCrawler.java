package org.sunnysolong.net;
import java.io.IOException;   
import java.io.InputStream;   
import java.net.MalformedURLException;   
import java.net.URL;   
import java.net.URLConnection;   
import java.util.Properties;   
import java.util.StringTokenizer;   
import java.util.Vector;   
  
public class WebCrawler implements Runnable {   
  public static final String SEARCH = "Search";   
  public static final String STOP = "Stop";   
  public static final String DISALLOW = "Disallow:";   
  public static final int SEARCH_LIMIT = 50;   
  
  @SuppressWarnings("unchecked")
Vector vectorToSearch = new Vector();   
  @SuppressWarnings("unchecked")
Vector vectorSearched = new Vector();   
  @SuppressWarnings("unchecked")
Vector vectorMatches = new Vector();   
  
  Thread searchThread;   
  
  public WebCrawler() {   
    // ("text/html");   
    // ("audio/basic");   
    // ("audio/au");   
    // ("audio/aiff");   
    // ("audio/wav");   
    // ("video/mpeg");   
    // ("video/x-avi");   
  
    URLConnection.setDefaultAllowUserInteraction(false);   
    searchThread = new Thread(this);   
    searchThread.start();   
  }   
  
  @SuppressWarnings({ "unchecked", "static-access" })
public void run() {   
    String strURL = "http://www.google.com";   
    String strTargetType = "text/html";   
    int numberSearched = 0;   
    int numberFound = 0;   
  
    if (strURL.length() == 0) {   
      System.out.println("ERROR: must enter a starting URL");   
      return;   
    }   
  
    vectorToSearch = new Vector();   
    vectorSearched = new Vector();   
    vectorMatches = new Vector();   
  
    vectorToSearch.addElement(strURL);   
  
    while ((vectorToSearch.size() > 0)   
        && (Thread.currentThread() == searchThread)) {   
      strURL = (String) vectorToSearch.elementAt(0);   
  
      System.out.println("searching " + strURL);   
  
      URL url = null;   
      try {   
        url = new URL(strURL);   
      } catch (MalformedURLException e1) {   
        // TODO Auto-generated catch block   
        e1.printStackTrace();   
      }   
  
      vectorToSearch.removeElementAt(0);   
      vectorSearched.addElement(strURL);   
  
      try {   
        URLConnection urlConnection = url.openConnection();   
  
        urlConnection.setAllowUserInteraction(false);   
  
        InputStream urlStream = url.openStream();   
        String type = urlConnection.guessContentTypeFromStream(urlStream);   
        if (type == null)   
          break;   
        if (type.compareTo("text/html") != 0)   
          break;   
  
        byte b[] = new byte[5000];   
        int numRead = urlStream.read(b);   
        String content = new String(b, 0, numRead);   
        while (numRead != -1) {   
          if (Thread.currentThread() != searchThread)   
            break;   
          numRead = urlStream.read(b);   
          if (numRead != -1) {   
            String newContent = new String(b, 0, numRead);   
            content += newContent;   
          }   
        }   
        urlStream.close();   
  
        if (Thread.currentThread() != searchThread)   
          break;   
  
        String lowerCaseContent = content.toLowerCase();   

        int index = 0;   
        while ((index = lowerCaseContent.indexOf("<a", index)) != -1) {   
          if ((index = lowerCaseContent.indexOf("href", index)) == -1)   
            break;   
          if ((index = lowerCaseContent.indexOf("=", index)) == -1)   
            break;   
  
          if (Thread.currentThread() != searchThread)   
            break;   
  
          index++;   
          String remaining = content.substring(index);   
          System.out.println("remaining: "+content);
          StringTokenizer st = new StringTokenizer(remaining, "\t\n\r\">#");   
          String strLink = st.nextToken();   
  
          URL urlLink;   
          try {   
            urlLink = new URL(url, strLink);   
            strLink = urlLink.toString();   
          } catch (MalformedURLException e) {   
            System.out.println("ERROR: bad URL " + strLink);   
            continue;   
          }   
  
          if (urlLink.getProtocol().compareTo("http") != 0)   
            break;   
  
          if (Thread.currentThread() != searchThread)   
            break;   
  
          try {   
            URLConnection urlLinkConnection = urlLink.openConnection();   
            urlLinkConnection.setAllowUserInteraction(false);   
            InputStream linkStream = urlLink.openStream();   
            String strType = urlLinkConnection   
                .guessContentTypeFromStream(linkStream);   
            linkStream.close();   
  
            if (strType == null)   
              break;   
            if (strType.compareTo("text/html") == 0) {   
              if ((!vectorSearched.contains(strLink))   
                  && (!vectorToSearch.contains(strLink))) {   
  
                vectorToSearch.addElement(strLink);   
              }   
            }   
  
            if (strType.compareTo(strTargetType) == 0) {   
              if (vectorMatches.contains(strLink) == false) {   
                System.out.println(strLink);   
                vectorMatches.addElement(strLink);   
                numberFound++;   
                if (numberFound >= SEARCH_LIMIT)   
                  break;   
              }   
            }   
          } catch (IOException e) {   
            System.out.println("ERROR: couldn't open URL " + strLink);   
            continue;   
          }   
        }   
      } catch (IOException e) {   
        System.out.println("ERROR: couldn't open URL " + strURL);   
        break;   
      }   
  
      numberSearched++;   
      if (numberSearched >= SEARCH_LIMIT)   
        break;   
    }   
  
    if (numberSearched >= SEARCH_LIMIT || numberFound >= SEARCH_LIMIT)   
      System.out.println("reached search limit of " + SEARCH_LIMIT);   
    else  
      System.out.println("done");   
    searchThread = null;   
  }   
  
  public static void main(String argv[]) {   
    @SuppressWarnings("unused")
	WebCrawler applet = new WebCrawler();   
    /*  
     * Behind a firewall set your proxy and port here!  
     */  
    Properties props = new Properties(System.getProperties());   
    props.put("http.proxySet", "true");   
    props.put("http.proxyHost", "webcache-cup");   
    props.put("http.proxyPort", "8080");   
  
    Properties newprops = new Properties(props);   
    System.setProperties(newprops);   
  }   
  
}   
