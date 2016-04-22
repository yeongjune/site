package com.base.util;

public class UnicodeUtil {

	/**
	 * 将字符串转成unicode
	 * 
	 * @param str
	 *            待转字符串
	 * @return unicode字符串
	 */
	public static String convert(String str) {
		str = (str == null ? "" : str);
		if(isUnicode(str)){
			return str;
		}
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	/**
	 * 将unicode 字符串
	 * 
	 * @param str
	 *            待转字符串
	 * @return 普通字符串
	 */
	public static String revert(String str) {
		str = (str == null ? "" : str);
		if (!isUnicode(str))// 如果不是unicode码则原样返回
			return str;

		StringBuffer sb = new StringBuffer(1000);

		String strTemp = null;
		String value = null;
		int c;
		char tempChar;
		int t;
		for (int i = 0; i <= str.length() - 6;) {
			strTemp = str.substring(i, i + 6);
			value = strTemp.substring(2);
			c = 0;
			for (int j = 0; j < value.length(); j++) {
				tempChar = value.charAt(j);
				t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();
	}
	
	/**
	 * 判断是否Unicode
	 * @param code
	 * @return
	 */
	public static boolean isUnicode(String code){
		if(code!=null && code.length()>0){
			for (int i = 0; i < code.length(); i++) {
				if(i%6==0){
					if(code.charAt(i)=='\\'){
						
					}else{
						return false;
					}
				}else if(i%6==1){
					if(code.charAt(i)=='u' || code.charAt(i)=='U'){
						
					}else{
						return false;
					}
				}else{
					if(code.charAt(i)>='0' && code.charAt(i)<='9' || code.charAt(i)>='a' && code.charAt(i)<='f'){
						
					}else{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		String tmp = convert("啊水电费");
		System.out.println(tmp);
		tmp = revert(tmp);
		System.out.println(tmp);
	}
	
}
