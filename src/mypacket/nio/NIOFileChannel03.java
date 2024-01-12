package mypacket.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        FileOutputStream fileOutputStream = new FileOutputStream("file02.txt");
        FileChannel channel2 = fileOutputStream.getChannel();
        while (true) {

            // 清空buffer，否则position == limit 时，会返回0，导致死循环
            byteBuffer.clear();
            int read = channel.read(byteBuffer);
            // 表示读完
            if (read == -1) {
                break;
            }
            // 将buffer，中的数据写入到fileChannel2 ---file02.txt
            byteBuffer.flip();
            channel2.write(byteBuffer);
        }
        fileOutputStream.close();
        fileInputStream.close();

    }
}
