package com.lol.fwk.util;

import com.lol.fwk.exception.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 
 000发送成功！ -01当前账号余额不足！ -02当前用户ID错误！ -03当前密码错误！ -04参数不够或参数内容的类型错误！
 * -05手机号码格式不对！ -06短信内容编码不对！ -07短信内容含有敏感字符！ -8无接收数据 -09系统维护中..
 * -10手机号码数量超长！（100个/次 超100个请自行做循环） -11短信内容超长！（70个字符） -12其它错误！
 */

/**
 * ClassName: MessageUtil <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2014-4-4 下午3:19:05 <br/> 
 * 
 * @author Administrator 
 * @version  
 * @since JDK 1.6
 */

public class MessageUtil2 {
	private static final String x_id = "cqwangjian";
	private static final String x_pwd = "cqwj@123";
	public void sendSMS(String mobile, String content) throws ServiceException {
		HttpURLConnection httpconn = null;
		String result = "-20";
		content = content.replaceAll("JAVA手机短信测试", "测试看看");
		content += "【大蛇】";
		StringBuilder sb = new StringBuilder();
		sb.append("http://service.winic.org/sys_port/gateway/?");
		sb.append("id=").append(x_id);
		sb.append("&pwd=").append(x_pwd);
		sb.append("&to=").append(mobile);
		try {
			// 注意乱码的话换成gb2312编码
			sb.append("&content=").append(URLEncoder.encode(content, "gb2312"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			throw new ServiceException("发送信息转码失败!");
		}
		try {
			URL url = new URL(sb.toString());
			httpconn = (HttpURLConnection) url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpconn.getInputStream()));
			result = rd.readLine();
			rd.close();
			System.out.println(result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
				httpconn = null;
			}
		}
		}
	public static void main(String[] args) throws ServiceException {
		new MessageUtil2().sendSMS("18223349610","鞋特法克尤");
	}
}

