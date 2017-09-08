package org.noneorone.net.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author wangmeng
 */
public class Client {

	private static final String host = "192.168.1.136";
	private static final int port = 5320;
	
	public static void main(String[] args){
		
		try {
			Socket socket = new Socket(host,port);
			if(socket.isConnected()){
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				dos.writeUTF("hello,server ...");
				System.out.println(dis.readUTF());
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
