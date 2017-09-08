package org.noneorone.thread.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

	private int port;

	public DiscardServer(int port) {
		this.port = port;
	}

	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast(new DiscardServerHandler());
				}
			});

			ChannelFuture future = bootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	
	public static void main(String[] args) {
		
		int port = 8080;
		if (args.length > 0) {
			port = Integer.valueOf(args[0]);
		}
		
		System.out.println("port: " + port);
		
		new DiscardServer(port).run();
		
		
	}
	
	
}
