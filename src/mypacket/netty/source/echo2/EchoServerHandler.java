/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package mypacket.netty.source.echo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * group就是充当业务线程池，可以将任务提交到该线程池中
     * 这里我们创建了16个线程
     */
    static final EventExecutorGroup GROUP = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException, InterruptedException {
        System.out.println("EchoServerHandler的线程是"+ Thread.currentThread().getName());
//         //按照原来的方法处理耗时任务
//        ctx.channel().eventLoop().execute(()->{
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                // 输出线程名
//                System.out.println("execute的线程是"+ Thread.currentThread().getName());
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端", CharsetUtil.UTF_8));
//            } catch (InterruptedException e) {
//                System.out.println("发生异常" +e.getMessage());
//            }
//        });
//
//        System.out.println("go on");
//
//        ctx.channel().eventLoop().execute(()->{
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                // 输出线程名
//                System.out.println("execute的线程2是"+ Thread.currentThread().getName());
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端", CharsetUtil.UTF_8));
//            } catch (InterruptedException e) {
//                System.out.println("发生异常" +e.getMessage());
//            }
//        });

//        // 将任务提交到group线程池
//        GROUP.submit(new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                // 接收客户端消息
//                ByteBuf buf = (ByteBuf) msg;
//                byte[] bytes = new byte[buf.readableBytes()];
//                buf.readBytes(bytes);
//                String body = new String(bytes, "UTF-8");
//                TimeUnit.SECONDS.sleep(10);
//                System.out.println("group.submit 的call 线程是" + Thread.currentThread().getName());
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端", CharsetUtil.UTF_8));
//                return null;
//            }
//        });

        // 普通方式
        // 接收客户端消息
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");
        System.out.println(body);
        TimeUnit.SECONDS.sleep(10);
        System.out.println("group.submit 的call 线程是" + Thread.currentThread().getName());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端", CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}