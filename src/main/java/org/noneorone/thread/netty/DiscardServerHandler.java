package org.noneorone.thread.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

@SuppressWarnings("unused")
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// ??
//		ByteBuf in = (ByteBuf) msg;
//		try {
//			while (in.isReadable()) {
//				char c = (char) in.readByte();
//				if (c == 10) {
//					System.out.println();
//				} else {
//					System.out.print(c);
//				}
//				System.out.flush();
//			}
//		} finally {
//			ReferenceCountUtil.release(msg);
//		}
		
		// §Õ
		ctx.write(msg);
		ctx.flush();
		
	}

}
