package com.base.util;

import java.util.Random;

public class CaptchaUitl {
	
	public static String getCaptcha(){
		
		return getCaptcha(4);
	}

	
	public static String getCaptcha(Integer length){
		String tmp = "";
		Random r = new Random();
		
		for(int i=0;i<length;i++){
			tmp+=r.nextInt(10);
		}
		return tmp;
	}
}
