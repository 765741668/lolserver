package lumbdar;

/**
 * Description :
 * Created by YangZH on 2017/2/20
 * 23:13
 */

interface interface1 {
    void test(int a);
}

/**
 * Description :
 * Created by YangZH on 2017/2/20
 * 23:13
 */

public class TestLumbdar2 {
    static interface1 t;

    public static void main(String[] args) {
        Bean1 b1 = new Bean1();
        Bean2 b2 = new Bean2();


        b1.boot(b2::processor);
//       b1.boot(new interface1() {
//            @Override
//            public void test(int a) {
//                b2.processor(a);
//            }
//        });
        t.test(11111);
    }

}

class Bean1 {

    public void boot(interface1 a) {
        TestLumbdar2.t = a;
    }
}

class Bean2 {
    public void processor(int a) {
        System.out.println(a);
    }
}
