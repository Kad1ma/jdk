package mypacket.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    // 定义属性
    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int PORT = 6667;

    // 初始化工作
    public GroupChatServer() {
        try {
        // 得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(6667));
            // 设置非阻塞模式
            listenChannel.configureBlocking(false);
            // 将该listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                if (count <= 0) {
                    System.out.println("等待中.....");
                }

                // 遍历得到的selectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    // 监听到accept
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = listenChannel.accept();
                        socketChannel.configureBlocking(false);
                        // 将该socketChannel注册到selector
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        // 提示
                        System.out.println(socketChannel.getRemoteAddress() + " 上线 ");
                    }

                    //通道发生read事件
                    if (key.isReadable()) {
                        // 处理读
                        readData(key);
                    }

                    // 当前的key删除，防止重复操作
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 读取客户端消息
     */
    public void readData(SelectionKey key) {
                        // 定义一个socketChannel
        SocketChannel channel = null;
        try {
            // 得到channel
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);

            // 根据count做处理
            if (count > 0) {
                // 把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                // 输出该消息
                System.out.println(msg);
                // 向其他客户端转发消息（排除自己）， 专门写一个方法来处理
                sendInfoToOtherClients(msg, channel);

            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了 ");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户（通道）
     */
    public void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中");
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            // 通过key取出对应的socketChannel
            SelectableChannel channel = key.channel();
            // 排除自己
            if (channel instanceof SocketChannel  && channel != self) {
                SocketChannel socketChannel = (SocketChannel) channel;
                // 将msg,存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer数据写入通道
                socketChannel.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        // 创建服务器对象
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
