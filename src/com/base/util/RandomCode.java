package com.base.util;

import java.util.List;
import java.util.Random;

public class RandomCode {

	public static String create(List<String> codeList) {
		String code = null;
		do {
			code = createCode();
		} while (codeList!=null && codeList.size()>0 && codeList.contains(code));
		return code;
	}

	private static String createCode() {
		Random r = new Random();
		int i = r.nextInt(26);
		String result = "";
		char c = (char) ('A'+i);
		result += c;
		i = r.nextInt(26);
		c = (char) ('A'+i);
		result += c;
		i = r.nextInt(26);
		c = (char) ('A'+i);
		result += c;
		i = r.nextInt(26);
		c = (char) ('A'+i);
		result += c;
		i = r.nextInt(26);
		c = (char) ('A'+i);
		result += c;
		return result;
	}

}
