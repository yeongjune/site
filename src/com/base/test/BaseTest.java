package com.base.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.site.model.Data;
import com.site.model.Label;
import com.site.service.DataService;
import com.site.service.LabelService;


/**
 * 测试数据库操作的基类
 * 
 * @author lostslef
 * 
 */
public class BaseTest {
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("com/base/config/applicationContext.xml");

	public static <T> T openService(Class<T> c) {
		return ctx.getBean(c);
	}
	
	public static void main(String[] args) throws Exception{
		
		LabelService labelService = openService(LabelService.class);
		Label label1 = new Label();
		label1.setId("Recommend");
		label1.setName("推荐位标签");
		labelService.save(label1);
		
		Label label2 = new Label();
		label2.setId("Column");
		label2.setName("栏目标签");
		labelService.save(label2);
		
		Label label4 = new Label();
		label4.setId("List");
		label4.setName("列表标签");
		labelService.save(label4);
		
		DataService dataService = openService(DataService.class);
		Data data = new Data();
		data.setName("当前位置");
		data.setLabelId("Location");
		dataService.save(data);
		
		Data data2 = new Data();
		data2.setName("文章内容");
		data2.setLabelId("Article");
		dataService.save(data2);
		
		Data data3 = new Data();
		data3.setLabelId("Menu");
		data3.setName("菜单");
		dataService.save(data3);

		Data data4 = new Data();
		data4.setLabelId("Friendlylink");
		data4.setName("友情链接");
		dataService.save(data4);
	}
}