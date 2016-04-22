package com.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 手机消息发送
 * @author ysq
 */
public class SendMessagesUtil {
	
	private final String userId="RC88";
	//用户名
	private final String pass="12345678";
	//用户密码
	
	
	
	/**
	 * HTTP POST 发送方式(短信发送)
	 * phonenumber 手机号码（以逗号分割）
	 * content  发送内容
	 * sendtime   定时发送时间（为空时表示即时发送）
	 * @throws IOException 
	 */
	public String sendByHttpPost(String phonenumber,String content,Date sendtime) throws IOException{
		
		
		
		InputStream inp = SendMessagesUtil.class.getClassLoader().getResourceAsStream("message.properties");
		Properties p = new Properties();
		try{
			p.load(inp);
			inp.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(p.getProperty("paramone"));//CorpID=RC88&Pwd=12345678&Mobile=
		buffer.append(phonenumber);
		buffer.append(p.getProperty("paramtwo"));//&Content=
		buffer.append(content);
		//buffer.append(content + "【"+p.getProperty("msgsuffix")+"】");//短信的后缀【华颖学校】
		if(sendtime != null){
			buffer.append(p.getProperty("paramthree"));//&SendTime=
			buffer.append(this.dateToString(sendtime));
		}
		
		URL url = new URL(p.getProperty("sendurl"));
		URLConnection connection = url.openConnection();
		
		connection.setDoOutput(true);
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"GBK");
		
		out.write(buffer.toString());
		
		out.flush();
		out.close();
		
		String sCurrentLine = "";
		String sTotalString = "";
		InputStream l_urlStream = connection.getInputStream();
		
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));
		while(((sCurrentLine = l_reader.readLine())) != null ){
			sTotalString += sCurrentLine;
		}
		l_urlStream.close();
		l_reader.close();
		
		return sTotalString;
	}
	
	/**
	 * 时间转换
	 * 如：2012-12-12 12:12:12  -->   20121212121212
	 */
	private String dateToString(Date sendtime) {
		SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = simpledate.format(sendtime);
		String formatdate = ((date.replaceAll("-","")).replaceAll(" ","")).replaceAll(":","");
		return formatdate;
	}
	
	/**
	 * HTTP POST 方式(查询剩余条数)
	 * @throws IOException 
	 */
	public String inquiryBalance() throws IOException{
		InputStream inp = SendMessagesUtil.class.getClassLoader().getResourceAsStream("message.properties");
		Properties p = new Properties();
		try{
			p.load(inp);
			inp.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		URL url = new URL(p.getProperty("inquiryurl"));
		URLConnection connection = url.openConnection();
		
		connection.setDoOutput(true);
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"GBK");
		
		out.write(p.getProperty("iparamone"));
		
		out.flush();
		out.close();
		
		String sCurrentLine = "";
		String sTotalString = "";
		InputStream l_urlStream = connection.getInputStream();
		
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));
		while(((sCurrentLine = l_reader.readLine())) != null ){
			sTotalString += sCurrentLine;
		}
		l_urlStream.close();
		l_reader.close();
		
		return sTotalString;
	}
	
	
	public static void main(String[] args) throws IOException {
		SendMessagesUtil smu = new SendMessagesUtil();
		//String str = smu.dateToString(new Date());
		//System.out.println(str);
//		Date d = new Date();
		smu.sendByHttpPost("18520297087", "我的门窗坏了，请工程部检修", null);
		
		
		/*InputStream inp = SendMessagesUtil.class.getClassLoader().getResourceAsStream("message.properties");
		Properties p = new Properties();
		try{
			p.load(inp);
			inp.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		StringBuffer buffer = new StringBuffer();
		System.out.println("msgsuffix=="+ p.getProperty("msgsuffix"));*/
	}
	
	/**
	 * 接收Soap流
	 */
	private String getSoapSmssend(String userid,String pass,String mobiles,String msg,String time)
    {
        try 
        {
            String soap = "";
            soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            		+"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            		+"<soap:Body>"
            		+"<BatchSend xmlns=\"http://tempuri.org/\">"
            		+"<CorpID>"+userid+"</CorpID>"
            		+"<Pwd>"+pass+"</Pwd>"
            		+"<Mobile>"+mobiles+"</Mobile>"
            		+"<Content>"+msg+"</Content>"
            		+"<Cell>"+""+"</Cell>" 
            		+"<SendTime>"+time+"</SendTime>"
            		+"</BatchSend>"
            		+"</soap:Body>"
            		+"</soap:Envelope>";                        
            return soap;
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
	
	/**
	 * 解析Soap流
	 * @author ysq
	 * 2011-3-16上午09:07:27
	 * @param userid 用户名
	 * @param pass 用户密码
	 * @param mobiles 手机号码
	 * @param msg 手机消息
	 * @param time 发送时间
	 * @return
	 * @throws Exception
	 */
	private InputStream getSoapInputStream(String userid,String pass,String mobiles,String msg,String time)throws Exception
    {
		URLConnection conn = null;
		InputStream is = null;
        try
        {
            String soap=getSoapSmssend(userid,pass,mobiles,msg,time);            
            if(soap==null)
            {
                return null;
            }
            try{
             
            	URL url=new URL("http://web.vv106.com/WS/LinkWS.asmx");     
            	
            	conn=url.openConnection();
            	conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);                           
                conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
                conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                //conn.setRequestProperty("HOST","web.vv106.com/WS");
                conn.setRequestProperty("SOAPAction","\"http://tempuri.org/BatchSend\"");

                OutputStream os=conn.getOutputStream();
                OutputStreamWriter osw=new OutputStreamWriter(os,"utf-8");
                osw.write(soap);
                osw.flush();                
            }catch(Exception ex){
            	System.out.print("SmsSoap.openUrl error:"+ex.getMessage());
            }                                            
            try{
            	is=conn.getInputStream();            	
            }catch(Exception ex1){
            	System.out.print("SmsSoap.getUrl error:"+ex1.getMessage());
            }
            
            return is;   
        }
        catch(Exception e)
        {
        	System.out.print("SmsSoap.InputStream error:"+e.getMessage());
            return null;
        }
    }
	
	/**
	 * 发送手机信息
	 * @param mobiles 用户手机(发送多条，请以英文逗号分隔)
	 * @param msg 消息内容
	 * @param time 定时发送（为空则为即时）
	 * @return
	 */
	public String sendSmsByWebService(String mobiles,String msg,String time)
    {
        String result = "-2";        
		try
        {
            Document doc;
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db=dbf.newDocumentBuilder();
            InputStream is=getSoapInputStream(this.userId,this.pass,mobiles,msg + "【华侨中学】",time);
            if(is!=null){
	            doc=db.parse(is);
	            NodeList nl=doc.getElementsByTagName("BatchSendResult");
	            Node n=nl.item(0);
	            result=n.getFirstChild().getNodeValue();
	            is.close();
            }
            return result;
        }
        catch(Exception e)
        {
        	System.out.print("SmsSoap.sendSms error:"+e.getMessage());
            return "-2";
        }
    } 
	
	/**
	 * 得到soap用户信息流
	 * @author ysq
	 * 2011-3-16上午09:09:57
	 * @param userid
	 * @param pass
	 * @return
	 */
	private String getSoapUserInfo(String userid,String pass)
    {
        try 
        {
            String soap = "";
            soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            		+"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            		+"<soap:Body>"
            		+"<SelSum xmlns=\"http://tempuri.org/\">"
            		+"<CorpID>"+userid+"</CorpID>"
            		+"<Pwd>"+pass+"</Pwd>"           		
            		+"</SelSum>"
            		+"</soap:Body>"
            		+"</soap:Envelope>";                        
            return soap;
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
	/**
	 * 解析soap用户信息流
	 * @author ysq
	 * 2011-3-16上午09:10:46
	 * @param userid
	 * @param pass
	 * @return
	 * @throws Exception
	 */
    private InputStream getUserInfoInputStream(String userid,String pass)throws Exception
    {
		URLConnection conn = null;
		InputStream is = null;
        try
        {
            String soap=getSoapUserInfo(userid,pass);            
            if(soap==null)
            {
                return null;
            }
            try{
             
            	URL url=new URL("http://web.vv106.com/WS/LinkWS.asmx");     
            	
            	conn=url.openConnection();
            	conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);                           
                conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
                conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                //conn.setRequestProperty("HOST","web.vv106.com/WS");
                conn.setRequestProperty("SOAPAction","\"http://tempuri.org/SelSum\"");

                OutputStream os=conn.getOutputStream();
                OutputStreamWriter osw=new OutputStreamWriter(os,"utf-8");
                osw.write(soap);
                osw.flush();                
            }catch(Exception ex){
            	System.out.print("SmsSoap.openUrl error:"+ex.getMessage());
            }                                            
            try{
            	is=conn.getInputStream();            	
            }catch(Exception ex1){
            	System.out.print("SmsSoap.getUrl error:"+ex1.getMessage());
            }
            
            return is;   
        }
        catch(Exception e)
        {
        	System.out.print("SmsSoap.InputStream error:"+e.getMessage());
            return null;
        }
    }	 

	/**
	 * 得到用户信息
	 * @author ysq
	 * 2011-3-16上午09:11:09
	 * @param userid 用户名
	 * @param pass 用户密码
	 * @return
	 */
	private String GetInfo()
    {
        String result = "-2";
		try
        {
            Document doc;
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db=dbf.newDocumentBuilder();
            InputStream is=getUserInfoInputStream(userId,pass);
            if(is!=null){
	            doc=db.parse(is);
	            NodeList nl=doc.getElementsByTagName("SelSumResult");
	            Node n=nl.item(0);
	            result=n.getFirstChild().getNodeValue();
	            is.close();
            }    
            return result;
        }
        catch(Exception e)
        {
        	System.out.print("SmsSoap.sendSms error:"+e.getMessage());
            return "-2";
        }
    }
	/**
	 * 得到用户可发消息数
	 * @author ysq
	 * 2011-3-16上午10:14:15
	 * @return
	 */
	public int Getbalance(){
		//得到平台返回信息
		String result=this.GetInfo();
		if(result=="-2"){
			return 0;
		}
		int count=(Integer.parseInt(result));
		return count;
	}
}
