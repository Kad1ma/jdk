package mypacket.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {

        /*
            创建一个bytebuf
            1.创建对象，该对象包含一个数组arr，是一个byte[10]
            2.在Netty的buffer中，不需要使用flip进行反转
            3.底层维护了一个readIndex合writeIndex
            4.通过readIndex、writeIndex和capacity,将buffer分成三个区域
            [0,readIndex] 已经读取的区域
            (readIndex,writeIndex] 可读区域
            (writeIndex,capacity] 可写区域
         */
        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        System.out.println("capacity= " + byteBuf.capacity());

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.getByte(i));
        }

        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.readByte());
        }
    }
}
