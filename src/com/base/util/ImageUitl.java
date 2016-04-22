package com.base.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.swing.ImageIcon;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUitl {

	/**
	 * 限制最大宽高缩放并压缩图片
	 * @param is
	 * @param to
	 * @param maxWidth
	 * @param maxHeight
	 * @throws Exception
	 */
	public static void maxWidthHeight(InputStream is, File to, int maxWidth, int maxHeight) throws Exception{
		BufferedImage image = ImageIO.read(is);
		if(image==null)return;
		Integer width = image.getWidth();
		Integer height = image.getHeight();
		if(width>maxWidth || height>maxHeight){
			ResampleOp resampleOp = new ResampleOp(DimensionConstrain.createMaxDimension(maxWidth, maxHeight));
			BufferedImage rescaled = resampleOp.filter(image, null);
			ImageIO.write(rescaled, "png", to);
		}
	}
	public static void maxWidthHeight(InputStream is, String path, int maxWidth, int maxHeight) throws Exception{
		maxWidthHeight(is, new File(path), maxWidth, maxHeight);
	}
	public static void maxWidthHeight(File from, File to, int maxWidth, int maxHeight) throws Exception{
		BufferedImage image = ImageIO.read(from);
		if(image==null)return;
		Integer width = image.getWidth();
		Integer height = image.getHeight();
		if(width>maxWidth || height>maxHeight){
			ResampleOp resampleOp = new ResampleOp(DimensionConstrain.createMaxDimension(maxWidth, maxHeight));
			BufferedImage rescaled = resampleOp.filter(image, null);
			ImageIO.write(rescaled, "png", to);
		}
	}
	public static void maxWidthHeight(File from, String path, int maxWidth, int maxHeight) throws Exception{
		maxWidthHeight(from, new File(path), maxWidth, maxHeight);
	}
	public static void copyInputStreamToFile(InputStream inputStream, File file) throws Exception {
		if(inputStream!=null && file!=null){
			if(!file.exists()){
				file.mkdirs();
				file.delete();
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			while(inputStream.read(bytes)!=-1){
				out.write(bytes);
			}
			out.flush();
			out.close();
			inputStream.close();
			
			String path = file.getPath();
			String maxPath = path.substring(0, path.lastIndexOf('.'))+"_max.png";
			maxWidthHeight(file, maxPath, 1024, 768);
			String midPath = path.substring(0, path.lastIndexOf('.'))+"_mid.png";
			maxWidthHeight(file, midPath, 310, 310);
			String minPath = path.substring(0, path.lastIndexOf('.'))+"_min.png";
			maxWidthHeight(file, minPath, 80, 80);
		}
	}

	public static final Random R = new Random();
	/**
	 * 产生（日期+100以内随机数+后缀名）的字符串；yyyy-MM-dd-HH-mm-ss-ms-random.ext
	 * @param inputStream 
	 * @param ext 
	 * @return
	 * @throws IOException 
	 */
	public static String createFileName(InputStream inputStream) throws IOException {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
				digest.update(buffer, 0, len);
			}
			inputStream.close();
		} catch (Exception e) {
			if(inputStream != null)inputStream.close();
			e.printStackTrace();
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
	public static byte[] imageToByteArray(File file) throws IOException {
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());
		BufferedImage bi = ImageIO.read(file);
		if(bi==null)return null;
		ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
		boolean resultWrite = ImageIO.write(bi, ext, imageStream);
		if(resultWrite){
			imageStream.flush();
			byte[] tagInfo = imageStream.toByteArray();
			return tagInfo;
		}
		return null;
	}
	
	/**
	 * 限制最大宽高缩放并压缩图片,如果不超出则直接复制到到
	 * @author  lfq
	 * @param is
	 * @param to
	 * @param maxWidth
	 * @param maxHeight
	 * @throws Exception
	 */
	public static void maxWidthHeight2(InputStream is, File to, int maxWidth, int maxHeight) throws Exception{
		BufferedImage image = ImageIO.read(is);
		if(image==null)return;
		Integer width = image.getWidth();
		Integer height = image.getHeight();
		if(width>maxWidth || height>maxHeight){
			ResampleOp resampleOp = new ResampleOp(DimensionConstrain.createMaxDimension(maxWidth, maxHeight));
			BufferedImage rescaled = resampleOp.filter(image, null);
			ImageIO.write(rescaled, "png", to);
		}else{
			ImageIO.write(image, "png", to);
		}
	}
	
	
	/**
	 * 图片缩放和截取处理
	 * 
	 * @author lfq
	 * @time 2014-11-17 上午10:52:58
	 * @param is
	 *            原图片输入流
	 * @param ouptPath
	 *            输出路径
	 * @param originalPath
	 *            原图输出路径(为空则不保存原图)
	 * @param width
	 *            宽度,如果是等比例缩放该参数代表最大宽度,否则表示压缩的宽度
	 * @param height
	 *            高度,如果是等比例缩放该参数代表最大高度,否则表示压缩的高度
	 * @param scale
	 *            是否等比例缩放,如果该值为false并且原图小于with或height时图片将会被放大
	 * @throws IOException
	 */
	public static void imageZip(InputStream is, String ouptPath, String originalPath,int width, int height, boolean scale) throws IOException {
		FileUtil.copyFile(is, new File(ouptPath));
		if(originalPath != null && !originalPath.trim().isEmpty() ){
            imageZip(ouptPath, originalPath, 1024, 768, true);//保存压缩后的原图（目前只为补充缩略图截图后功能缺失用）
		}
		imageZip(ouptPath, ouptPath, width, height, scale);
	}
	/**
	 * 图片缩放和截取处理
	 * 
	 * @author lfq
	 * @time 2014-11-17 上午10:52:58
	 * @param inputPath
	 *            原图片路径
	 * @param ouptPath
	 *            输出路径
	 * @param width
	 *            宽度,如果是等比例缩放该参数代表最大宽度,否则表示压缩的宽度
	 * @param height
	 *            高度,如果是等比例缩放该参数代表最大高度,否则表示压缩的高度
	 * @param scale
	 *            是否等比例缩放,如果该值为false并且原图小于with或height时图片将会被放大
	 * @throws IOException
	 */
	public static void imageZip(String inputPath, String ouptPath,int width, int height, boolean scale) throws IOException {
		File oldFile=new File(inputPath);
		// 获得源文件
		BufferedImage oldImg = ImageIO.read(oldFile);
		int oldWidth=oldImg.getWidth();
		int oldHeight=oldImg.getHeight();
		boolean com=false;//标识原图是否大于要压缩的尺寸
		if (oldWidth>width || oldHeight>height) {
			com=true;
		}
		
		if (com) {//原图大于要压缩的尺寸
			if (scale) {//等比例压缩
				int newWidth;
				int newHeight;
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) oldWidth)/width;// + 0.1;
				double rate2 = ((double) oldHeight)/height;// + 0.1;
				// 根据缩放比率大的进行缩放控制
				double rate = rate1 > rate2 ? rate1 : rate2;
				newWidth = (int) (oldWidth/ rate);
				newHeight = (int) (oldHeight / rate);
				
				BufferedImage newImg = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_RGB);
				//Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				newImg.getGraphics().drawImage(oldImg.getScaledInstance(newWidth, newHeight,Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = new FileOutputStream(ouptPath);
				// JPEGImageEncoder可适用于其他图片类型的转换
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(newImg);
				out.close();
				
				System.out.println("原图大于要压缩的尺寸等比例压缩");
			}else{//非等比例压缩 
				int newWidth;
				int newHeight;
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) oldWidth)/width;//+ 0.1;
				double rate2 = ((double) oldHeight)/height;// + 0.1;
				// 根据缩放比率大的进行缩放控制
				double rate = rate1 > rate2 ? rate1 : rate2;
				while (true) {//确定压缩的图片要大于截图的图
					newWidth = (int) (oldWidth/ rate);
					newHeight = (int) (oldHeight / rate);
					if (newWidth>=width && newHeight>=height) {
						break;
					}
					rate=rate-0.01;
				}
				
				//1.先压缩
				BufferedImage newImg = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_RGB);
				//Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				newImg.getGraphics().drawImage(oldImg.getScaledInstance(newWidth, newHeight,Image.SCALE_SMOOTH), 0, 0, null);
				
				//2.再截图
				BufferedImage newImg2 = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
				newImg2.getGraphics().drawImage(newImg, 0, 0, null);
				
				//3.输出结果图片
				FileOutputStream out = new FileOutputStream(ouptPath);
				// JPEGImageEncoder可适用于其他图片类型的转换
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(newImg2);
				out.close();
				
				System.out.println("原图大于要压缩的尺寸非等比例压缩");
			}
		}else{//原图小于要压缩的尺寸
			if (scale) {//等比例压缩
				//直接显示原图
				if (oldFile.length()<1024*10) {//小于10kb的直接用原图
					if (!inputPath.equals(ouptPath)) {
						FileInputStream input=new FileInputStream(oldFile);
						FileOutputStream out = new FileOutputStream(ouptPath);
						byte[] buffer = new byte[255];
						int length = 0;
						while ((length = input.read(buffer)) > 0) {
							out.write(buffer, 0, length);
						}
						input.close();
						out.close();
					}
				}else{//否则使用工具类处理让图片变小但质量会有变化
					// JPEGImageEncoder可适用于其他图片类型的转换
					FileOutputStream out = new FileOutputStream(ouptPath);
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					encoder.encode(oldImg);
					out.close();
				}
				
				System.out.println("原图小于要压缩的尺寸等比例压缩");
			}else{//非等比例压缩
				int newWidth;
				int newHeight;
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) oldWidth)/width;// + 0.1;
				double rate2 = ((double) oldHeight)/height;// + 0.1;
				// 根据缩放比率大的进行缩放控制
				double rate = rate1 > rate2 ? rate1 : rate2;
				
				do{//确定压缩的图片要大于截图的图
					newWidth = (int) (oldWidth/ rate);
					newHeight = (int) (oldHeight / rate);
					if (newWidth>=width && newHeight>=height) {
						break;
					}
					rate=rate-0.01;
				}while(true);
				
				//1.先放大
				BufferedImage newImg = new BufferedImage(newWidth,newHeight, BufferedImage.TYPE_INT_RGB);
				//Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				newImg.getGraphics().drawImage(oldImg.getScaledInstance(newWidth, newHeight,Image.SCALE_SMOOTH), 0, 0, null);
				
				//2.再截图
				BufferedImage newImg2 = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
				newImg2.getGraphics().drawImage(newImg, 0, 0, null);
				
				//3.输出结果图片
				FileOutputStream out = new FileOutputStream(ouptPath);
				// JPEGImageEncoder可适用于其他图片类型的转换
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(newImg2);
				out.close();
				System.out.println("原图小于要压缩的尺寸非等比例压缩");
			}
		}
	}

	/**
	 * 按尺寸质量压缩（测试）
	 * @param is
	 * @param to
	 * @param quality
	 * @throws IOException
	 */
	public static void compressQuality(InputStream is, File to, float quality, int maxWidth, int maxHeight) throws IOException{
		//压缩大小
		BufferedImage image = ImageIO.read(is);
		if(image==null)return;
		Integer width = image.getWidth();
		Integer height = image.getHeight();
		if(width>maxWidth || height>maxHeight){
			ResampleOp resampleOp = new ResampleOp(DimensionConstrain.createMaxDimension(maxWidth, maxHeight));
			image = resampleOp.filter(image, null);
		}
		// 得到指定Format图片的writer 
		Iterator<ImageWriter> iter =  ImageIO.getImageWritersByFormatName("jpeg");
		ImageWriter imageWriter = iter.next();
		
		// 得到指定writer的输出参数设置(ImageWriteParam )  
        ImageWriteParam iwp = imageWriter.getDefaultWriteParam();  
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩  
        iwp.setCompressionQuality(quality); // 设置压缩质量参数  
        iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
        
        ColorModel colorModel = ColorModel.getRGBdefault();  
        // 指定压缩时使用的色彩模式  
        iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel,  
                colorModel.createCompatibleSampleModel(16, 16)));
        
        // 开始打包图片，写入byte[]  
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流  
        IIOImage iIamge = new IIOImage(image, null, null); 
        
        // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput  
        // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput  
        imageWriter.setOutput(ImageIO  
                .createImageOutputStream(byteArrayOutputStream));  
        imageWriter.write(null, iIamge, iwp); 
        
        InputStream sbs = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ImageIO.write(ImageIO.read(sbs), "png", to);
        
	}

	private final class Location{
		public static final int LEFT_TOP=1; //左上角
		public static final int RIGHT_TOP=2; //右上角
		public static final int LEFT_BOTTOM=3; //左下角
		public static final int RIGHT_BOTTOM=4; //右下角
		public static final int CENTER=5; //居中
		
	}
	 /**   
     * 给图片添加水印、可设置水印图片旋转角度   
     * @param iconPath 水印图片路径   
     * @param srcImgPath 源图片路径    
     * @param degree 水印图片旋转角度 
     * @param transparency 透明度 10-1 越来越透明
     * @param location 位置 1 左上角 2 右上角 3 左下角 4 右下角 5 居中 
     * 
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,     
           Integer degree,int transparency,int location) {     
    	FileOutputStream out =null;
        try {     
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),     
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);     
            int width=srcImg.getWidth(null);
            int height=srcImg.getHeight(null);
            Graphics2D g = buffImg.createGraphics();     
            
             
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,     
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);     
    
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg     
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);     
    
            if (null != degree) {     
                // 设置水印旋转     
                g.rotate(Math.toRadians(degree),     
                        (double) buffImg.getWidth() / 2, (double) buffImg     
                                .getHeight() / 2);     
            }     
            ImageIcon imgIcon = new ImageIcon(iconPath); 
            imgIcon.setImage(imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));//水印跟原图一样大
            int widthIcon=imgIcon.getIconWidth();
            int heightIcon=imgIcon.getIconHeight();
            Image img = imgIcon.getImage();     
            float alpha =(float)transparency/10;  // 透明度  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,     
                    alpha));
            int x=0;//x轴
            int y=0;//y轴
            if(location!=0) {
            	switch(location) {
            	case Location.LEFT_TOP:break;
            	case Location.RIGHT_TOP:x=width-widthIcon;break;
            	case Location.LEFT_BOTTOM:y=height-heightIcon;break;
            	case Location.RIGHT_BOTTOM:x=width-widthIcon;y=height-heightIcon;break;
            	case Location.CENTER:x=(width-widthIcon)/2;y=(height-heightIcon)/2;break;
            	default :break;
            	}
            }
            g.drawImage(img, x,y, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));     
            g.dispose();     
            out = new FileOutputStream(srcImgPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(buffImg);
        } catch (Exception e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if (null != out)     
                    out.close();     
            } catch (Exception e) {     
                e.printStackTrace();     
            }     
        }     
    }
    /**
     * 打印文字水印图片
     * 
     * @param pressText
     *            --文字
     * @param targetImg --
     *            目标图片
     * @param fontName --
     *            字体名
     * @param fontStyle --
     *            字体样式
     * @param color --
     *            字体颜色
     * @param fontSize --
     *            字体大小
     * @param x --
     *            偏移量
     * @param y
     */
 
    public static void pressText(String pressText, String targetImg,
            String fontName, int fontStyle, int color, int fontSize, int x,
            int y) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
             
            g.setColor(Color.RED);
            g.setFont(new Font(fontName, fontStyle, fontSize));
 
            g.drawString(pressText, wideth - fontSize - x, height - fontSize
                    / 2 - y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String getFormat(String filePath){
        return RegexUtil.parse("\\.(\\w+)", filePath);
    }

    /**
     * 截图
     * 返回的是临时文件路径，用于删除
     * @author lfq
     * @param fromFile	 原图片
     * @param toFile	 生成的图片
     * @param imgWidth	图片缩放后的宽度
     * @param imgHeight 图片缩放后的高度
     * @param imgX		开始截取点x坐标
     * @param imgY		开始截取点y坐标
     * @param cutterWidth	生成截图的宽度
     * @param cutterHeight  生成截图的高度
     * @return
     * @throws Exception
     */
    public static String corp(String fromFile,String toFile,Integer imgWidth,Integer imgHeight
            ,Integer imgX,Integer imgY, Integer selectorX, Integer selectorY
            ,Integer cutterWidth,Integer cutterHeight) throws Exception{
        String tempFile=toFile.substring(0, toFile.lastIndexOf("."))+"_temp."+ImageUitl.getFormat(toFile);
        ImageUitl.resize(fromFile, tempFile, imgWidth, imgHeight);//缩放
        //构建图片对象
        BufferedImage _image = new BufferedImage(cutterWidth, cutterHeight,BufferedImage.TYPE_INT_RGB);
        //绘制缩小后的图
        Image srcImage = ImageIO.read(new File(tempFile));
        Graphics2D g2 = (Graphics2D) _image.getGraphics();
        g2.setBackground(Color.WHITE);//设置背景色
        g2.clearRect(0, 0, cutterWidth, cutterHeight);
        g2.drawRect(0, 0, cutterWidth, cutterHeight);
        g2.drawImage(srcImage, imgX - selectorX, imgY - selectorY, null);
        g2.dispose();

        FileOutputStream out = new FileOutputStream(toFile);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(_image);
        out.flush();
        out.close();
        return tempFile;
    }

    /**
     * 强制压缩/放大图片到固定的大小
     * @param srcImgPath
     * @param outImgPath
     * @param w int 新宽度
     * @param h int 新高度
     * @throws IOException
     */
    public static void resize(String srcImgPath,String outImgPath,int w, int h) throws IOException {
        //源图片
        BufferedImage src=ImageIO.read(new File(srcImgPath));
        //构建图片对象
        BufferedImage newImg =null;
        // 判断输入图片的类型
        switch (src.getType()) {
			/*case 13:
				newImg = new BufferedImage(w, h,BufferedImage.TYPE_4BYTE_ABGR);
				break;*/
            default:
                newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                break;
        }
        Graphics2D g = newImg.createGraphics();
        g.setBackground(Color.WHITE);//设置背景色
        g.clearRect(0, 0, w, h);//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
        g.drawRect(0, 0, w, h);
        // 根据图片尺寸压缩比得到新图的尺寸
        g.drawImage(src.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0,null);
        g.dispose();
        //输出到文件流
        File file = new File(outImgPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {// 输出到文件流
            ImageIO.write(newImg,outImgPath.substring(outImgPath.lastIndexOf(".") + 1),new File(outImgPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
