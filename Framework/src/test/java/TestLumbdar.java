/**
 * Description : 
 * Created by YangZH on 2017/4/12
 *  10:35
 */

import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

/**
 * Description :
 * Created by YangZH on 2017/4/12
 * 10:35
 */

public class TestLumbdar {
    /**
     * @param args
     * @author RANDYZHY
     * Sep 10, 2015 4:05:23 PM
     */
    public static void main(String[] args) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~1~~~~~~~~~~~~~~~~~~~~~~~~");
        //Consumer: call accept to set input arg and  return void with one input argument
        Consumer<String> cu = (c) -> setSomething(c);
        cu.accept("this is Consumer ,it will process logic and nothing return");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~11~~~~~~~~~~~~~~~~~~~~~~~~");
        //Consumer: call accept to set input arg and  return void with one input argument
        Consumer<String> cu2 = (c) -> setSomething2();
        cu2.accept(null);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~2~~~~~~~~~~~~~~~~~~~~~~~");
        //Predicate: call test to set input arg  and return boolean with one input argument
        Predicate<String> pu = (p) -> isTure(p);
        boolean _pu = pu.test("this is Predicate,it will return blooean");
        System.out.println(_pu);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~3~~~~~~~~~~~~~~~~~~~~~~~");
        //Function: call apply to set input arg with first arg and and it should be return with second arg
        Function<String, Integer> fu = f -> getSomething(f);
        Integer _fu = fu.apply("1111");
        System.out.println("Function: " + _fu);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~4~~~~~~~~~~~~~~~~~~~~~~~~");
        //Supplier:call get to return input arg type
        Supplier<String> su = () -> getSomething2("this is Supplier,it will return void with no arg or one ");
        String _su = su.get();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~44~~~~~~~~~~~~~~~~~~~~~~~~");
        //Supplier:call get to return input arg type
        Supplier<Integer> su2 = () -> getSomething();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~5~~~~~~~~~~~~~~~~~~~~~~~");
        BinaryOperator<String> bo = (x, y) -> getSomething2(x);
        String getbo = bo.apply("bo->a", "bo->b");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~6~~~~~~~~~~~~~~~~~~~~~~");
        //customer interface for return void and no any argument
        VoidNoArgInterface v = () -> setSomething();
        v.doNothing();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~7~~~~~~~~~~~~~~~~~~~~~~");
        VoidNoArgInterface v2 = () -> setSomething("hhh");
//        v2.doNothing();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~8~~~~~~~~~~~~~~~~~~~~~~");
        VoidNoArgInterface2<String, Integer> v3 = (x, y) -> setSomething(x, y);
        v3.doSomething("a", 100);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~9~~~~~~~~~~~~~~~~~~~~~~");
        VoidNoArgInterface3<String> v4 = (x, y) -> getSomething2(x);
//        v4.doSomething("a","b");


        Map<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "Val" + i);
        }

        map.forEach((key, val) -> {
            System.out.println(key + ":" + val);
        });

        String str = "圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈圈";
        System.out.println(str.length());

    }

    static void setSomething() {
        System.out.println("return void function with argument with no argument");
    }

    static void setSomething2() {
        System.out.println("accept...");
    }

    static void setSomething(Object x, Object y) {
        System.out.println("return void function with argument,arg1 :" + x + " , arg2 : " + y);
    }

    static Integer getSomething() {
        System.out.println("return String function with no argument");
        return 0;
    }

    static void setSomething(String arg) {
        System.out.println("return void function with argument,arg :" + arg);
    }

    static Integer getSomething(String arg) {
        System.out.println("return String function with argument,arg:" + arg);
        Integer result = null;
        try {
            result = Integer.valueOf(arg);
        } catch (Exception e) {
            System.out.println("input arg invalide , default to return null");
            result = null;
        }
        return result;
    }

    static String getSomething2() {
        System.out.println("return String function with no argument");
        return "nothing";
    }

    static String getSomething2(String arg) {
        System.out.println("return String function with argument,arg:" + arg);
        return arg;
    }

    static boolean isTure() {
        System.out.println("return boolean function with no argument");
        return false;
    }

    static boolean isTure(String arg) {
        System.out.println("return boolean function with argument,arg:" + arg);
        return arg.equals("this is Predicate,it will return blooean");
    }

    @FunctionalInterface
    interface VoidNoArgInterface {
        void doNothing();
    }

    @FunctionalInterface
    interface VoidNoArgInterface2<String, Integer> {
        void doSomething(String a, Integer b);
    }

    @FunctionalInterface
    interface VoidNoArgInterface3<String> {
        String doSomething(String a, String b);
    }

}
