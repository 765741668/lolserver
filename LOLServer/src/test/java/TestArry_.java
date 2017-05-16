/**
 * Description : 
 * Created by YangZH on 2017/2/20
 *  1:27
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Description :
 * Created by YangZH on 2017/2/20
 * 1:27
 */

public class TestArry_ {

    public static void main(String[] args) {
        Comparable c = x->0;

        Comparator c2 = (o1, o2) -> o2.hashCode()-o1.hashCode();

        List a = new ArrayList<>();
        for (int i=3;i >= 0;i--){
            a.add("abcd".charAt(i));
        }

        System.out.println(a.toString());

        a.stream().sorted((o1, o2) -> o2.hashCode()-o1.hashCode()).forEach(f -> System.out.println(f));
    }
}
