package mypacket.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        // 创建相关流
        FileInputStream fileInputStream = new FileInputStream(("C:\\Users\\admin\\Downloads\\48e2f6e805dda6e99b011414211f8039.jpeg"));
        FileOutputStream fileOutputStream = new FileOutputStream("a.jpg");

        // 获取各个流对应的fileChannel
        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel destChannel = fileOutputStream.getChannel();

        // 使用transferForm完成拷贝
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        // 关闭相关通道和流
        sourceChannel.close();
        destChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
