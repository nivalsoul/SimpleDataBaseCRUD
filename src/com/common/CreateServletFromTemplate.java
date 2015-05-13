package com.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.util.StringUtil;

/**
 * <h1>根据模板创建servlet文件</h1>
 * 主要用于简单列表的增删改查功能<br>
 * 如果需要复杂操作，请修改相关逻辑
 * @author nivalsoul
 *
 */
public class CreateServletFromTemplate {

	public static void main(String[] args) {
		// 定义必须的变量
		 //模板路径（无须修改！）
		String templatePath="E:\\WorkSpace\\eclipse64\\appidv6m7v85xc3\\code\\src\\com\\common\\Servlet.template"; 
		String packageName="com.servlet";  //包名，比如com.admin.servlet
		String beanName="Question";  //数据库表对应的类名，比如User
		String description="问题管理";  //servlet功能描述
		String urlPatterns="/question/"+StringUtil.firstToLowerCase(beanName)+"Servlet";  //servlet访问路径，比如/admin/roleServlet
		String servletName=beanName+"Servlet";  //servlet的类名，比如RoleServlet
		String listUrl=StringUtil.firstToLowerCase(beanName)+"/list.html";  //列表的路径，比如role/list.html，相对于urlPatterns的相对路径或者绝对路径
		//servlet保存的路径
		String savePath="E:\\WorkSpace\\eclipse64\\appidv6m7v85xc3\\code\\src\\com\\servlet\\";  
		//保存的Servlet文件名（不用修改）
		String fileNme = savePath+servletName+".java";

		//创建文件
		try {
			String templateContent = "";
			FileInputStream fileinputstream = new FileInputStream(templatePath);// 读取模板文件
			// 下面四行：获得输入流的长度，然后建一个该长度的数组，再把输入流中的数据以字节的形式读入到数组中，最后关闭流
			int lenght = fileinputstream.available();
			byte bytes[] = new byte[lenght];
			fileinputstream.read(bytes);
			fileinputstream.close();
			// 通过使用平台的默认字符集解码指定的 byte 数组，构造一个新的String。然后利用字符串的replaceAll()方法进行指定字符的替换
			templateContent = new String(bytes);
			// System.out.print(templateContent);
			templateContent = templateContent.replaceAll("###packageName###", packageName);
			templateContent = templateContent.replaceAll("###beanName###", beanName);
			templateContent = templateContent.replaceAll("###description###", description);
			templateContent = templateContent.replaceAll("###urlPatterns###", urlPatterns);
			templateContent = templateContent.replaceAll("###servletName###", servletName);
			templateContent = templateContent.replaceAll("###listUrl###", listUrl);
			// 使用平台的默认字符集将此 String 编码为 byte 序列，并将结果存储到一个新的 byte 数组中。
			byte tag_bytes[] = templateContent.getBytes();
			// System.out.print(templateContent);
			FileOutputStream fileoutputstream;
			// 建立文件输出流
			fileoutputstream = new FileOutputStream(fileNme);
			fileoutputstream.write(tag_bytes);
			fileoutputstream.close();
			System.out.print("已成功生成Servlet文件："+packageName+"."+servletName+".java");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
