package com.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.springframework.stereotype.Controller;

/**
 * 类名：FileUtil 类描述：文件操作、复制、剪切、删除文件/文件夹 编写者 ：lostself 编写日期 ：2011-6-22
 */
@Controller
public class FileUtil {

	public static void main(String[] args) {

		// 测试剪切操作
		// FileUtil.cut("G:\\my", "G:\\my_to\\aa");
		// FileUtil.createFolder("D:/abc/dd/");
	}

	/**
	 * 创建目录
	 * 
	 * @param filePath
	 */
	public static void createFolder(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
			file.delete();
		}
	}

	/**
	 * 方法功能：删掉文件夹/删掉文件 参数说明：@param file 需要删除的文件/文件夹 返回：void 作者：lostself
	 * 日期：2011-6-17
	 */
	public static boolean delete(String path) {
		File file = new File(path);
		delete(file);
		return true;
	}

	/**
	 * 方法功能：删掉文件夹/删掉文件 参数说明：@param file 需要删除的文件/文件夹 返回：void 作者：lostself
	 * 日期：2011-6-17
	 */
	public static boolean delete(File file) {
		// 要删除的文件/文件夹不存在 返回false
		if (!file.exists()) {
			System.out.println("需要删除的文件/文件夹不存在");
			return false;
		}
		// 假如是文件就直接删除
		if (file.isFile()) {
			file.delete();
		}
		// 假如的文件夹就删除文件夹下面所有的文件，最后删掉文件夹
		else if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				// 递归调用删除方法
				delete(f);
			}
			file.delete();
		}
		return true;
	}

	/**
	 * 方法功能：复制文件夹/文件 参数说明：@param fromPath 需要复制的文件路径/文件夹路径 参数说明：@param toPath
	 * 复制到目标文件夹路径 参数说明：@return 返回：boolean 作者：lostself 日期：2011-6-20
	 */
	public static boolean copy(String fromPath, String toPath) {
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);
		// 判断源文件是否存在
		if (!fromFile.exists()) {
			System.out.println("需要复制的文件/文件夹不存在");
			return false;
		}
		// 假如是文件的话文件流复制操作
		if (fromFile.isFile()) {
			copyFile(fromFile, toFile);
		}
		// 假如是文件夹的话旧递归调用复制方法
		else if (fromFile.isDirectory()) {
			toFile.mkdirs();
			for (File f : fromFile.listFiles()) {
				copy(f.getAbsolutePath(), toFile.getPath() + "/" + f.getName());
			}
		}
		return true;
	}

	/**
	 * 方法功能：剪切文件夹/文件 参数说明：@param fromPath 需要剪切的文件/文件夹路径 参数说明：@param toPath
	 * 剪切到目标文件夹路径 返回：boolean 作者：lostself 日期：2011-6-22
	 */
	public static boolean cut(String fromPath, String toPath) {
		boolean flag = false;
		if (copy(fromPath, toPath) && delete(fromPath)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 方法功能：复制文件，必须保证路径存在 参数说明：@param fromFile 文件来源 参数说明：@param toFile 目标文件
	 * 返回：void 作者：lostself 日期：2011-6-22
	 */
	public static void copyFile(File fromFile, File toFile) {
		// 先创建好复制到的路径，
		new File(toFile.getParent()).mkdirs();
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 将流写入到文件
	 * 
	 * @param is
	 *            文件流
	 * @param toFile
	 *            写入的文件
	 */
	public static void copyFile(InputStream is, File toFile) {
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存文件在本地
	 * 
	 * @param savePath
	 *            保存路径(包括文件名)
	 * @param file
	 *            File 对象
	 */
	public static void saveFile(String savePath, File file) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {

			File temp = new File(savePath);
			if (!temp.exists()) {
				temp.createNewFile();
			}

			fos = new FileOutputStream(savePath);
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 将流写入到文件
	 * 
	 * @param is
	 *            文件流
	 * @param toFile
	 *            写入的文件
	 */
	public static void copyFileByIOStream(InputStream is, File toFile) {
		new File(toFile.getParent()).mkdirs();
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 等待命令转换完成
	 * 
	 * @param p
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static int doWaitFor(Process p) {
		int exitValue = -1; // returned to caller when p is finished
		try {

			InputStream in = p.getInputStream();
			InputStream err = p.getErrorStream();
			boolean finished = false; // Set to true when p is finished

			while (!finished) {
				try {
					while (in.available() > 0) {
						// Print the output of our system call
						Character c = new Character((char) in.read());
						System.out.print(c);
					}
					while (err.available() > 0) {
						// Print the output of our system call
						Character c = new Character((char) err.read());
						System.out.print(c);
					}

					// Ask the process for its exitValue. If the process
					// is not finished, an IllegalThreadStateException
					// is thrown. If it is finished, we fall through and
					// the variable finished is set to true.
					exitValue = p.exitValue();
					finished = true;
				} catch (IllegalThreadStateException e) {
					// Process is not finished yet;
					// Sleep a little to save on CPU cycles
					Thread.currentThread().sleep(500);
				}
			}
			if (in != null)
				in.close();
			if (err != null)
				err.close();
		} catch (Exception e) {
			// unexpected exception! print it out for debugging...
			System.err.println("doWaitFor(): unexpected exception - "
					+ e.getMessage());
		}

		// return completion status to caller
		return exitValue;
	}

	/**
	 * 创建文件名（MD5文件字节码加密产生）
	 * @param inputStream
	 * @return
	 */
	public static String createFileName(InputStream inputStream) {
		try {
			return MD5Util.getMD5String(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 读取文件文本内容
	 * @author lifq
	 * @param pathname
	 * @return
	 */
	public static String getFileText(String pathname){
		try {
			File file=new File(pathname);
			if (file.exists()&&file.isFile()) {
				InputStreamReader reader=new InputStreamReader(new FileInputStream(file),"utf-8");
				BufferedReader bufferedReader=new BufferedReader(reader);
				String tempStr="";
				StringBuilder result=new StringBuilder();
				while ((tempStr=bufferedReader.readLine())!=null) {
					result.append(tempStr).append("\n");
				}
				reader.close();
				return result.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 写入文件内容
	 * @author lifq
	 * @param pathname
	 * @param content
	 * @param append
	 * @return
	 */
	public static boolean writeToFile(String pathname,String content,boolean append){
		try {
			File file=new File(pathname);
			if (file.exists()&&file.isFile()) {
				BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
				bufferedWriter.write(content);
				bufferedWriter.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
