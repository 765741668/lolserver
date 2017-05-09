package com.lol.fwk.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *  uid 	用户账号 	 
	pwd 	用户密码 	小写32位MD5加密
	time 	定时时间 	可选项，及时发送时参数无 格式:YYYY-MM-DD HH:MM　如："2010-05-27 12:01" (年-月-日 时:分),发送时间以北京时间为准
	mid 	子扩展号 	可选项，根据用户账号是否支持扩展
	encode 	字符编码 	可选项，默认接收数据是GBK编码,如提交的是UTF-8编码字符,需要添加参数 encode=utf8
	mobile 	接收号码 	同时发送给多个号码时,号码之间用英文半角逗号分隔(,)如:13972827282,13072827282,02185418874
	GET方式每次最多可以提交50条号码
	POST方式每次最多可以提交2000条号码[建议用POST方式提交]
	content 	短信内容 	发送内容需要进行URL字符标准化转码。
	{URL字符编码说明:返回字符串，此字符串中除了-_.之外的所有非字母数字字符都将被替换成百分号（%）后跟两位十六进制数，空格则编码为加号（+）}
	ASP：server.URLEncode("短信内容")
	PHP：urlencode("短信内容")
	JAVA：java.net.URLEncoder.encode("短信内容")
	
	100 　发送成功
	101 　验证失败
	102 　短信不足
	103 　操作失败
	104 　非法字符
	105 　内容过多
	106 　号码过多
	107 　频率过快
	108 　号码内容空
	109 　账号冻结
	110 　禁止频繁单条发送
	111 　系统暂定发送
	112 　号码错误
	113 　定时时间格式不对
	114 　账号被锁，10分钟后登录
	115 　连接失败
	116 　禁止接口发送
	120 　系统升级 
	
	四、短信发送
	GET/POST操作格式： http://api.sms.cn/mt/?uid=用户账号&pwd=MD5位32密码&mobile=号码&content=内容
	
	五、接收回复短信
	操作的格式： http://http.000.com/rx/?uid=用户账号&pwd=MD5位32密码
	注：提取的回复短信不能在重复提取，可以通过time参数重复提取某个时间的所有回复短信，需要间隔10分钟提取一次，不能频繁提取。

	六、取剩余短信条数
	操作格式： http://http.000.com/mm/?uid=用户账号&pwd=MD5位32密码
	
	七、取已发送总条数
	操作格式： http://http.000.com/mm/?uid=用户账号&pwd=MD5位32密码&cmd=send
 * ClassName: MessageUtil <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2014-4-4 下午3:19:05 <br/> 
 * 
 * @author Administrator 
 * @version  
 * @since JDK 1.6
 */
public class MessageUtil {
	private static final String addr="http://api.sms.cn/mt/";
		public int send(String msg,String mobile) throws Exception {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
			int nRet = 0;
			String straddr = addr +"?uid=a765741668&pwd=6fbdb54f3634f21b86660298af987c43&mobile="+mobile+"&encode=utf8";
			StringBuffer sb = new StringBuffer(straddr);
			sb.append("&content="+ URLEncoder.encode(msg, "UTF-8"));
			System.out.println(sb.toString());
			URL url = new URL( sb.toString());
			//URL  url = new URL(URLEncoder.encode(sb.toString(),"UTF-8") );
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputline = in.readLine();
			System.out.println(inputline);
			System.out.println(msg);
			//if(! inputline.equals("100"))
			//   nRet = 1;
			return nRet;
		}
		public static void main(String[] args) throws Exception {
			new MessageUtil().send("你的验证码为：123321【云信】", "18223349610");
		}
	
}

