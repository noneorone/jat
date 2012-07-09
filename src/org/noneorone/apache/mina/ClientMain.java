package org.noneorone.apache.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientMain {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder builder = connector.getFilterChain();
		builder.addLast("myChainBuilder", new ProtocolCodecFilter(new TextLineCodecFactory()));
		connector.setHandler(new ClientHandler());
		connector.setConnectTimeout(30);
		
		ConnectFuture future = connector.connect(new InetSocketAddress("localhost", 9988));
		future.awaitUninterruptibly();
		future.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
		
	}
	
}
