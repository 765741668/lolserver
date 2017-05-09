package com.lol.fwk.util;

import java.lang.reflect.*;
import java.util.*;

/**
 * 
 * 对比两个对象属性值的不同，如对象obj1:A取值1;对象obj2:A取值2，则compare返回map集合，且其中元素(A,false)
 * 
 * @version 1.0 2013-5-14
 * @author david
 * @history
 * 
 */
public class BeansCompare {
	public static <T> Map<String, String> compare(T obj1, T obj2)
			throws Exception {

		Map<String, String> result = new HashMap<String, String>();

		Field[] fs = obj1.getClass().getDeclaredFields();
		for (Field f : fs) {
			f.setAccessible(true);
			Object v1 = f.get(obj1);
			Object v2 = f.get(obj2);
			result.put(f.getName(), String.valueOf(equals(v1, v2)));
		}
		return result;
	}

	public static boolean equalVals(Object obj1, Object obj2) throws Exception {
		Field[] fs = obj1.getClass().getDeclaredFields();
		boolean equal = true;
		for (Field f : fs) {
			f.setAccessible(true);
			Object v1 = f.get(obj1);
			Object v2 = f.get(obj2);
			if (!equals(v1, v2)) {
				//equal = false;
				return false;
			}
		}
		return equal;
		// return true;
	}
    //返回bean属性名
    public static <T> List<String> getCulumName(T o) throws Exception {
		List<String> list = new ArrayList<>();
        Field[] fs = o.getClass().getDeclaredFields();
		for (Field f : fs) {
            list.add(f.getName());
		}
		return list;
		// return true;
	}

	public static boolean equals(Object obj1, Object obj2) {

		if (obj1 == obj2) {
			return true;
		}
		if (isEmpty(obj1) && isEmpty(obj2)) {
			return true;
		}
		if (obj1 == null && obj2 != null) {
			return false;
		}
		if (obj1 != null && obj2 == null) {
			return false;
		}
		return obj1.equals(obj2);
	}

	public static boolean isEmpty(Object obj) {
		if (obj == null || String.valueOf(obj).equals("")
				|| String.valueOf(obj).equals("国家")
				|| String.valueOf(obj).equals("请选择")
				|| String.valueOf(obj).equals("null")
				|| String.valueOf(obj).equals("Null")
				|| String.valueOf(obj).equals("省份、州")
				|| String.valueOf(obj).equals("地级市、县")) {
			return true;
		}
		return false;
	}

    /**
     * 遍历实体类的属性和数据类型以及属性值
     * @param model
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static Map<String,Object> getBeanNameAndValue(Object model) throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Map<String,Object> map = new HashMap<>();

        // 获取实体类的所有属性，返回Field数组
        Field[] field = model.getClass().getDeclaredFields();
        // 遍历所有属性
        for (Field aField : field) {
            // 获取属性的名字
            String name = aField.getName();
            // 将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            // 获取属性的类型
            String type = aField.getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
//            System.out.println("属性为：" + name);
            if (type.equals("class java.lang.String")) {
                Method m = model.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                String value = (String) m.invoke(model);
//                System.out.println("数据类型为：String");
                if (value != null) {
                    map.put(model.getClass().getSimpleName()+name,value);
//                    System.out.println("属性值为：" + value);
                } else {
//                    System.out.println("属性值为：空");
                }
            } else
            if (type.equals("class java.lang.Integer")) {
                Method m = model.getClass().getMethod("get" + name);
                Integer value = (Integer) m.invoke(model);
//                System.out.println("数据类型为：Integer");
                if (value != null) {
                    map.put(model.getClass().getSimpleName()+name,value);
//                    System.out.println("属性值为：" + value);
                } else {
//                    System.out.println("属性值为：空");
                }
            }else
            if (type.equals("class java.lang.Short")) {
                Method m = model.getClass().getMethod("get" + name);
                Short value = (Short) m.invoke(model);
//                System.out.println("数据类型为：Short");
                if (value != null) {
                    map.put(model.getClass().getSimpleName()+name,value);
//                    System.out.println("属性值为：" + value);
                } else {
//                    System.out.println("属性值为：空");
                }
            }else
            if (type.equals("class java.lang.Double")) {
                Method m = model.getClass().getMethod("get" + name);
                Double value = (Double) m.invoke(model);
//                System.out.println("数据类型为：Double");
                if (value != null) {
                    map.put(model.getClass().getSimpleName()+name,value);
//                    System.out.println("属性值为：" + value);
                } else {
//                    System.out.println("属性值为：空");
                }
            }else
            if (type.equals("class java.lang.Boolean")) {
                Method m = model.getClass().getMethod("get" + name);
                Boolean value = (Boolean) m.invoke(model);
//                System.out.println("数据类型为：Boolean");
                if (value != null) {
                    map.put(model.getClass().getSimpleName()+name,value);
//                    System.out.println("属性值为：" + value);
                } else {
//                    System.out.println("属性值为：空");
                }
            }else
            if (type.equals("class java.util.Date")) {
                Method m = model.getClass().getMethod("get" + name);
                Date value = (Date) m.invoke(model);
//                System.out.println("数据类型为：Date");
                if (value != null) {
                    map.put(model.getClass().getSimpleName()+name,value);
//                    System.out.println("属性值为：" + value);
                } else {
//                    System.out.println("属性值为：空");
                }
            }else

            if (type.equals("double")) {
                Method m = model.getClass().getMethod("get" + name);
                double value = (double) m.invoke(model);
//                System.out.println("数据类型为：double");
                if (value > 0) {
                    map.put(model.getClass().getSimpleName()+name,value);
//                    System.out.println("属性值为：" + value);
                } else {
//                    System.out.println("属性值为：空");
                }
            }else{
                //type： java.util.List<com.zds.service.bean.RoleBean>
//                Method m = model.getClass().getMethod("get" + name);
//                double value = (double) m.invoke(model);
                System.out.println("属性类型为：" + type);
//                map.put(model.getClass().getSimpleName()+type,value);
            }
        }

        return map;
    }
    /**
     * 通过反射获取泛型的类型
     * 返回 实体类的除基本数据类型外的属性名和对应的clazz（实体成员属性，集合成员属性）
     * @param clazz
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static Map<String,Class> getBeanNameHard(Class clazz) throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Field[] fs = clazz.getDeclaredFields(); // 得到所有的fields
        Map<String,Class> map = new HashMap<>();
        for(Field f : fs)
        {
            //得到field的class及类型全路径
            Class fieldClazz = f.getType();
            //【1】 //判断是否为基本类型
            if(fieldClazz.isPrimitive())  continue;
            //getName()返回field的类型全路径；
            if(fieldClazz.getName().startsWith("java.lang")) continue;
            //【2】
            if(fieldClazz.isAssignableFrom(List.class)
                    ||fieldClazz.isAssignableFrom(Set.class)
                    ||fieldClazz.isAssignableFrom(ArrayList.class)
                    ||fieldClazz.isAssignableFrom(HashSet.class))
            {
                // 关键的地方，如果是List类型，得到其Generic的类型
                Type fc = f.getGenericType();

                if(fc == null) continue;
                // 【3】如果是泛型参数的类型
                if(fc instanceof ParameterizedType)
                {
                    ParameterizedType pt = (ParameterizedType) fc;
                    //【4】 得到泛型里的class类型对象。
                    Class genericClazz = (Class)pt.getActualTypeArguments()[0];
                    map.put(f.getName(), genericClazz);
                }
            }
        }

        return map;
    }

}
