package com.lol.fwk.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Properties;

public class PropertiesUtil {
	
	public static Properties props = new Properties();
	
	private static String propertyWordsAddress = "/message/WordsProperties.properties";

    private static String debug = "debug";
	/**
	 * 取得文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
	/**
	 * 毫秒转换
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(long time){
		String strTime = "";
		if (time < 60) {
			strTime = time+ "秒";
		} else if (time < 60*60) {
			strTime = (time % (60 * 60)) / 60 + "分" + (time % 60) + "秒";
		} else if (time < 60*60*24) {
			strTime = (time % (60 * 60 * 24)) / (60 * 60) + "小时" + (time % (60 * 60)) / 60 + "分" + (time % 60) + "秒";
		} else {
			strTime = time / (60 * 60 * 24)+"天"+(time % (60 * 60 * 24)) / (60 * 60) + "小时" + (time % (60 * 60)) / 60 + "分" + (time % 60) + "秒";
		}
		return strTime;
	}
	
	/**
	 * 取得配置文件的信息
	 * 
	 * @param key
	 * @return
	 */
	public String getWordsProperties(String key) {
		String onlineUrl = null;
		try {
			InputStream is = this.getClass().getResourceAsStream(propertyWordsAddress);
			props.load(is);
			if (is != null) {
				is.close();
			}
			onlineUrl = props.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return onlineUrl;
	}
    public static String getCacheProperties(String key) {
		String onlineUrl = null;
		try {
            String propertyMemcahedAddress = "/spring/memcached.properties";
            InputStream is = PropertiesUtil.class.getResourceAsStream(propertyMemcahedAddress);
			props.load(is);
			if (is != null) {
				is.close();
			}
			onlineUrl = props.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return onlineUrl;
	}

	/**
	 * @return the debug
	 */
	public static String getDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public static void  setDebug(String debug) {
		PropertiesUtil.debug = debug;
		if(!debug.equals("debug")){
			propertyWordsAddress = "/properties/system_online.properties";
		}
	}

	
	public static void main(String args[]) {
	}
}

