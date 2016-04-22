package com.base.test;

import com.base.util.RegexUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {

	public static void main(String[] args) throws IOException {
		String code = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("F:/index2.jsp"), "utf-8"));
		String line;
		while ((line = reader.readLine()) != null)
			code += line;
		reader.close();
		// 校验代码
		Pattern compile = Pattern.compile("(<%[^@](?:.|\\n)*%>)");
		Matcher matcher = compile.matcher(code);
		System.out.println(matcher.group());
	}
}
