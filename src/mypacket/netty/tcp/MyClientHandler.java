package mypacket.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private       int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                // 使用客户端发送10条数据 hello,Server
        for (int i = 0; i < 10; i++) {
            ByteBuf buf = Unpooled.copiedBuffer("Hello,Server" + i, Charset.defaultCharset());
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);
        String message = new String(buffer, Charset.forName("utf-8"));
        System.out.println("客户端接受到消息=" + message);
        System.out.println("客户端接受消息数量=" + (++this.count));
    }
}
