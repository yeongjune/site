package com.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数组处理工具类
 * @author dzf
 * @date 2014-6-19 下午03:19:00
 */
public class ListUtils {

	/**
	 * 对数组分类
	 * @param key 按分组数据中的哪一项分组
	 * @param data 需分组数据
	 * @return
	 */
	public static Map<Object, List<Map<String, Object>>> classifyMapList(String key, List<Map<String, Object>> data){
		Map<Object, List<Map<String, Object>>> mapList = new HashMap<Object, List<Map<String,Object>>>();
		classifyMapList(key, data, mapList);
		return mapList;
	}
	
	/**
	 * 对数组分类
	 * @param key 按分组数据中的哪一项分组
	 * @param data 需分组数据
	 * @param container 容器
	 * @return
	 */
	public static void classifyMapList(String key, List<Map<String, Object>> data, Map<Object, List<Map<String, Object>>> container){
		for (Map<String, Object> map : data) {
			Object o = map.get(key);
			if( o == null ) continue;
			List<Map<String, Object>> list = container.get(o);
			if(list == null ){
				list = new ArrayList<Map<String,Object>>();
				container.put(o, list);
			}
			list.add(map);
		}
	}
	
	/**
	 * 对数组分类
	 * @param key 按分组数据中的哪一项分组
	 * @param data 需分组数据
	 * @return
	 */
	public static Map<Object, Map<String, Object>> classifyMapListByMap(String key, List<Map<String, Object>> data){
		Map<Object, Map<String, Object>> maps = new HashMap<Object, Map<String,Object>>();
		for (Map<String, Object> map : data) {
			Object o = map.get(key);
			if(o != null) maps.put(o, map);
		}
		return maps;
	}
	
	/**
	 * 通过get方法获取对象字段值保存于Map中，key为字段名，value为值
	 * @param t 对象
	 * @author dzf
	 * @return 
	 * 		对象的Map
	 * @throws Exception 
	 * 		get方法执行异常
	 */
	@SuppressWarnings({ "rawtypes" })
	public static <T> Map<String, Object> ObjectToMap(T t) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		if( t != null ){
			Class c = t.getClass();
			Method[] methods = c.getMethods();
			Field[] fields = c.getDeclaredFields();
			if( fields != null && fields.length > 0 )
				for (Field field : fields) {
					if( field == null ) continue;
					String name = field.getName();
					if( name == null ) continue;
					String mName = name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1) : "");
					for (int i = 0; i < methods.length; i++) {
						Method m = methods[i];
						if( m == null) continue;
						Class[] params = m.getParameterTypes();
						if( params.length != 0 ){
							methods[i] = null;
							continue;
						}
						String methodName = m.getName();
						if( !methodName.equals("get"+mName) )continue;
						Object obj = m.invoke(t);
						result.put(name, obj);
						methods[i] = null;
						break;
					}
				}
		}
		return result;
	}
	
	/**
	 * map转换成object（map中的字段名必须是object的setXXX方法后面的XXX）
	 * @author ah
	 * @param t
	 * @param map
	 * @return
	 */
	public static <T> T mapToObject(T t, Map<String, Object> map){
		try {
			Method[] methods = t.getClass().getMethods();
			
			for(Method method : methods){
				String key = method.getName();
				if(key.startsWith("set")){
					key = key.substring(3);
					key = key.substring(0, 1).toLowerCase() + key.substring(1);
					Object value = map.get(key);
					if(value != null){
						method.invoke(t, value);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
}
