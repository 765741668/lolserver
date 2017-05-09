/*
 * @(#)ObjectUtil.java        1.0 2009-8-11
 *
 * Copyright (c) 2007-2009 Shanghai Handpay IT, Co., Ltd.
 * 16/F, 889 YanAn Road. W., Shanghai, China
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Shanghai Handpay IT Co., Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use 
 * it only in accordance with the terms of the license agreement you 
 * entered into with Handpay.
 */

package com.lol.fwk.util;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 *
 * Description : 对象的工具类.
 * Created by YangZH on 2014/9/11
 *  15:25
 *
 */
public class ObjectUtil {
    /**
     * 判断字符串是否为空
     *
     * @param str
     *            字符串
     * @return 是否为空
     */
    public static boolean isNullStr(String str) {
        return (str == null || "".equals(str.trim()) || "null".equals(str.trim()) || "undefined".equals(str.trim()));
    }

    /**
     * 判断集合是否为空
     * @param c
     * @return
     */
    public static boolean isNotNullCollection(Collection c) {
        return (c != null && !c.isEmpty() && c.size()>0 );
    }

    /**
     * 将类似￥1280.00元转化为金额1280.00
     *
     * @param amountStr
     *            金额字符串
     * @return 金额数值
     */
    public static double getAmount(String amountStr) {
        String str = amountStr;
        if (amountStr.contains("￥")) {
            str = amountStr.substring("￥".length());
        }
        if (str.contains("元")) {
            str = str.substring(0, str.indexOf("元"));
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 将以元为单位的金额转化为以分为单位的金额
     *
     * @param amount
     *            以元为单位的金额
     * @return 以分为单位的金额
     */
    public static int moneyConvertYuan2Fen(double amount) {
        if (amount > 0)
            return (int) (amount * 100 + 0.5d);
        else
            return (int) (amount * 100 - 0.5d);
    }

    /**
     * 将以分为单位的金额转化为以元为单位的金额
     *
     * @param amount
     *            以分为单位的金额
     * @return 以元为单位的金额
     */
    public static double moneyConvertFen2Yuan(Integer amount) {
        if(amount == null){
            return 0.00d;
        }
        return (double) (amount / 100.00d);
    }

    /**
     * 将模板template中所有出现$key的字串替换为substitute
     *
     * @param template
     *            模板字符串
     * @param findStr
     *            匹配子串
     * @param substitute
     *            替换子串
     * @return 替换完成后的字符串
     */
    public static String replaceSubString(String template, String findStr,
                                          String substitute) {
        String result = template.toString();
        while (true) {
            int index = result.indexOf(findStr);
            if (index == -1)
                break;

            result = result.substring(0, index) + substitute
                    + result.substring(index + findStr.length());
        }

        return result;
    }

    /**
     * 获得当前日期的格式化表示
     *
     * @param formatStr
     *            格式化字符串
     * @return
     */
    public static String getCurrentDateStr(String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(new Date());
    }

    /**
     * 对象属性拷贝 拷贝非null的属性
     *
     * @param dest
     *            目标对象
     * @param src
     *            源对象
     */
    @SuppressWarnings("unchecked")
    public static void copyProperties(Object dest, Object src) {
        PropertyUtilsBean util = new PropertyUtilsBean();
        PropertyDescriptor[] srcProps = util.getPropertyDescriptors(src
                .getClass());
        for (PropertyDescriptor srcProp : srcProps) {
            Method srcMethod = srcProp.getReadMethod();
            Object srcValue = null;
            try {
                srcValue = srcMethod.invoke(src);
            } catch (Exception ex) {
                continue;
            }
            if (srcValue == null) {
                continue;
            }
            Class type = srcProp.getPropertyType();
            String merthodName = "set"
                    + srcProp.getName().substring(0, 1).toUpperCase()
                    + srcProp.getName().substring(1);
            Method destMethod = null;
            try {
                destMethod = dest.getClass().getMethod(merthodName, type);
            } catch (Exception ex) {
                continue;
            }
            if (destMethod == null) {
                continue;
            }
            try {
                destMethod.invoke(dest, srcValue);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * 将整数转化为定长的字符串
     *
     * @param value
     *            整数
     * @param strLength
     *            字符串长度
     * @return
     */
    public static String convertInt2String(Integer value, int strLength) {
        if (value == null) {
            return null;
        }

        String result = value.toString();
        if (result.length() < strLength) {
            int count = strLength - result.length();
            for (int i = 0; i < count; i++) {
                result = "0" + result;
            }
        }

        return result;
    }

    /**
     * 将整数转化为定长的字符串
     *
     * @param value
     *            整数
     * @param strLength
     *            字符串长度
     * @return
     */
    public static String convertInt2String(Long value, int strLength) {
        if (value == null) {
            return null;
        }

        String result = value.toString();
        if (result.length() < strLength) {
            int count = strLength - result.length();
            for (int i = 0; i < count; i++) {
                result = "0" + result;
            }
        }

        return result;
    }
    /**
     * 转换数字型的字符串，去掉无用的"0",如：00004
     *
     * @param value
     * @return
     */
    public static Long String2Long(String value){
        if (value == null) {
            return null;
        }
        while(value.startsWith("0") && value.length() > 1){
            value = value.substring(1);
        }
        return Long.valueOf(value);
    }

    /**
     * 将二进制的字符串转化为字符串
     *
     * @param hexStr
     *            二进制的字符串
     * @return
     */
    public static String convertHexStr2Str(String hexStr) {
        if (isNullStr(hexStr)) {
            return null;
        }

        int length = hexStr.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            byte high = convertChar2Digit(hexStr.charAt(i * 2));
            byte low = convertChar2Digit(hexStr.charAt(i * 2 + 1));
            bytes[i] = (byte) ((high << 4) + low);
        }

        return new String(bytes);
    }

    /**
     * 将数字字符转化为数字
     *
     * @param value
     * @return
     */
    private static byte convertChar2Digit(char value) {
        if (value >= '0' && value <= '9') {
            return (byte) (value - '0');
        }

        if (value >= 'a' && value <= 'f') {
            return (byte) (10 + value - 'a');
        }

        if (value >= 'A' && value <= 'F') {
            return (byte) (10 + value - 'A');
        }

        return 0;

    }

    /**
     * 产生随机数字符串
     *
     * @param digitNumber
     *            字符串的长度
     * @param modeNum
     *            模数
     * @return
     */
    public static String createRandomDigitStr(int digitNumber, int modeNum) {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < digitNumber; i++) {
            buffer.append(rand.nextInt(modeNum));
        }
        return buffer.toString();
    }

    /**
     * 格式化金额 以元为单位
     *
     * @param amount
     *            金额(必须能转成Number型的)
     * @return 30格式成30.00返回
     */
    public static String formartNumber(Object amount) {
        BigDecimal b = new BigDecimal("" + amount);
        return String.format("%.2f", b.doubleValue());
    }

    /**
     * @param request
     * @return 访问者的IP
     */
//    public static String getRemoteIpAddr(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("x-real-ip");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//
//        return ip;
//    }

    /**
     * 判断是否是一个有效的身份证
     *
     * @param idNo
     *            身份证号
     * @return 如果是(true),否则(false)
     */
    public static boolean isIdCard(String idNo) {
        return !isNullStr(idNo) && Pattern.matches("([0-9]{14}||[0-9]{17})[0-9a-zA-Z]", idNo);
    }

    /**
     * 判断是否是一个有效的用户名(最长为50位)
     *
     * @param name
     *            用户名
     * @return 如果是(true),否则(false)
     */
    public static boolean isValidName(String name) {
        return !isNullStr(name) && Pattern.matches("[\u4e00-\u9fa5a-zA-Z0-9]{1,15}", name);
    }

    /**
     * 判断是否是为数字
     *
     * @param number
     *            数字
     * @return 如果是(true),否则(false)
     */
    public static boolean isNumber(String number) {
        return !isNullStr(number) && Pattern.matches("[0-9]*", number);
    }

    /**
     * 判断是否是为字母
     *
     * @param letters
     *            大小写字母的字符串
     * @return 如果是(true),否则(false)
     */
    public static boolean isLetters(String letters) {
        return !isNullStr(letters) && Pattern.matches("[A-Za-z]*", letters);
    }

    /**
     * 判断是否是为字母,和数字的组成
     *
     * @param str
     * @return 如果是(true),否则(false)
     */
    public static boolean isLettersAndNumber(String str) {
        return !isNullStr(str) && Pattern.matches("[A-Za-z0-9]*", str);
    }

    /**
     * 判断是否是为字母,和数字的组成
     *
     * @param mobile
     *            手机号
     * @return 如果是(true),否则(false)
     */
    public static boolean isMobile(String mobile) {
        return !isNullStr(mobile) && Pattern.matches("[1][0-9]{10}", mobile);
    }

    /**
     * 将固有格式的字符串转化为一个map 字符串格式为 “a=b|c=d|e=f” a、c、e为相应的键 b、d、f为相应的值
     *
     * @param str
     * @return
     */
    public static Map<String, String> string2Map(String str, String splitStr)
            throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        if (ObjectUtil.isNullStr(str)) {
            return null;
        } else {
            if (!str.contains("=")) {
                throw new Exception("Params style error[" + str + "]");

            }

            StringTokenizer token = new StringTokenizer(str, splitStr);
            int count = token.countTokens();
            for (int i = 0; i < count; i++) {
                String string = token.nextToken();
                if (ObjectUtil.isNullStr(string)) {
                    continue;
                }
                String[] ps = string.split("=");
                if (ps.length != 2) {
                    throw new Exception("Params style error[" + str + "]");
                }
                map.put(ps[0], ps[1]);
            }
        }
        return map;
    }

    /**
     * 把map转为String    key separator1 value separator2 key separator1 value
     * @param map
     * @param separator1
     * @param separator2
     * @return
     */
    public static String mapToString(Map<String,String> map,String separator1,String separator2){
        if(map == null){
            return "";
        }
        if(StringUtils.isBlank(separator1)){
            return "";
        }
        if(StringUtils.isBlank(separator2)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Set<String> set = map.keySet();
        for (String key : set) {
            String value = map.get(key);
            sb.append(key).append(separator1).append(value).append(separator2);
        }
        if(sb.length() > 0){
            sb.setLength(sb.length()-separator2.length());
        }
        return sb.toString();
    }

    /**
     * 生成随机 xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     * ID字符串
     * @return
     */
    public static String getUUID()
    {
        return UUID.randomUUID().toString().toUpperCase();
    }


    /**
     * 快速排序法
     * @param list
     * @return
     */
    public static List<Integer> quickSort(List<Integer> list){
        Integer mid = list.get(new Random().nextInt(list.size()));
        mid = list.get(5);
        List<Integer> small = new ArrayList<Integer>();
        List<Integer> big = new ArrayList<Integer>();

        for (Integer aList : list) {
            if (aList <= mid) {
                small.add(aList);
            } else {
                big.add(aList);
            }
        }
        list.clear();
        if(list.size()>2){
            list.addAll(quickSort(big));
            list.addAll(quickSort(small));
        }else{
            list.addAll(big);
            list.addAll(small);
        }
        return list;
    }
    /**
     * 数组插入排序
     * @param nums
     * @return
     */
    public static int[] insertSort(int[] nums){
        int temp ;
        for(int i = 1;i<nums.length;i++){
            int j = i-1;
            temp = nums[i];
            while(j>=0&&temp>nums[j]){
                temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
                j--;
            }
        }

        return nums;
    }
    /**
     * list插入排序
     * @param list
     * @return
     */
    public static List<Integer> insertSort(List<Integer> list){
        int temp ;
        for(int i = 1;i <list.size();i++){
            int j = i-1;
            temp = list.get(i);
            while(j >= 0 && temp > list.get(j)){
                temp = list.get(j);
                list.set(j,list.get(i));
                list.set(i,list.get(j));
                j--;
            }
        }

        return list;
    }

    /**
     * 冒泡排序
     * @param nums
     * @return
     */
    public static int[] sort(int[] nums){
        int temp = 0;
        for(int i = 0; i < nums.length-1; i++){
            for(int j = i+1 ;j < nums.length; j++){
                if(nums[j]>nums[i]){
                    temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }
        }
        return nums;
    }

    /**
     * 数组转list
     * @param nums
     * @return
     */
    public static List<Integer> toList(int[] nums){
        List<Integer> list = new ArrayList<Integer>();
        for (int num : nums) {
            list.add(num);
        }
        return list;
    }

    /**
     * 将ElementNSImpl dataset数据类型转换成List map 数据类型
     * 返回二维list
     */
    public static List element2map(ElementNSImpl body){
        List list = new ArrayList();
        List lines ;
        Node college = body.getFirstChild();
        if (college != null && college.getNodeType() == Node.ELEMENT_NODE) {
            NodeList classNodes = college.getChildNodes();
            if (classNodes == null) return null;
            for (int j = 0; j < classNodes.getLength(); j++) {
                Map map = new HashMap();
                lines = new ArrayList();
                Node clazz = classNodes.item(j);
                if (clazz != null && clazz.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList studentNodes = clazz.getChildNodes();
                    if (studentNodes == null) continue;
                    for (int k = 0; k < studentNodes.getLength(); k++) {
                        Node student = studentNodes.item(k);
                        if (student != null && student.getNodeType() == Node.ELEMENT_NODE) {
                            map.put(student.getNodeName(),student.getFirstChild().getNodeValue());
//                            System.err.println(student.getFirstChild().getNodeValue());
                        }
                    }

                }
                lines.add(map);
                list.add(lines);
            }
        }
        return list;
    }

    private static void main(String[] obj){
        System.out.println(getUUID());
    }
}
