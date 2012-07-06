package org.noneorone.apache.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ServerHandler extends IoHandlerAdapter{

	private int recCount;
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("client: " + session.getRemoteAddress() + " connected!");
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("client: " + session.getRemoteAddress() + " disconnected!");
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("client-message: " + message.toString());
		session.write("Honey, how are you? " + recCount);
		recCount ++;
	}
	
}
