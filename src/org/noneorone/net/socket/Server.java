package org.noneorone.net.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wangmeng
 */
public class Server {

	private static final int port = 5320;
	
	public static void main(String[] args){
		
		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();
			if(socket.isConnected()){
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				dos.writeUTF("hello,client...");
				System.out.println(dis.readUTF());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
