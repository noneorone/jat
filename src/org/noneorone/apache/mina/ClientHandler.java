package org.noneorone.apache.mina;

import java.net.SocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends IoHandlerAdapter{

	public SocketAddress remote(IoSession session){
		return session.getRemoteAddress();
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("incoming client " + this.remote(session) + " connected!");
		
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("server: " + message.toString());
		session.write(this.remote(session) + " said to you: " + message.toString() + " ... ");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("incoming client " + this.remote(session) + " disconnected!");
	}
	
	
	
}
