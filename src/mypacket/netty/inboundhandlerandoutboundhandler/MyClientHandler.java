package mypacket.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    /**
     * 发送数据
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClinetHandler 发送数据");
//        ctx.writeAndFlush(123456L);

        // 分析
        // 1."abcdabcdabcdabcd" 16个字节
        // 2.该处理器的前一个handler 是 MyLongToByteEncoder
        // 3.MyLongToByteEncoder 父类是 MessageToByteEncoder
        // 4.父类 MessageToByteEncode
            /*
            if (acceptOutboundMessage(msg)) {  //判断当前msg，是不是应该处理的类型，如果是就处理，不是就跳过encode
                @SuppressWarnings("unchecked")
                I cast = (I) msg;
                buf = allocateBuffer(ctx, cast, preferDirect);
                try {
                    encode(ctx, cast, buf);
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise);
            }
            4.因此我们编写Encoder时，要注意传入的数据类型和处理的数据类型一致
             */
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("得到服务器" + ctx.channel().remoteAddress() + " 发送消息" + msg );
    }
}
