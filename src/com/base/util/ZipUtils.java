package com.base.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipOutputStream;


public class ZipUtils {
	
	/**
	 * 将文件打包
	 * @param fos 输出流
	 * @param files 文件数组
	 * @param names 文件别名数组（如果不需要取别名，用文件的名话的话，传null 就行了）
	 * @return
	 * @throws Exception
	 */
	public static ZipOutputStream packByFiles(OutputStream fos,File[] files,String[] names) throws Exception {
		
		boolean flg = true; //判断是否有传文件名过来
		if(names == null){ 
			flg = false;
			names = new String[files.length];
		}
		
		ZipOutputStream zos = new ZipOutputStream(fos,"GBK");
		
		for(int i=0;i<files.length;i++){
			File f = files[i];
			if(!f.exists()) continue; //如果不存在，则跳过
			
			if(!flg) names[i] = f.getName();  //如果没有传文件名字过来，就用此文件的名字代替
			
			ZipEntry ze = new ZipEntry(names[i]); 
			
			zos.putNextEntry(ze);
			
			FileInputStream fis = new FileInputStream(f);
			
			IOUtils.copy(fis,zos); //文件copy
			
			fis.close(); //关闭当前文件流
			zos.closeEntry();  //关闭当前输入流
			
		}
		
		
		return zos;
	}
	
	public static ZipOutputStream packFiles(OutputStream fos,Map<String,Map<String,File>> file) throws Exception{
		ZipOutputStream zos = new ZipOutputStream(fos,"GBK");
		
		for(String key : file.keySet()){
			zos.putNextEntry(new ZipEntry(key+"/"));  //内部创建目录
			
			Map<String,File> f = file.get(key);
			for(String name : f.keySet()){ //每个文件
				File doc = f.get(name); //文档或图片
				
				if(!doc.exists() && !doc.isFile()) continue; //如果不存在，则跳过
				
				zos.putNextEntry(new ZipEntry(key +"/"+name));
				FileInputStream fis = new FileInputStream(doc);
				
				IOUtils.copy(fis,zos); //文件copy
				fis.close(); //关闭当前文件流
				
			}
			zos.closeEntry();  //关闭当前输入流
			
		}
		
		
		return zos;
	}
	
	/**
	 * 将文件夹或文件打包成zip包，包括子文件夹的文件
	 * 
	 * @author lfq
	 * @param base 可以为null,生成包的顶级文件夹名，多级是用/隔开表示多级，传null表示不自定义顶级文件名
	 * @param zos  必须，生成的ZipOutputStream,调用该方法结束后还需要调用zos.close();来关闭
	 * @param file 必须，要压缩的文件夹或文件,一个或多个
	 * 调用例子一：生成在指定目录
	 * ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("生成的包存放路径.zip"));
	 * ZipUtils.newZip(null,zos,new File("要压缩的文件夹或文件路径"));//多次调用该方法可以将多个不同的目录打包到一个压缩包
	 * zos.close();
	 * 
	 * 调用例子二：下载文件夹或文件的压缩包
	 * response.setContentType("application/zip;charset=utf-8");
	 * response.setHeader("Cache-Control", "no-cache");
	 * response.addHeader("Content-Disposition", "attachment;filename="+(new Date()).getTime()+".zip");//这里指定默认下载的名字，下载的时候用户可以修改
	 * ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
	 * ZipUtils.newZip(null, zos, new File("要压缩的文件夹或文件路径"));//多次调用该方法可以将多个不同的目录打包到一个压缩包
	 * zos.close();
	 */
	public static void newZip(String base,ZipOutputStream zos,File...files){
		try {
			if (files!=null&&files.length>0) {
				for (File file : files) {
					if (file.exists()&&file.isFile()) {
						String name= (base==null||"".equals(base))? file.getName(): (base+File.separator+file.getName());
						zos.putNextEntry(new ZipEntry(name));
						InputStream is = new FileInputStream(file);
						/* 这种方式写入不完整 
						 * byte content[] = new byte[1024];
						while (is.read(content) != -1) {
							zos.write(content);
						}*/
						IOUtils.copy(is,zos); //完整的将文件流is写入zos
						is.close();
						zos.closeEntry();
					}else if (file.exists()&&file.isDirectory()) {
						if (file.listFiles().length>0) {
							for (int i = 0; i < file.listFiles().length; i++) {
								if (base==null||base.equals("")) {
									newZip(file.getName(),zos,file.listFiles()[i]);
								}else{
									newZip(base+File.separator+file.getName(),zos,file.listFiles()[i]);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
}
