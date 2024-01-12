package mypacket.nio;

import java.nio.IntBuffer;

public class NIOTest {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }
        intBuffer.flip();

        // 如何从buffer读取数据
        // 将buffer换换，读写切换（！！！）
        /*
            public final Buffer flip() {
                // 读/写数据不能超过position
                limit = position;
                // 重置position，从数组开头开始读/写
                position = 0;
                mark = -1;
                return this;
            }
         */
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

    }
}
