package com.base.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class FormworkValues {

	private static final String rootPath = FormworkValues.class.getResource("")
			.getPath();

	private static Map<String, List<String>> MAP = new HashMap<String, List<String>>();
	private static String xmlFileName = "formworkValues.xml";

	static {
		initMap();
	}

	/**
	 * 读取xml文件，初始化数据
	 * 
	 * @param elementName
	 * @return
	 */
	public static void initMap() {
		loadKeyValue();
	}

	@SuppressWarnings("unchecked")
	private static void loadKeyValue() {
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
			is = new FileInputStream(rootPath + xmlFileName);
			Document doc = reader.read(is);
			Element root = doc.getRootElement();
			List<Element> children = root.elements();
			if (children != null && children.size() != 0) {
				Element child;
				List<Element> data;
				String key;
				for (int i = 0; i < children.size(); i++) {
					child = children.get(i);
					String name = child.attributeValue("name");
					List<String> list = new ArrayList<String>();
					data = child.elements();
					if (data != null && data.size() != 0) {
						for (int j = 0; j < data.size(); j++) {
							key = data.get(j).getText();
							list.add(key);
						}
					}
					MAP.put(name, list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取type元素子元素的数据
	 * 
	 * @param type
	 * @return
	 */
	public static List<String> get(String type) {
		return MAP.get(type);
	}

	/**
	 * 判断是否存在type的一级元素
	 * 
	 * @param type
	 * @return
	 */
	public boolean containsType(String type) {
		return MAP.containsKey(type);
	}
}
