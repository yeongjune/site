package com.base.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class WordUtil {
	
	/**
	 * 输出html文档为word文档
	 * 
	 * @author lifq
	 * @param outputStream 输出流
	 * @param htmlUrl	   html地址
	 * @throws IOException 
	  * 调用例子一：生成在指定目录
	 * OutputStream os = new OutputStream(new FileOutputStream("存放路径.doc"));
	 * WordUtil.downloadHtmlAsWord(os,'http://www.baidu.com');
	 * os.close();
	 * 
	 * 调用例子二：下载
	 * response.setContentType("application/msword;charset=utf-8");
	 * response.setHeader("Cache-Control", "no-cache");
	 * response.addHeader("Content-Disposition", "attachment;filename="+(new Date()).getTime()+".doc");//这里指定默认下载的名字，下载的时候用户可以修改
	 * WordUtil.downloadHtmlAsWord(response.getOutputStream(),'http://www.baidu.com');
	 */
	public static void downloadHtmlAsWord(OutputStream outputStream,String htmlUrl) throws IOException{
		URL url=new URL(htmlUrl);
		InputStream inputStream=url.openStream();
		POIFSFileSystem poifsFileSystem=new POIFSFileSystem();
		poifsFileSystem.createDocument(inputStream, "WordDocument");
		poifsFileSystem.writeFilesystem(outputStream);
		inputStream.close();
	}
	
	public static void downloadContentAsWord(OutputStream outputStream,String content) throws IOException{
		InputStream inputStream=new ByteArrayInputStream(content.getBytes());
		POIFSFileSystem poifsFileSystem=new POIFSFileSystem();
		poifsFileSystem.createDocument(inputStream, "WordDocument");
		poifsFileSystem.writeFilesystem(outputStream);
		inputStream.close();
	}
	
	public static void main(String[] args) throws IOException {
		FileOutputStream os=new FileOutputStream(new File("c:/baidu.doc"));
		WordUtil.downloadHtmlAsWord(os,"http://www.baidu.com");
		os.close();
		
		
		 
         FileOutputStream fos = null;
         String path = "C:/";  //根据实际情况写路径
         

         

	}
	
	public static void writeAsWord(OutputStream outputStream,String htmlUrl) throws IOException{
		URL url=new URL(htmlUrl);
		InputStream inputStream=url.openStream();
		POIFSFileSystem poifs = new POIFSFileSystem();
        DirectoryEntry directory = poifs.getRoot();
        //DocumentEntry documentEntry = directory.createDocument("WordDocument", inputStream);
        directory.createDocument("WordDocument", inputStream);
        poifs.writeFilesystem(outputStream);
        inputStream.close();
	}
}
