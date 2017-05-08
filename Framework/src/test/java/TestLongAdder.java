/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  15:01
 */

import java.util.concurrent.atomic.LongAdder;

/**
 * Description :
 * Created by YangZH on 2017/4/12
 * 15:01
 */

public class TestLongAdder {
    public static void main(String[] args) {
        LongAdder la = new LongAdder();
        la.increment();
        la.increment();
        la.increment();
        System.out.println(la.longValue());
        System.out.println(la.longValue());
        la.decrement();
        System.out.println(la.intValue());
    }
}
