package mypacket.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello lxs";
        // 创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");
        // 通过FileOutputStream 获取对应的 FileChannel
        // 这个fileChannel 真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将 str 放入byteBuffer
        byteBuffer.put(str.getBytes());

         // 对byteBuffer进行反转
        byteBuffer.flip();

        // 将byteBuffer的数据写入到channel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();
     }
}

