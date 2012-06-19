package org.noneorone.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Just a case of the NIO.
 * @author wangmeng
 */
public class NIOCase {

	/**
	 * copy file via NIO
	 * @param src
	 * @param dest
	 */
	protected static void copy(final String src, final String dest){
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel inputChannel = null, outputChannel = null;
		ByteBuffer byteBuffer = null;
		int len = 0;
		try {
			inputStream = new FileInputStream(new File(src));
			outputStream = new FileOutputStream(new File(dest));
			inputChannel = inputStream.getChannel();
			outputChannel = outputStream.getChannel();
			byteBuffer = ByteBuffer.allocate(256);
			while(true){
				//clear the buffer so that can be written with bytes.
				byteBuffer.clear();
				len = inputChannel.read(byteBuffer);
				if(len == -1){
					break;
				}
				//flush the buffer
				byteBuffer.flip();
				outputChannel.write(byteBuffer);
			}
			outputChannel.close();
			outputStream.close();
			inputChannel.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			System.err.append("copy()[0]: " + e.getLocalizedMessage() + "\r\n");
		} catch (IOException e) {
			System.err.append("copy()[1]: " + e.getLocalizedMessage() + "\r\n");
		} finally{
			byteBuffer = null;
			outputChannel = null;
			outputStream = null;
			inputChannel = null;
			outputChannel = null;
		}
	}
	
	/**
	 * Use SocketChannel to fetch resource from the specified URL.
	 * @param url
	 * @param file
	 */
	protected static void access(final String url, final String file){
		SocketAddress remote = null;
		SocketChannel socketChannel = null;
		ByteBuffer byteBuffer = null;
		Charset charset = null;
		FileOutputStream fileOutputStream = null;
		FileChannel fileChannel = null;
		try {
			remote = new InetSocketAddress(url, 80);
			//open the remote resource
			socketChannel = SocketChannel.open(remote);
			//specify the encode format
			charset = Charset.forName("UTF-8");
			//send the request
			socketChannel.write(charset.encode(CharBuffer.wrap("GET " + "/ HTTP/1.1" + "\r\n\r\n")));
			byteBuffer = ByteBuffer.allocateDirect(1024);
			fileOutputStream = new FileOutputStream(new File(file));
			fileChannel = fileOutputStream.getChannel();
			while(socketChannel.read(byteBuffer) != -1){
				byteBuffer.flip();
				//the buffer just can be used only once
//				System.out.println(charset.decode(byteBuffer));
				fileChannel.write(byteBuffer);
				byteBuffer.clear();
			}
			fileChannel.close();
			fileOutputStream.close();
			socketChannel.close();
		} catch(java.nio.channels.UnresolvedAddressException e){
			System.err.append("access()[0]: " + e.getMessage() + "\r\n");
		} catch (IOException e) {
			System.err.append("access()[1]: " + e.getMessage() + "\r\n");
		} finally {
				try {
					if(null != fileChannel){
						fileChannel.close();
						fileChannel = null;
					}
					if(null != fileOutputStream){
						fileOutputStream.close();
						fileOutputStream = null;
					}
					if(null != socketChannel){
						socketChannel.close();
						socketChannel = null;
					}
				} catch (IOException e) {
					System.err.append("access()[2]: " + e.getMessage() + "\r\n");
				}
		}
	}

	public static void main(String[] args) {
//		copy("E:\\note.txt", "E:\\note.log");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println(simpleDateFormat.format(new Date())+ ": Request remote resource...");
		access("www.baidu.com", "E:\\remote.html");
		System.out.println(simpleDateFormat.format(new Date())+ ": Data has been loaded.");
		System.out.println("jack is dull!");
		
		//Exception in thread "main" ERROR: JDWP Unable to get JNI 1.2 environment, jvm->GetEnv() return code = -2 JDWP exit error AGENT_ERROR_NO_JNI_ENV(183):  [../../../src/share/back/util.c:820]
		System.exit(0);
	}
}
