package mypacket.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import mypacket.netty.codec.StudentPOJO;

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
            // 读取从客户端发送的StudentPOJO.Student
        MyDataInfo.MyMessage myMessage = (MyDataInfo.MyMessage) msg;
            // 根据dataTypa来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = myMessage.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = myMessage.getStudent();
            System.out.println("学生id="+student.getId()+"学生名字=" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = myMessage.getWorker();
            System.out.println("工人的名字="+ worker.getName() +"年龄="+worker.getAge());
        } else {
            System.out.println("传输的类型不正确");
        }

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
