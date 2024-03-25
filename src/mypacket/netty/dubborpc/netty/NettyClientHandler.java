package mypacket.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private      ChannelHandlerContext context;

    /**
     * 返回的结果
     */
    private String result;

    /**
     * 客户调用方法是，传入的参数
     */
    private String param;

    /**
     * 与服务器的连接创建后，就会被调用，这个方法是第一个被调用的
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 在其他方法会使用到ctx
        context = ctx;
    }

    /**
     * 收到服务器的数据后，调用方法
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public  synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            result = msg.toString();
            // 唤醒等待的线程
            notify();
    }

    /**
     * 被代理对象调用，发送数据给服务器，-> wait -> 发送完后等待被唤醒 -> 返回结果
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        // 等待channelRead方法获取到服务器结果后，唤醒
        wait();
        // 服务器返回的结果
        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public void setParam(String param) {
        this.param = param;
    }
}
