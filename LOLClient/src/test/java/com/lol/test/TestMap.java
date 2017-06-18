package com.lol.test;/**
 * Description : 
 * Created by YangZH on 2017/6/17
 *  14:52
 */


import java.util.*;

/**
 * Description :
 * Created by YangZH on 2017/6/17
 * 14:52
 */

public class TestMap {

    /**
     map 中有{张三=3，李四=7，王五=9，刘六=5，田七=7，周七=3}这些元素，定义
     一个list，将map中value值相同的元素从该map集合中去掉，将剩余的元素中的
     姓名存入到该list中然后打印到控制台上。 （题目中的去重，不是去除所有重复元素，而是去除一个重复元素，保留一个）
     */
    public static void main(String[] args) {
        Map<String, Map> map = new HashMap<>();
        map.put("a",map2());
        map.put("b",map22());
        System.out.println(map);

        for(Map m : map.values()){
            distinct(m);
        }
        System.out.println(map);
    }

    static void distinct(Map<String, Map> map){
        //创建list,先接收键，相当于数组中的索引
        List<String> list = new ArrayList<>();
        for (String s : map.keySet()) {
            list.add(s);
        }

        //创建新的集合接收具有相同值的键，只留一个
        TreeSet<String> ts = new TreeSet<>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = i+1; j < map.size(); j++) {
                if (map.get(list.get(i)).equals(map.get(list.get(j)))
                        && (!map.get(list.get(i)).containsKey("selected")
                        || !map.get(list.get(j)).containsKey("selected"))) {
                    ts.add(list.get(j));    //如果有相同的，只留下一个，留哪个最后就会去除哪个
                    ts.add(list.get(i));
                }
            }
        }

        //遍历新集合，将原集合中这些键去除
        for (String s : ts) {
            map.remove(s);
            list.remove(s);//同时移除list中相应的值
        }
        System.out.println(map);

        //打印list
        System.out.println(list);
    }

    static void test(){
        Map<String, String> maps = new HashMap<String, String>();//数据源

        maps.put("张三", "3");
        maps.put("李四", "7");
        maps.put("王五", "9");
        maps.put("刘六", "5");
        maps.put("田七", "7");
        maps.put("周七", "3");

        Map<String, String> map = new TreeMap<String, String>();//接收的集合

        for(Map.Entry<String, String> entry : maps.entrySet()) {

            if(map.get(entry.getValue()) != null)
            {
                map.put(map.get(entry.getValue()) + 1,entry.getValue());
            }else {
                map.put(entry.getKey(),entry.getValue());
            }
        }

        for(Map.Entry<String, String> entrys : map.entrySet()) {
            System.out.println(entrys.getKey() + "........" + entrys.getValue());
        }
    }

    static Map map2(){
        Map<String,Map> map = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        map2.put("role_id","1");
        map2.put("memue_id","1");
        map2.put("selected","1");
        map2.put("name","1");
        Map<String,String> map22 = new HashMap<>();
        map22.put("role_id","1");
        map22.put("memue_id","1");
        map22.put("name","1");
        Map<String,String> map222 = new HashMap<>();
        map222.put("role_id","1");
        map222.put("memue_id","1");
        map222.put("name","1");
        Map<String,String> map2222 = new HashMap<>();
        map2222.put("role_id","3");
        map2222.put("memue_id","3");
        map2222.put("name","3");
        Map<String,String> map22222 = new HashMap<>();
        map22222.put("role_id","2");
        map22222.put("memue_id","2");
        map22222.put("name","2");

        map.put("1",map2);
        map.put("2",map22);
        map.put("3",map222);
        map.put("4",map2222);
        map.put("5",map22222);

        return map;
    }

    static Map map22(){
        Map<String,Map> map = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        map2.put("role_id","1");
        map2.put("memue_id","1");
        map2.put("selected","1");
        map2.put("name","1");
        Map<String,String> map22 = new HashMap<>();
        map22.put("role_id","1");
        map22.put("memue_id","1");
        map22.put("name","1");
        Map<String,String> map222 = new HashMap<>();
        map222.put("role_id","1");
        map222.put("memue_id","1");
        map222.put("name","1");
        Map<String,String> map2222 = new HashMap<>();
        map2222.put("role_id","3");
        map2222.put("memue_id","3");
        map2222.put("name","3");
        Map<String,String> map22222 = new HashMap<>();
        map22222.put("role_id","2");
        map22222.put("memue_id","2");
        map22222.put("name","2");

        map.put("1",map2);
        map.put("2",map22);
        map.put("3",map222);
        map.put("4",map2222);
        map.put("5",map22222);

        return map;
    }
}
