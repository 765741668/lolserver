package com.lol.fwk.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MoneyUtils {
	public static void main(String[] args) {

		System.out.print(digitUppercase(1234567890.03));
	}

	public static String getFormatter(String str) {
		NumberFormat n = NumberFormat.getNumberInstance();
		double d;
		String outStr = null;
		try {
			d = Double.parseDouble(str);
			outStr = n.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outStr;
	}


	public static String getDecimalFormat(String str) {
		DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00000");
		String outStr = null;
		double d;
		try {
			d = Double.parseDouble(str);
			outStr = fmt.format(d);
		} catch (Exception e) {
		}
		return outStr;
	}

	public static String getDecimalFormat(Double d) {
		DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00");
		String outStr = null;
		try {
			outStr = fmt.format(d);
		} catch (Exception e) {
		}
		return outStr;
	}

	public static String getDecimalFormat(Double d,String fmtStr) {
		DecimalFormat fmt = new DecimalFormat(fmtStr);
		String outStr = null;
		try {
			outStr = fmt.format(d);
		} catch (Exception e) {
		}
		return outStr;
	}
	
	public static String getCurrency(String str) {
		NumberFormat n = NumberFormat.getCurrencyInstance();
		double d;
		String outStr = null;
		try {
			d = Double.parseDouble(str);
			outStr = n.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outStr;
	}

	public static String getRBBig(String input) {
		char c[] = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
		String str = "";
		for (int count = 0; count < input.length(); count++) {
			// 转成数字
			char temp = input.charAt(count);
			switch (temp) {
			case '1':
				str = str + c[1];
				break;
			case '2':
				str = str + c[2];
				break;
			case '3':
				str = str + c[3];
				break;
			case '4':
				str = str + c[4];
				break;
			case '5':
				str = str + c[5];
				break;
			case '6':
				str = str + c[6];
				break;
			case '7':
				str = str + c[7];
				break;
			case '8':
				str = str + c[8];
				break;
			case '9':
				str = str + c[9];
				break;
			case '0':
				str = str + c[0];
				break;
			default:
				break;
			}
		}
		return str;
	}

	public static String digitUppercase(double n) {
		String fraction[] = { "角", "分" };
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };
		String head = n < 0 ? "负" : "";
		n = Math.abs(n);
		String s = "";
		for (int i = 0; i < fraction.length; i++) {
			s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i])
					.replaceAll("(零.)+", "");
		}
		if (s.length() < 1) {
			s = "整";
		}
		int integerPart = (int) Math.floor(n);
		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
					+ s;
		}
		return head
				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
						.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}
}
