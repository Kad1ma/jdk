package mypacket.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明
 * 1.MappedByteBuffer可以让文件直接在内存（堆外内存）中修改，即操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile rw = new RandomAccessFile("file02.txt", "rw");
        // 获取对应的文件通道
        FileChannel channel = rw.getChannel();

        /**
         * 参数1：使用的读写模式
         * 参数2: 可以直接修改的起始位置
         * 参数3： 映射到内存的大小（不是索引位置），即将file02.txt的多少个字节映射到内存
         * 可以直接修改的范围就是0-5
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0,(byte)'H');
        map.put(3,(byte)'9');
        map.put(5,(byte)'9'); // IndexOutOfBoundsException
        rw.close();
    }
}
