package com.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;

/**
 * String操作工具
 * @author Frank
 */
@Controller
public class StringUtil {
	
	/**
	 * 去掉重复的字符
	 * @param str 格式:1,2,3,2,3
	 * @return 1,2,3
	 */
	public static String replaceRepeat(String code)
	{
		String result = "";
		Set<String> set = new LinkedHashSet<String>();
		for(String str : code.split(","))
		{
			if("".equals(str))
			{
				continue;
			}
			set.add(str);
		}
		result = set.toString().replaceAll("\\[|\\]|\\s*", "");
		return result;
	}

	/**
	 * 去掉所有空白
	 * @param str
	 * @return
	 */
	public static String replaceNoSpaceString(String str) {
		return str.replaceAll("\\s*", "");
	}
	
	/**
	 * 把集合元素转换成字符串 并且去掉空白 和[]
	 * @param code
	 * @return 1, 2, 3
	 */
	public static String replaceCollectionToString (Collection<?> c)
	{
		return c.toString().replaceAll("\\[|\\]|\\s*", "");
	}
	
	/**
	 * 把集合元素转换成字符串 并且去掉空白 和[]
	 * @param code
	 * @return 1, 2, 3
	 */
	public static String replaceCollectionToString (String[] str){
		String result = "";
		for(int i = 0; i < str.length; i++){
			result += str[i] + ",";
		}
		
		return result.replaceFirst(",$", "");
	}
	
	/**
	 * 第一个字母转换为大写
	 * @param value
	 * @return
	 */
	public static String changeOneStringToUpper(String value)
	{
		String result = value.substring(0, 1).toUpperCase();
		return result + value.substring(1);
	}
	
	/**
	 * 第一个字母转换为小写
	 * @param value
	 * @return
	 */
	public static String changeOneStringToLower(String value)
	{
		String result = value.substring(0, 1).toLowerCase();
		return result + value.substring(1);
	}
	
	/**
	 * 判断是否结束为某个字符，没有的话就加上
	 * @param code
	 * @param sign
	 * @return
	 */
	public static String addEndSign(String code, String sign)
	{
		if(!code.endsWith(sign))
		{
			return code += sign;
		}
		return code;
	}
	
	/**
	 * 去掉前后[]
	 * @param code
	 * @return
	 */
	public static String replaceSign (String code)
	{
		return code.replaceAll("\\[|\\]", "");
	}
	

	
	/**
	 * 处理sql特殊字符
	 * @param value
	 * @return
	 */
	public static String replaceSpecia(String value){
		value=value.replace("%", "\\%");
		value=value.replace("'"," ");
		return value;
	}
	
	/**
	 * 去掉前后[]和空白
	 * @param code
	 * @return
	 */
	@SuppressWarnings("rawtypes")
  public static String replaceSign (List list)
	{
		return list.toString().replaceAll("\\[|\\]\\s*", "");
	}
	
	/**
	 * 拼凑字符串成 sql 字符参数
	 * @param str 格式：1,2,3
	 * @return '1','2','3'
	 */
	public static String changeForSQL(String str)
	{
		StringBuilder sb = new StringBuilder();
		for(String s : str.replaceAll(" ", "").split(","))
		{
			sb.append("'" + s + "'" + ",");
		}
		return sb.toString().replaceFirst(",$", "");
	}
	
	/**
	 * 编写者：CP
	 * 编写日期：2011-9-4
	 * @param str
	 * @return
	 * <br />方法描述：拼凑字符数 成 sql 语句中使用 形式 '1','2'
	 */
	public static String changeForSQL(String[] str){
		StringBuilder sb = new StringBuilder();
		for(String s : str){
			if("".equals(s)){ continue;}
			sb.append("'" + s + "',");
		}
		return sb.toString().replaceFirst(",$", "");
	}
	
	/**
	 * 编写者：CP
	 * 编写日期：2011-9-7
	 * @param strCollection
	 * @return
	 * <br />方法描述：拼凑字符数 成 sql 语句中使用 形式 '1','2'
	 */
	public static String changeForSQL(Collection<?> objCollection){
		StringBuilder sb = new StringBuilder();
		for(Object obj : objCollection){
			if(obj == null || "".equals(obj.toString())){ continue;}
			sb.append("'" + obj + "',");
		}
		return sb.toString().replaceFirst(",$", "");
		
	}
	
