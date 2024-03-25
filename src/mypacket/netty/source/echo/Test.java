package mypacket.netty.source.echo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        System.out.println(test+"    1");
                test.a(test);

        TimeUnit.SECONDS.sleep(1);
        Test test1 = new Test();
        System.out.println(test1+ "    2");

        test1.a(test1);

        TimeUnit.SECONDS.sleep(1);
        test.a(test1 );
        TimeUnit.SECONDS.sleep(1);
        test1.a(test);
    }

    public void a(Test test) {
        System.out.println(Test.this + "  3");
        boolean b = Test.this == (test);
        System.out.println( b + "      4");
                new Thread(()->{
                    System.out.println(Test.this + "  5");
                    boolean b1 = Test.this == (test);
                    System.out.println(b1 + "    6");
                }).start();
    }
}
