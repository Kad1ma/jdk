package mypacket.netty.buf;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class NettyByteBuf02 {
    public static void main(String[] args) {

        /*
            创建一个ByteBuf
         */
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello lxs", StandardCharsets.UTF_8);

        // 使用相关的方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            System.out.println(new String(content, StandardCharsets.UTF_8));
        }

        System.out.println("byteBuf" + byteBuf);

        System.out.println(byteBuf.arrayOffset());
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());

        byteBuf.readByte();// 导致下面行-1

        System.out.println(byteBuf.readableBytes());


        System.out.println(byteBuf.getCharSequence(0,4, StandardCharsets.UTF_8));


    }
}
