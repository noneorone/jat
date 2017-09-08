package org.noneorone.apache.mina;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ServerCaseHandler implements IoHandler {
	
	private static final Logger LOGGER = Logger.getLogger(ServerCase.class);

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		LOGGER.error("服务器端发送发生异常.", throwable);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String msg = message.toString();
		LOGGER.info("服务端收到消息：" + msg);
		if("exit".equals(msg)){
			session.close();
		}
		session.write(new Date().toLocaleString());
	}

	@Override
	public void messageSent(IoSession session, Object object) throws Exception {
		LOGGER.info("服务端发送信息成功.");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOGGER.info("连接已关闭.");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		LOGGER.info("已建立连接.");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		LOGGER.info("服务器端进入空闲状态...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOGGER.info("连接已打开.");
	}

}
