/**
 * Description : 
 * Created by YangZH on 2017/5/30
 *  23:21
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description : 反射泛型
 *   ParameterizedType:表示一种参数化的类型，比如Collection< String >
     GenericArrayType:表示一种元素类型是参数化类型或者类型变量的数组类型
     TypeVariable:是各种类型变量的公共父接口
     WildcardType:代表一种通配符类型表达式，比如？、？ extends Number、？ super Integer。（wildcard是一个单词：就是”通配符“）
 * Created by YangZH on 2017/5/30
 * 23:21
 */

public class TestReflex extends ClassA<String> {
    private List<ClassA> list;
    private Map<String, ClassA> map;

    /***
     * 获取List中的泛型
     */
    public static void testList() throws NoSuchFieldException, SecurityException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Type t = TestReflex.class.getDeclaredField("list").getGenericType();
//        if(t instanceof ParameterizedType){
        if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
            for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
                System.out.print(t1 + ",");
                Class<?> clazz = Class.forName(t1.getTypeName());
                clazz.getDeclaredMethod("testPrint",String.class).invoke(clazz.newInstance(),"gadsgsdgsdg");

                Class<?> clazz2 = ClassLoader.getSystemClassLoader().loadClass(t1.getTypeName());
                clazz2.getMethod("testPrint2").invoke(clazz.newInstance());
            }
            System.out.println();
        }
    }

    /***
     * 获取Map中的泛型
     */
    public static void testMap() throws NoSuchFieldException, SecurityException {
        Type t = TestReflex.class.getDeclaredField("map").getGenericType();
        if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
            for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
                System.out.print(t1 + ",");
            }
            System.out.println();

        }
    }

    public static void main(String args[]) throws Exception {
        System.out.println(">>>>>>>>>>>testList>>>>>>>>>>>");
        testList();
        System.out.println("<<<<<<<<<<<testList<<<<<<<<<<<\n");
        System.out.println(">>>>>>>>>>>testMap>>>>>>>>>>>");
        testMap();
        System.out.println("<<<<<<<<<<<testMap<<<<<<<<<<<\n");
        System.out.println(">>>>>>>>>>>testClassA>>>>>>>>>>>");
        new TestReflex().testClassA();
        System.out.println("<<<<<<<<<<<testClassA<<<<<<<<<<<");
        ArrayList<Integer> list = new ArrayList<Integer>();
        Method method = list.getClass().getMethod("add", Object.class);
        method.invoke(list, "Java反射机制实例。");
        System.out.println(list.get(0));

        //调用这个方法的class或接口 与 参数cls表示的类或接口相同，或者是参数cls表示的类或接口的父类，则返回true
        System.out.println(ArrayList.class.isAssignableFrom(Object.class));  //false
        System.out.println(Object.class.isAssignableFrom(ArrayList.class));  //true

        //被测试的对象，如果obj是调用这个方法的class或接口 的实例，则返回true。这个方法是instanceof运算符的动态等价
        String s= "javaisland";
        System.out.println(String.class.isInstance(s)); //true

        //nstanceof运算符 只被用于对象引用变量，检查左边的被测试对象 是不是 右边类或接口的 实例化。如果被测对象是null值，则测试结果总是false。
        String s2= "javaisland";
        System.out.println(s2 instanceof String); //true
    }

}

class  ClassA <T>{

    private T obj;

    public void testPrint(String arg){
        System.out.println("~~~~~~~~~~testPrint : "+ arg +"~~~~~~~~~~");
    }
    public void testPrint2(){
        System.out.println("~~~~~~~~~~testPrint~~~~~~~~~~");
    }

    /**
     * 获取T的实际类型
     */
    public void testClassA() throws NoSuchFieldException, SecurityException {
        System.out.print("getSuperclass:");
        System.out.println(this.getClass().getSuperclass().getName());
        System.out.print("getGenericSuperclass:");
        Type t = this.getClass().getGenericSuperclass();
        System.out.println(t);
        if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
            System.out.print("getActualTypeArguments:");
            for (Type t1 : ((ParameterizedType) t).getActualTypeArguments()) {
                System.out.print(t1 + ",");
            }
            System.out.println();
        }
    }
}
