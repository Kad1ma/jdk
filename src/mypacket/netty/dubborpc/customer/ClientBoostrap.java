package mypacket.netty.dubborpc.customer;

import mypacket.netty.dubborpc.netty.NettyClient;
import mypacket.netty.dubborpc.publicinterface.HelloService;

import java.util.concurrent.TimeUnit;

public class ClientBoostrap {

    private static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {
                    // 创建一个消费者
        NettyClient customer = new NettyClient();

        // 创建代理对象
        HelloService service;
        service = (HelloService) customer.getBean(HelloService.class, providerName);

        // 通过代理对象调用服务提供者的方法
        for (int i = 0; ;) {
            TimeUnit.SECONDS.sleep(5);
            String res = service.hello("你好，dubbo");
            System.out.println("调用的结果res="+res);
        }

    }
}
