package com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class FileUtil {

	public static void main(String[] args) {
		String filePath="/config/SystemConfig.properties";
		//Class.getResourceAsStream("com/xxx/app/demo.xml");
		readSensitiveWords(filePath); //读取properties文件  
		//writePropertiesFile(filePath); //写properties文件  
        //readPropertiesFileFromXML(readxmlfile); //读取XML文件  
        //writePropertiesFileToXML(writexmlfile); //写XML文件 
	}

	
	 //读取资源文件,并处理中文乱码  
    public static String readSensitiveWords(String filename)  {  
        Properties properties = new Properties();  
        try  
        {  
            InputStream inputStream = FileUtil.class.getResourceAsStream(filename);  
            properties.load(inputStream);  
            inputStream.close(); //关闭流  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        String sensitiveWords = properties.getProperty("sensitiveWords");  
       /* try  
        {  
        	sensitiveWords = new String(sensitiveWords.getBytes("ISO-8859-1"), "UTF-8"); // 处理中文乱码  
        }  
        catch (UnsupportedEncodingException e)  
        {  
            e.printStackTrace();  
        }  */
        return sensitiveWords;  
    }  
  
    //读取XML文件,并处理中文乱码  
    public static void readPropertiesFileFromXML(String filename)  
    {  
        Properties properties = new Properties();  
        try  
        {  
            InputStream inputStream = FileUtil.class.getResourceAsStream(filename);  
            properties.loadFromXML(inputStream);  
            inputStream.close();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        String username = properties.getProperty("username");  
        String passsword = properties.getProperty("password");  
        String chinese = properties.getProperty("chinese"); //XML中的中文不用处理乱码，正常显示  
        System.out.println(username);  
        System.out.println(passsword);  
        System.out.println(chinese);  
    }  
  
    //写资源文件，含中文  
    public static void writeSensitiveWords(String filename, String sensitiveWords) {  
        Properties properties = new Properties();  
        try  
        {  
        	File file = new File(FileUtil.class.getResource(filename).getFile()); 
            OutputStream outputStream = new FileOutputStream(file);  
            properties.setProperty("sensitiveWords", sensitiveWords);  
            properties.store(outputStream, "");  
            outputStream.close();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }  
  
    //写资源文件到XML文件，含中文    
    public static void writePropertiesFileToXML(String filename)  
    {  
        Properties properties = new Properties();  
        try  
        {  
            OutputStream outputStream = new FileOutputStream(filename);  
            properties.setProperty("username", "myname");  
            properties.setProperty("password", "mypassword");  
            properties.setProperty("chinese", "中文");  
            properties.storeToXML(outputStream, "author: shixing_11@sina.com");  
            outputStream.close();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }  

}
