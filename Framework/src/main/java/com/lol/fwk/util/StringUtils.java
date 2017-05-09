package com.lol.fwk.util;

import java.util.UUID;

/**
 * 字符串工具类
 * @author YangZH
 *
 */
public class StringUtils {

    final static String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    final static String wordsAll = new PropertiesUtil().getWordsProperties("wordsAll");
    final static String wordsAllTw = new PropertiesUtil().getWordsProperties("wordsAllTw");
    final static String wordsTw = new PropertiesUtil().getWordsProperties("wordsTw");
    final static String wordsJa = new PropertiesUtil().getWordsProperties("wordsJa");
    /**
     * 随机生成数字/字母
     * @return
     */
    public static char randomChar(){
        Double random = Math.random();
        random = random * 10000;
        int index = Math.abs(random.intValue());
        index = index % words.length();
        return words.charAt(index);
    }

    /**
     * 随机生成生僻字
     * @return
     */
    public static char randomWNewTw(){
        Double random = Math.random();
        random = random * 100;
        int index = Math.abs(random.intValue());
        index = index % wordsTw.length();
        return wordsTw.charAt(index);
    }
    /**
     * 随机生成生日语片假名、平假名
     * @return
     */
    public static char randomWJa(){
        Double random = Math.random();
        random = random * 100;
        int index = Math.abs(random.intValue());
        index = index % wordsJa.length();
        return wordsJa.charAt(index);
    }

    /**
     * 随机生成繁体字
     * @return
     */
    public static char randomWAllTw(){
        Double random = Math.random();
        random = random * 10000;
        int index = Math.abs(random.intValue());
        index = index % wordsAllTw.length();
        return wordsAllTw.charAt(index);
    }

    /**
     * 随机生成简体字
     * @return
     */
    public static char randomWAll(){
        Double random = Math.random();
        random = random * 10000;
        int index = Math.abs(random.intValue());
        index = index % wordsAll.length();
        return wordsAll.charAt(index);
    }

    /**
     * 数据生成数字/字母字符串
     * @param length 字符串长度
     * @return
     */
    public static String randomCharString(int length)
    {
        char[] result = new char[length];
        for(int i=0;i<length;i++)
        {
            result[i] = randomChar();
        }
        return new String(result);
    }

    /**
     * 数据生成所有生僻字符串
     * @param length 字符串长度
     * @return
     */
    public static String randomNewTwString(int length)
    {
        char[] result = new char[length];
        for(int i=0;i<length;i++)
        {
            result[i] = randomWNewTw();
        }
        return new String(result);
    }
    /**
     * 数据生成所有日语字符串
     * @param length 字符串长度
     * @return
     */
    public static String randomJaString(int length)
    {
        char[] result = new char[length];
        for(int i=0;i<length;i++)
        {
            result[i] = randomWJa();
        }
        return new String(result);
    }

    /**
     * 数据生成所有繁体字符串
     * @param length 字符串长度
     * @return
     */
    public static String randomAllTwString(int length)
    {
        char[] result = new char[length];
        for(int i=0;i<length;i++)
        {
            result[i] = randomWAllTw();
        }
        return new String(result);
    }

    /**
     * 数据生成所有简体字符串
     * @param length 字符串长度
     * @return
     */
    public static String randomWAllString(int length)
    {
        char[] result = new char[length];
        for(int i=0;i<length;i++)
        {
            result[i] = randomWAll();
        }
        return new String(result);
    }
    /**
     * 0繁转简，1简转繁
     * @param st
     * @param n
     * @return
     */
    public static String WordsConver(String st, int n){
        if (n == 0){
            return traditionalized(st);
        }else{
            return simplized(st);
        }
    }
    /**
     * 简转繁
     * @param st
     * @return
     */
    private static String simplized(String st){
        String stReturn = "";
        for(int i=0;i<st.length();i++){
            char temp = st.charAt(i);
            if(wordsAll.indexOf(temp)!=-1)
                stReturn+=wordsAllTw.charAt(wordsAll.indexOf(temp));
            else
                stReturn+=temp;
        }
        return stReturn;
    }
    /**
     * 繁转简
     * @param st
     * @return
     */
    private static String traditionalized(String st){
        String stReturn = "";
        for(int i=0;i<st.length();i++){
            char temp = st.charAt(i);
            if(wordsAllTw.indexOf(temp)!=-1)
                stReturn+=wordsAll.charAt(wordsAllTw.indexOf(temp));
            else
                stReturn+=temp;
        }
        return stReturn;
    }

    /**
     * 生成随机ID
     * @return
     */
    public static String getUUID()
    {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args)
    {
        System.out.println(getUUID());
        System.out.println(WordsConver("谁", 1));

    }
}
