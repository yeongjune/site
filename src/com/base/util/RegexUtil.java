package com.base.util;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil
{
	/**
	 * 匹配正则表达式,返回字符串List集合
	 * @param regex 正则表达式
	 * @param code 需要匹配的字符串
	 * @return 返回匹配后的字符串List集合
	 */
	public static List<String> parseList(String regex, String code)
	{
		List<String> result = new ArrayList<String>();
		// 启用不区分大小写匹配模式
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(code);
		while (m.find())
		{
			result.add(m.group(1));
		}
		return result;
	}
	
	/**
	 * 正则返回字符串，逗号衔接
	 * @param regex
	 * @param code
	 * @return
	 */
	public static String parseString(String regex, String code)
	{
		String result = "";
		// 启用不区分大小写匹配模式
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(code);
		while (m.find())
		{
			result += m.group(1) + ",";
		}
		return result;
	}
	
	/**
	 * 测试有没有找到正则
	 * @param regex
	 * @param code
	 * @return
	 */
	public static boolean parseFind(String regex, String code)
	{
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(code);
		while(m.find()){
			return true;
		}
		return false;
	}
	

	/**
	 * 匹配正则表达式,返回字符串List集合
	 * @param regex 正则表达式
	 * @param code 需要匹配的字符串
	 * @return 返回匹配后的字符串List集合
	 */
	public static List<String> parseList(String regex, String code, int number)
	{
		List<String> result = new ArrayList<String>();
		// 启用不区分大小写匹配模式
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(code);
		for (; number > 0; number--)
		{
			m.find();
			result.add(m.group(1));
		}
		return result;
	}

	/**
	 * 匹配正则表达式,返回唯一的字符串
	 * @param regex 正则表达式
	 * @param code 需要匹配的字符串
	 * @return 匹配后的字符串
	 */
	public static String parse(String regex, String code)
	{
		String result = null;
		// 启用不区分大小写匹配模式
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(code);
		while (m.find())
		{
			result = m.group(1);
		}
		return result;
	}

	/**
	 * 匹配正则表达式,返回唯一的字符串
	 * @param regex 正则表达式
	 * @param code 需要匹配的字符串
	 * @return 匹配后的字符串
	 */
	public static String parseOne(String regex, String code)
	{
		// 启用不区分大小写匹配模式
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(code);
		m.find();
		return m.group(1);
	}

	/**
	 * 获取下载文件图片地址的文件名
	 * @param s图片地址
	 * @return 图片文件名
	 */
	public static String getFileName(String s)
	{
		String fileName = null;
		String regex = "http://.+/(.+)";
		fileName = parse(regex, s);
		return fileName;
	}

	public static RegexUtil getInstanceParse()
	{
		return new RegexUtil();
	}


	/**
	 * 创建一个文件 确保文件路径存在
	 * @param path 文件路径 格式d:\\
	 * @param fileName 文件名
	 * @param code 需要写进去的字符串
	 */
	public static void createJS(String path, String code, String charset)
	{
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path), charset);
			out.write(code);
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 对文件进行追加字符的操作
	 * @param path 文件存放的目录
	 * @param fileName 文件的名称
	 * @param code 追加的字符
	 */
	public static void appendToFile(String path, String fileName, String code)
	{
		try{
			File file = new File(path);
			if(!file.exists())
			{
				file.mkdirs();
			}
			FileWriter fileWriter = new FileWriter(path + fileName, true);
			fileWriter.write(code);
			fileWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建一个文件
	 * @param path 文件路径 格式d:\\
	 * @param fileName 文件名
	 * @param code 需要写进去的字符串
	 * @param charset 字符编码 
	 */
	public static void createJS(String path, String fileName, String code, String charset)
	{
		try
		{
			File file = new File(path);
			if (!file.exists())
			{
				file.mkdirs();
			}
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path + fileName), charset);
			out.write(code);
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 下载图片到指定路径
	 * @param url	需要下载文件的路径
	 * @param filePath  本机保存路径
	 * @param fileName 文件保存名字
	 */
	public static void saveImg(String url, String filePath, String fileName)
	{
		try
		{
			URL u = new URL(url);
			InputStream is = u.openStream();
			File cartoonFile = new File(filePath);
			if (!cartoonFile.exists())
			{
				cartoonFile.mkdirs();
			}
			OutputStream os = new FileOutputStream(filePath + fileName);
			int read = 0;
			byte[] buffer = new byte[400];
			while ((read = is.read(buffer)) != -1)
			{
				os.write(buffer, 0, read);
			}
			os.close();
			is.close();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 加载文件字符串内容
	 * @param rootPath 根路径
	 * @return
	 */
	public static String loadStringByFile(String rootPath)
	{
		File file = new File(rootPath);
		
		StringBuilder sb = new StringBuilder();
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			InputStream is = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while((line = br.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 加载文件字符串内容
	 * @param rootPath 根路径
	 * @param charset 根路径
	 * @return
	 */
	public static String loadStringByFile(String rootPath, String charset)
	{
		File file = new File(rootPath);
		
		StringBuilder sb = new StringBuilder();
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			InputStream is = new FileInputStream(rootPath);
			InputStreamReader fr = new InputStreamReader(is, charset);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while((line = br.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
}
