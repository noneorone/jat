package org.noneorone.apache.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientCase {
	
	private static final Logger LOGGER = Logger.getLogger(ClientCase.class);
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 3005;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		IoConnector connector = null;
		TextLineCodecFactory factory = null;
		
		connector = new NioSocketConnector();
		//设定超时时间
		connector.setConnectTimeout(30000);
		//文本字符编码
		factory = new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue());
		//添加过滤器
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));
		//添加业务逻辑
		connector.setHandler(new ClientCaseHandler());
		
		IoSession session = null;
		//创建连接
		ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));
		//等待建立连接
		future.awaitUninterruptibly();
		//获得session
		session = future.getSession();
		//发送信息
		session.write("Mina Baby!");
		LOGGER.info("客户端发送信息成功.");
		
		//等待连接断开
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
		
	}

}
