/**
 * Description : 
 * Created by YangZH on 2017/2/20
 *  1:27
 */

import java.util.Arrays;
import java.util.List;

/**
 * Description :
 * Created by YangZH on 2017/2/20
 * 1:27
 */

public class TestArry_ {

    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        List b = Arrays.asList(a);
        b.stream().forEach(System.out::println);
        System.out.println("size: " + b.size());

        Integer[] a2 = {1, 2, 3};
        List b2 = Arrays.asList(a2);
        b2.stream().forEach(System.out::println);
        System.out.println("size: " + b2.size());

        String[] aa = {"1", "2", "3"};

        List bb = Arrays.asList(aa);
        bb.stream().forEach(System.out::print);
        System.out.println("size: " + bb.size());
    }
}
