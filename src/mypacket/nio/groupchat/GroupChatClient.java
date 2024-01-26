package mypacket.nio.groupchat;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GroupChatClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6667;

    private Selector selector;

    private SocketChannel socketChannel;

        private String username;

    public GroupChatClient() {
        try {
            selector = Selector.open();
            // 连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            // 设置非阻塞
            socketChannel.configureBlocking(false);
            // 将channel 注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 得到username
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向服务器发送消息
     * @param info
     */
    public void sendInfo(String info) {
        info = username + " 说: " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取从服务器端回复的消息
     */
    public void readInfo() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        System.out.println(new String(buffer.array()));
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("没有可用的通道");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // 启动我们客户端
        GroupChatClient chatClient = new GroupChatClient();
         // 启动一个线程，每隔3秒，读取从服务器发送数据
        new Thread(()->{
            while (true) {
                chatClient.readInfo();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {

                }
            }
        }).start();

        // 发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            chatClient.sendInfo(str);
        }
    }
}
