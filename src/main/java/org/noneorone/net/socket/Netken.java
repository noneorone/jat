package org.noneorone.net.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Netken {

	
	public static void main(String[] args){
		//URL(String protocol, String host, int port, String file) 
		//http://192.168.1.1:8899/index.html
		try {
			URL url = new URL("HTTP","192.168.1.1",8899,"/index.html");
			InputStreamReader isr = new InputStreamReader(url.openStream());
			BufferedReader br = new BufferedReader(isr);
			String s = null;
			while((s=br.readLine()) != null){
				System.out.println(s);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
