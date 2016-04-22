package com.base.init;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {  
  
    private static final long serialVersionUID = 1L;  
  
    public void init(ServletConfig config) throws ServletException {  
        super.init(config);  
        String fileSep = System.getProperty("file.separator");  
        String prefix = getServletContext().getRealPath(fileSep) + fileSep;  
        String initfileName = getInitParameter("log4j-init-file");  
  
        FileInputStream fis;  
        Properties prop = new Properties();  
        try {  
            fis = new FileInputStream(prefix + initfileName);  
            prop.load(fis);  
            String logfileName = prop.getProperty("log4j.appender.A1.file");  
            prop.put("log4j.appender.A1.file", prefix + logfileName);  
            if (initfileName != null) {  
                PropertyConfigurator.configure(prop);  
            }  
            fis.close();  
        } catch (FileNotFoundException e) {  
            System.out.println(e);  
        } catch (IOException e) {  
            System.out.println(e);  
        }  
    }  
  
    protected void doGet(HttpServletRequest request,  
            HttpServletResponse response) throws ServletException, IOException {  
    }  
}  
