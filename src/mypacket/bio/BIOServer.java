package mypacket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用BIO模型编写一个服务器端 ，监听6666端口，当有客户端连接时，就启动一个线程与之通讯。
 * 要求使用线程池机制改善，可以链接多个客户端
 * 服务器端可以接收客户端发送的数据（telnet方式即可）。telnet 127.0.0.1 6666  ctrl+] 然后 send hello
 * <p>
 * <p>
 * 问题分析
 * 1)每个请求都需要创建独立的线程，与对应的客户端进行数据Read，业务处理，数据Write。
 * 2）当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大。
 * 3）连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在Read操作上，造成线程资源浪费。
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        // 线程池机制
        /*
            思路
            1.创建一个线程池
            2.如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）
         */
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true) {
            System.out.println("线程信息1 id=" + Thread.currentThread().getName() + "名字 =" + Thread.currentThread().getName());
            //监听，等待客户端连接
            System.out.println("等待连接");
            Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //创建一个线程，与之通讯（单独写一个方法）
            executorService.execute(() -> {
                // 可以和客户端通讯
                handler(accept);
            });
        }
    }

    private static void handler(Socket accept) {
        try {
            System.out.println("线程信息2 id=" + Thread.currentThread().getName() + "名字 =" + Thread.currentThread().getName());
            byte[] bytes = new byte[2048];

            // 通过Socket获取输入流
            InputStream inputStream = accept.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // 循环的读取客户端发送的数据
            while (true) {
                System.out.println("线程信息3 id=" + Thread.currentThread().getName() + "名字 =" + Thread.currentThread().getName());
                System.out.println("read........");
                int read = inputStream.read(bytes);
                System.out.println("reading........");

                    String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }

//                if (read != -1) {
//                    // 输出客户端发送的数据
//                    System.out.println(new String(bytes, 0, read));
//                } else {
//                    break;
//                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("关闭和client的连接");
            try {
                accept.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
