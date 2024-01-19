package mypacket.nio;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        //举例说明buffer使用
        //创建一个buffer,大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向buffer中存放数据
        intBuffer.put(11);
        intBuffer.put(11);
        intBuffer.put(12);
        intBuffer.put(13);
        intBuffer.put(14);



        // 如何从buffer读取数据
            // 将buffer转换，读写切换
        intBuffer.flip();
        intBuffer.position(1);

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }


    }
}
