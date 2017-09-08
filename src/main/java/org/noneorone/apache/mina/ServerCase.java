package org.noneorone.apache.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ServerCase {
	
	private static final int PORT = 3005;
	private static final Logger LOGGER = Logger.getLogger(ServerCase.class);
	
	public static void main(String[] args) {
		IoAcceptor acceptor = null;
		TextLineCodecFactory factory = null;
		IoSessionConfig sessionConfig = null;
		
		//非阻塞socket
		acceptor = new NioSocketAcceptor();
		//文本换行符编解码
		factory = new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue());
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		//设定消息编码解码过滤器
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));
		//获取session配置
		sessionConfig = acceptor.getSessionConfig();
		//设置读取数据缓冲区的大小
		sessionConfig.setReadBufferSize(2048);
		//读写通道空闲时间
		sessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, 10);
		//绑定逻辑处理器
		acceptor.setHandler(new ServerCaseHandler());
		//绑定端口
		try {
			acceptor.bind(new InetSocketAddress(PORT));
			LOGGER.info("服务器已启动，端口号：" + PORT);
		} catch (IOException e) {
			LOGGER.error("绑定端口失败!");
			e.printStackTrace();
		}
		
	}

}
