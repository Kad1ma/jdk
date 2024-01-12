package mypacket.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        // 创建文件的输入流
        File file = new File("d:\\file01.txt");
        FileInputStream fileOutputStream = new FileInputStream(file);

        // 通过输入流对象获取对应的FileChannel -> 实际类型 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将通道的数据读入到buffer
        fileChannel .read(byteBuffer);

        // 将byteBuffer的字节数据转成String
        System.out.println(new String(byteBuffer.array()));
         fileOutputStream.close();
    }
}
