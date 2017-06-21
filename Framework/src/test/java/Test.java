/**
 * Description : 
 * Created by YangZH on 2017/3/21
 *  1:31
 */

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 * Created by YangZH on 2017/3/21
 * 1:31
 */

public class Test {
    public static void main(String[] args) {
        int[][] aa = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        for (int[] a : aa) {
            System.out.println(a[0]);
        }
        System.out.println( 1<< 30);
    }


}


class PrintStream2 extends PrintStream {

    public PrintStream2(OutputStream out) {
        super(out);
    }

    public void println(String x) {
        synchronized (this) {
            print(x);
//            super.newLine();
        }
    }
}
