package com.base.excel;

public class ClassUtil {

	@SuppressWarnings("rawtypes")
	public static Class getClass(String className){
		Class c = Object.class;
		if(className!=null && !className.trim().equals("")){
			if(className.contains(".")){
				try {
					c = Class.forName(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}else{
				try {
					c = Class.forName("java.util."+className);
				} catch (ClassNotFoundException e) {
					try {
						c = Class.forName("java.lang."+className);
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return c;
	}
}
