package com.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.springframework.stereotype.Controller;

@Controller
public class CryptUtil
{
	
	public static String SPKEY = "ruichiyanfa";

	private byte[] md5(String strSrc)throws Exception
	{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		return md5.digest(strSrc.getBytes("GBK"));  
	}

	/**
	 * 进行md5加密
	 * @param source
	 * @return
	 */
	public static String MD5encrypt(String source)
	{
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 得到3-DES的密钥匙
	 * 根据接口规范，密钥匙为24个字节，md5加密出来的是16个字节，因此后面补8个字节的0
	 * 
	 * @param String
	 *            原始的SPKEY
	 * @return byte[] 指定加密方式为md5后的byte[]
	 */
	private byte[] getEnKey(String spKey)throws Exception
	{
		byte[] desKey1 = md5(spKey);
		byte []desKey = new byte[24];
		int i = 0;
		while (i < desKey1.length && i < 24)
		{
			desKey[i] = desKey1[i];
			i++;
		}
		if (i < 24)
		{
			desKey[i] = 0;
			i++;
		}
		return desKey;
	}

	/**
	 * 3-DES加密
	 * 
	 * @param byte[] src 要进行3-DES加密的byte[]
	 * @param byte[] enKey 3-DES加密密钥
	 * @return byte[] 3-DES加密后的byte[]
	 */
	private byte[] Encrypt(String src, byte[] enKey)throws Exception
	{
		DESedeKeySpec dks = new DESedeKeySpec(enKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return  cipher.doFinal(src.getBytes("UTF-16LE"));
	}



	/**
	 * 进行3-DES解密（密钥匙等同于加密的密钥匙）。
	 * 
	 * @param byte[] src 要进行3-DES解密byte[]
	 * @param String
	 *            spkey分配的SPKEY
	 * @return String 3-DES解密后的String
	 */
	private String deCrypt(byte[] debase64, String spKey)throws Exception
	{
		Cipher  cipher = Cipher.getInstance("DESede");
		byte[] key = getEnKey(spKey);
		DESedeKeySpec dks = new DESedeKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey sKey = keyFactory.generateSecret(dks);
		cipher.init(Cipher.DECRYPT_MODE, sKey);
		byte ciphertext[] = cipher.doFinal(debase64);
		String strDe = new String(ciphertext, "UTF-16LE");
		return strDe;
	}

	/**
	 * 3-DES解密
	 * 
	 * 
	 * 进行base64解码
	 */
	public String get3DESDecrypt(String src, String spkey)throws Exception
	{
		String source = Base64.decode(src); 
		return deCrypt(Base64.oldDecode(source), spkey);
	}
	
	/**
	 * 3-DES加密
	 * 
	 * 进行base64编码
	 */
	public String get3DESEncrypt(String src, String spkey)throws Exception
	{
		// 得到3-DES的密钥匙
		byte[] enKey = getEnKey(spkey);
		byte[] encryptedData = Encrypt(src, enKey);

		String source = Base64.encodeBytes(encryptedData);
		
		return Base64.encode(source);
	}

	public static void main(String[] args)throws Exception
	{
		
//	    String keyInfo =  "fasfafasfaf864371354321321144162817732132-121GV.N..1/L;][1F3131F3414.F0A3RF41FS1F$%^*#^#^(&(&*$&$&^(&字节转变为4个字节，因此，编码后的代码量（以字节为单位，下同）约比编码前的代码量多了1/3。之所以说是“约”，是因为如果代码量正好是3的整数倍，/3呢？ 细心的人可能已经注意到了，在The Base64 Alphabet中的最后一个有一个(pad) =字符。这个字符的目的就是用来处理这个问题的。 当代码量不是3的整数倍时，代码量/3的余数自然就是2或者1。转换的时候，结位的用0来补上相应的位置，之后再在6位的前面补两个0。转换完空出的结果就用就用“=”来补位。譬如结果若最后余下的为2个字节的“张”： ";
//	    System.out.println("进行3-DES加密前的内容: " + keyInfo);
//
//		CryptUtil test = new CryptUtil();
//		String Value = test.get3DESEncrypt(keyInfo,SPKEY);
//		System.out.println("进行3-DES加密后的内容: "+Value);
//		
//		String reValue = test.get3DESDecrypt(Value, SPKEY);
//		System.out.println("进行3-DES解密后的内容: " + reValue);
//		System.out.println(keyInfo.equals(keyInfo));
		CryptUtil test = new CryptUtil();
//		System.out.println(test.get3DESDecrypt("QlU3anZWcUI1bjVobDVIbVhEekR1bTMvbUw3TWtQSmRxZWo0cGdSOE43a3lMUVJ0ZENPLzNRPT0=", SPKEY));
//		String code = "2014-12-30,D0-27-88-16-B1-FF";
		System.out.println(test.md5("123456").toString());
//		System.out.println(test.get3DESEncrypt(code, SPKEY));
	}
}
