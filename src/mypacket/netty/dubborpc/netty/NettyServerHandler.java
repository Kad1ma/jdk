package mypacket.netty.dubborpc.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import mypacket.netty.dubborpc.provider.HelloServiceImpl;
import mypacket.netty.dubborpc.publicinterface.HelloService;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息，并调用服务
        System.out.println("msg="+msg);
        // 客户端在调用服务器的api时，我们需要定义一个协议
        // 比如我们要求每次发消息时，都必须以某个字符串开头 "HelloService#hello#"
        if (msg.toString().startsWith("HelloService#hello#")) {
            String hello = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(hello);
        }
    }
}
