package mypacket.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        // 创建BossGroup WorkerGroup
        // 说明
        // 1.创建两个线程组 bossGroup和workerGroup
        // 2.bossGroup只是处理连接请求, 真正的和客户端业务处理，会交给workerGroup完成
        // 3.两个都是无限循环
        // 4.bossGroup 和 workerGroup 含有的子线程（NioEventLoop)的个数默认为实际cpu核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象（匿名对象）
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 可以使用一个集合管理SocketChannel，在需要推送消息时，可以将业务加入到各个channel对应的NioEventLoop的taskQueue/ScheduleTaskQueue
                            System.out.println("客户对应的socketChannel" + socketChannel.hashCode());
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });  // 给我们的workerGroup的EventLoop对应的管道设置处理器

            System.out.println("....服务器准备好了");

            // 绑定一个端口并且同步,生成了一个CahnnelFuture对象
            // 启动服务器（并绑定端口）
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            // 给chaneelFuture注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口成功");
                    } else {
                        System.out.println("监听端口失败");
                    }
                }
            });

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
