package mypacket.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 说明
 * 1. 我们自定义一个Handler，需要继承netty规定好的某个HandlerAdapter
 * 2. 这时我们自定义一个handler，才能成为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件（这里我们可以读取客户端发送的消息）
     * @param ctx 上下文对象，含有管道pipeline，通道
     * @param msg 就是客户端发送的数据 默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //比如我们这里有一个非常耗时间的业务 -> 异步执行 -> 提交该channel 对应的NioEventLoop的taskQueue中

        // 解决方案1 用户程序自定义的普通任务
//ctx.channel().eventLoop().execute(()->{
//    try {
//        TimeUnit.SECONDS.sleep(10);
//        System.out.println(Thread.currentThread().getName());
//    } catch (InterruptedException e) {
//
//    }
//    ctx.writeAndFlush(Unpooled.copiedBuffer("hello client2", CharsetUtil.UTF_8));
//});
//
//        // 解决方案1 用户程序自定义的普通任务
//        ctx.channel().eventLoop().execute(()->{
//            try {
//                TimeUnit.SECONDS.sleep(20);
//                System.out.println(Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//
//            }
//            ctx.writeAndFlush(Unpooled.copiedBuffer("hello client3", CharsetUtil.UTF_8));
//        });


        // 解决方案2 用户自定义定时任务 -> 该任务提交到scheduleTaskQueue
        ctx.channel().eventLoop().schedule(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {

            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello client3", CharsetUtil.UTF_8));
        },5,TimeUnit.SECONDS);



        System.out.println("go on");


//        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
//        System.out.println("server ctx = "  + ctx);
//        System.out.println("看看channel和pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();// 本质是一个双向链表，出栈入栈问题
//        //将 msg转成一个ByteBuf
//        // ByteBuf是Netty提供的，不是NIO的ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是："+ buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

      /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是wirte + flush
        // 将数据写入到缓存，并刷新
        // 一般来讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello 客户端", CharsetUtil.UTF_8));
    }

    /**
     * 处理一次，一般需要关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
