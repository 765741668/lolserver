package com.lol.fwk.util;/**
 * Description : 
 * Created by YangZH on 2016/4/2
 *  23:56
 */

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Description :计算MD5或SHA-1，一样的就是同一个文件 下面的代码，不需要额外使用第三方组件，且支持超大文件
 * Created by YangZH on 2016/4/2
 * 23:56
 */

public class FileUtils {

    // 计算文件的 MD5 值
    public static String getFileMD5ByIO(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 计算文件的 SHA-1 值
    public static String getFileSha1ByIO(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
       String md5_1 = FileUtils.getFileMD5ByIO(new File("D:\\Administrator\\桌面\\WIN7下PS2等键盘失灵无法使用的解决办法.txt"));
       String md5_2 = FileUtils.getFileMD5ByIO(new File("C:\\Users\\YangZH\\AppData\\Local\\Temp\\WIN7下PS2等键盘失灵无法使用的解决办法1.txt"));
       System.out.println(md5_1);
       System.out.println(md5_2);
       System.out.println(md5_2.equals(md5_1));

       String sha_1_1 = FileUtils.getFileSha1ByIO(new File("D:\\Administrator\\桌面\\WIN7下PS2等键盘失灵无法使用的解决办法.txt"));
       String sha_1_2 = FileUtils.getFileSha1ByIO(new File("C:\\Users\\YangZH\\AppData\\Local\\Temp\\WIN7下PS2等键盘失灵无法使用的解决办法1.txt"));
       System.out.println(sha_1_1);
       System.out.println(sha_1_2);
       System.out.println(sha_1_2.equals(sha_1_1));



    }
}
