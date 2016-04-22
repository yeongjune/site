package com.base.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

import org.springframework.beans.factory.annotation.Autowired;

import com.site.service.FriendlyLinkService;
import com.tejiao.service.DeclareService;

/**
 * 定时检查死链
 *	类名 EffectiveUrl.java
 */
public class EffectiveUrl  {
	@Autowired
	private  FriendlyLinkService friendlyLinkService;
	@Autowired
	private  DeclareService declareService;
	public static final int EFFECTIVE_PASS=0;//正常链接
	public static final int EFFECTIVE_FAIL=1;//无法连接--死链
	public  void effective() {
		List<Map<String, Object>> list=friendlyLinkService.findEffectiveFriendlyLinkList();
		for(Map<String, Object> data:list) {
			try {
				if(data.get("linkUrl")!=null) {
					URL uri=new URL(data.get("linkUrl").toString());
					HttpURLConnection connection=(HttpURLConnection) uri.openConnection();
					connection.setRequestMethod("HEAD");
					connection.setRequestProperty("Charset", "UTF-8");
					connection.setUseCaches(false);
					connection.setConnectTimeout(10000);//10秒内连不上  自动结束			
					connection.connect();
					if(connection.getResponseCode()==200) {
					
						friendlyLinkService.updateEffective(Long.valueOf(data.get("id").toString()), EFFECTIVE_PASS);//把200的设置为0
						System.out.println("成功");
					}else {
						
						friendlyLinkService.updateEffective(Long.valueOf(data.get("id").toString()), EFFECTIVE_FAIL);//非200的设置为1
						System.out.println("失败");
					}
					connection.disconnect();
	
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				friendlyLinkService.updateEffective(Long.valueOf(data.get("id").toString()), EFFECTIVE_FAIL);//非200的设置为1
				System.out.println("失败");
			}
		}
	}
	/**
	 * 特教网站,定时升级学生年级()
	 */
	public  void upgrade() {
		try {
			declareService.updateGrade(null);
			System.out.println("升级学生年级成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("升级学生年级失败");
		}
	}
}
