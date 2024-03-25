package mypacket.netty.dubborpc.provider;

import mypacket.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService{

    private int cnt = 0;

    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息=" +  msg);
        // 根据msg返回不同的结果
        if (msg != null) {
            return "你好客户端，我已经收到你的消息[" + msg + "] 第"+ (++cnt) + "次";
        } else {
            return "你好客户端，我已经收到你的消息";
        }
    }
}