	/**
	 * 编写者：CP
	 * 编写日期：2011-10-8
	 * <br />方法描述：对字符utf8 进行转码 
	 */
	public static String encode(String code){
		try {
			code = URLEncoder.encode(code, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return code;
	}

	//文件大小转换
	public static String formatSize(float size){
		long kb = 1024;
		long mb = (kb*1024);
		long gb = (mb*1024);
		if(size<kb){
			return String.format("%d B", (int)size);
		}else if(size<mb){
			return String.format("%.2f KB", size/kb);
		}else if (size<gb){
			return String.format("%.2f MB", size/mb);
		}else{
			return String.format("%.2f GB", size/gb);
		}
	}
	
	public static String filter(String content) {
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("\"", "&quot;");
		content = content.replaceAll("\r\n", "<br>");
		content = content.replaceAll("\n", "<br>");
		content = content.replaceAll(" ", "&nbsp;");
		return content;
	}
	
	/**
	 * 将String类型的数据转换成Long类型的数组,此方法必须要确认字符串中的内容可以拆分成数字 例如:"1,2,3,4,5" 调用方法为:
	 * CommonUtil.stringToLongArray("1,2,3,4,5",",");
	 * 
	 * @param str
	 *            需要拆分的字符串
	 * @param splitExp
	 *            拆分的符号
	 * @return
	 */
	public static Long[] stringToLongArray(String str, String splitExp) {
		Long[] l = null;
		String temp[] = str.split(splitExp);
		l = new Long[temp.length];
		for (int i = 0; i < temp.length; i++) {
			l[i] = new Long(temp[i]);
		}
		return l;
	}
	
	/**
	 * 获取当前系统时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 判断字符串是否为null或空字符串
	 * @author lifq
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if (str==null||str.trim().isEmpty()) {
			return true;
		}else{
			return false;
		}
	}

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
	
	/**
	 * 按字节截取字符串长度
	 * @author lifq
	 * @param str
	 * @param count
	 * @return
	 */
	public static String subStr(String str,int count){
		int reInt=0;
		String reStr="";
		char [] tempChar=str.toCharArray();
		for (int i = 0; (i<tempChar.length && count>reInt); i++) {
			@SuppressWarnings("static-access")
			String s1=str.valueOf(tempChar[i]);
			byte [] b=s1.getBytes();
			reInt += b.length;
			reStr += tempChar[i];
		}
		return reStr;
	}
	
	/**
	 * 将字符串分割成Integer数据
	 * @author lfq
	 * @time 2015-3-17
	 * @param content	要分割的字符串
	 * @param regex		分割正则表达式,比如按逗号分割传","
	 * @return
	 */
	public static Integer [] splitToArray(String content,String regex){
		if (StringUtil.isEmpty(content)) {
			return null;
		}else{
			String [] array=content.split(regex);
			List<Integer> result=new ArrayList<Integer>();
			for (int i = 0; i < array.length; i++) {
				result.add(Integer.parseInt(array[i]));
			}
			return result.toArray(new Integer[0]);
		}
	}

    /**
     * 搜索字符串
     * @param str 源字符串
     * @param start 开头字符串
     * @param end 结尾字符串,遭遇到即结束，保存一条结果
     * @return
     *  结果集
     * @author dzf
     */
    public static List<String> searchStr(String str, String start, String end){
        List<String> result = new ArrayList<String>();
        boolean exit = false;
        int beginIndex = 0;
        while (!exit){
            int startIndex = str.indexOf(start, beginIndex);
            if(startIndex != -1){
                beginIndex = str.indexOf(end, startIndex + start.length());
                if(beginIndex != -1){
                    result.add(str.substring(startIndex, beginIndex + 1));
                }else{
                    exit = true;
                }
            }else{
                exit = true;
            }
        }
        return result;
    }
    /**
     * 去除html标签和回车空格
     * @param htmlStr html代码内容块
     * @return
     */
    public static String delHTMLTag(String htmlStr) {  
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);  
        Matcher m_html = p_html.matcher(htmlStr);  
        htmlStr = m_html.replaceAll(""); // 过滤html标签  
        Pattern p_space = Pattern.compile("\\s*|\t|\r|\n", Pattern.CASE_INSENSITIVE);  
        Matcher m_space = p_space.matcher(htmlStr);  
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签  
        return htmlStr.trim();
	}
    /**
     * 手机号码格式验证
     * @return
     */
    public static boolean checkMobilePhone(String mobilePhone) {
    	Pattern pattern=Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    	Matcher matcher=pattern.matcher(mobilePhone);
    	return matcher.matches();
    }

	/**
	 * 将字符串数组用分隔符关联起来
	 * @param strArray 字符串数组
	 * @param separator 分隔符
	 * @param isTrim 是否去除两边空白
     * @return
     */
	public static String join(String[] strArray, String separator, boolean isTrim){
		String result = "";
		if(strArray != null && strArray.length > 0 ){
			if(separator == null) separator = "";
			for (int i = 0; i < strArray.length; i++) {
				if(i > 0 ) result += separator;
				result += (isNotEmpty(strArray[i]) && isTrim) ? strArray[i].trim() : strArray[i];
			}
		}
		return result;
	}

}
