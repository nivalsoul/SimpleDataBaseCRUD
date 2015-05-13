package com.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.MidiDevice.Info;

import org.nutz.dao.Dao;
import org.nutz.json.Json;

import com.common.DB2JavaBean;
import com.dao.DaoFactory;
import com.util.BeanUtil;
import com.util.StringUtil;

/**
 * Servlet implementation class FieldServlet
 */
@WebServlet(name = "fieldServlet", urlPatterns = { "/fieldServlet" })
public class FieldServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FieldServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8"); 
	    response.setCharacterEncoding("utf-8"); 
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter out = response.getWriter(); 
	    String opt = request.getParameter("opt");
	    
	    if ("readTableInfo".equals(opt)) {
			String tableName = request.getParameter("tableName");
			Map<String, Object> map = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> pkList = new ArrayList<Map<String, Object>>();
			try {
				ResourceBundle rb = ResourceBundle.getBundle("config.DBConfig");
				Class.forName(rb.getString("driver"));
				Connection conn = DriverManager.getConnection(
						rb.getString("url"), 
						rb.getString("userName"),
						rb.getString("password"));
				/*
				 * PreparedStatement ps = conn.prepareStatement(
				 * "SELECT TABLE_SCHEMA,COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM information_schema.columns a WHERE TABLE_NAME='Story'"
				 * ); ResultSet rs = ps.executeQuery(); while (rs.next()) {
				 * System
				 * .out.println(rs.getString(1)+"--"+rs.getString(2)+"--"+rs
				 * .getString(3)+"--"+rs.getString(4)); }
				 */
				
				DatabaseMetaData databaseMetaData = conn.getMetaData();
				ResultSet columnSet = databaseMetaData.getColumns(null, "%",tableName, "%");
				if (null != columnSet) {
					while (columnSet.next()) {
						Map<String, Object> field = new HashMap<>();
						// 列名
						field.put("fieldName", columnSet.getString("COLUMN_NAME"));
						// 备注
						field.put("comment", columnSet.getString("REMARKS"));
						// 列类型
						field.put("dataType", columnSet.getString("TYPE_NAME").toLowerCase());
						list.add(field);
					}
				}
				ResultSet pks = databaseMetaData.getPrimaryKeys(null, "%",tableName);
				if (null != pks) {
					while (pks.next()) {
						Map<String, Object> field = new HashMap<>();
						field.put("pkName", pks.getString("COLUMN_NAME"));
						pkList.add(field);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			map.put("fields", list);
			map.put("pkFields", pkList);
			out.print(Json.toJson(map));
		}else if ("create".equals(opt)) {
			String tableName = request.getParameter("tableName");
			String servletName = request.getParameter("servletName");
			String listName = request.getParameter("listName");
			String listFields = request.getParameter("listFields");
			String searchFields = request.getParameter("searchFields");
			String editUrl = request.getParameter("editUrl");
			String viewUrl = request.getParameter("viewUrl");
			String pageSize = request.getParameter("pageSize");
			String editName = request.getParameter("editName");
			String editFields = request.getParameter("editFields");
			String hideFields = request.getParameter("hideFields");
			String viewName = request.getParameter("viewName");
			String viewFields = request.getParameter("viewFields");
			listFields = listFields.replaceAll("\\[\\[", "\\[\n\t\t\\[").replaceAll("\\],\\[", "\\],\n\t\t\\[").replaceAll("\\]\\]", "\\]\n\t\\]");
			searchFields = searchFields.replaceAll("\\[\\[", "\\[\n\t\t\\[").replaceAll("\\],\\[", "\\],\n\t\t\\[").replaceAll("\\]\\]", "\\]\n\t\\]");
			/*editFields = editFields.replaceAll("\\[\\[\\[", "\\[\n\t\t\\[\\[").replaceAll("\\]\\],\\[\\[", "\\]\\],\n\t\t\\[\\[").replaceAll("\\]\\]\\];", "\\]\\]\n\t\\];")
					.replaceAll("\\[\\[", "\\[\n\t\t\\[").replaceAll("\\],\\[", "\\],\n\t\t\\[").replaceAll("\\]\\]", "\\]\n\t\\]");
			hideFields = hideFields.replaceAll("\\[\\[", "\\[\n\t\t\\[").replaceAll("\\],\\[", "\\],\n\t\t\\[").replaceAll("\\]\\]", "\\]\n\t\\]");
			viewFields = viewFields.replaceAll("\\[\\[", "\\[\n\t\t\\[").replaceAll("\\],\\[", "\\],\n\t\t\\[").replaceAll("\\]\\]", "\\]\n\t\\]");*/
			
			ResourceBundle rb = ResourceBundle.getBundle("config.SystemConfig");
			//创建数据库表对应的Entity
			DB2JavaBean d2j = new DB2JavaBean(rb.getString("driver"), rb.getString("url"), 
					rb.getString("userName"), rb.getString("password"), tableName, 
					rb.getString("tableMatchPattern"), rb.getString("matchPattern"), 
					rb.getString("outputDir"), rb.getString("packageName"));
			d2j.init();
			
			String info="";
			//创建Servlet文件
			String servletTemplatePath=rb.getString("servletTemplatePath"); 
			String servletSavePath=rb.getString("servletSavePath"); 
			info += createServlet(tableName, servletName, servletTemplatePath, servletSavePath, rb.getString("packageName"));
			
			//创建html文件
			String htmlTemplatePath =rb.getString("htmlTemplatePath");
			String htmlSavePath = rb.getString("htmlSavePath");
			info += createHtmls(tableName, htmlTemplatePath , htmlSavePath, listName,
					servletName, editUrl, viewUrl, pageSize, listFields,
					searchFields, editName, editFields, hideFields, viewName,viewFields);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map .put("result", "ok");
			map.put("info", info);
			out.print(Json.toJson(map));
		}
	}
	
	/**
	 * 创建Servlet
	 * @param tableName 数据库表名
	 * @param servletName 页面输入的Servlet访问名称
	 * @param servletTemplatePath Servlet模板路径
	 * @param servletSavePath Servlet保存路径
	 * @param beanPackageName 对于的Entity包名
	 */
	private static String createServlet(String tableName, String servletName, 
			String servletTemplatePath, String servletSavePath, String beanPackageName) {
		// 定义必须的变量
		String packageName="com.servlet";  //包名，比如com.admin.servlet
		String beanName=tableName;  //数据库表对应的类名，比如User
		String description=tableName+"管理";  //servlet功能描述
		String tbName = StringUtil.firstToLowerCase(beanName);
		String urlPatterns="/"+tbName+"/"+tbName+"Servlet";  //servlet访问路径，比如/admin/roleServlet
		String servletClassName=beanName+"Servlet";  //servlet的类名，比如RoleServlet
		String listUrl=tbName+"/list.html";  //列表的路径，比如role/list.html，相对于urlPatterns的相对路径或者绝对路径
		String fileNme = servletSavePath+servletClassName+".java";//保存的Servlet文件名（不用修改）

		String info="";
		//创建文件
		try {
			String templateContent = "";
			byte[] bytes = readTemplate(servletTemplatePath);
			// 通过使用平台的默认字符集解码指定的 byte 数组，构造一个新的String。然后利用字符串的replaceAll()方法进行指定字符的替换
			templateContent = new String(bytes);
			// System.out.print(templateContent);
			templateContent = templateContent.replaceAll("###packageName###", packageName);
			templateContent = templateContent.replaceAll("###beanPackageName###", beanPackageName);
			templateContent = templateContent.replaceAll("###beanName###", beanName);
			templateContent = templateContent.replaceAll("###description###", description);
			templateContent = templateContent.replaceAll("###urlPatterns###", urlPatterns);
			templateContent = templateContent.replaceAll("###servletName###", servletClassName);
			templateContent = templateContent.replaceAll("###listUrl###", listUrl);
			// 使用平台的默认字符集将此 String 编码为 byte 序列，并将结果存储到一个新的 byte 数组中。
			byte tag_bytes[] = templateContent.getBytes();
			saveFile(fileNme, tag_bytes);
			
			info ="<div style='margin:10px 0'>已成功生成Servlet文件："+fileNme+"，请根据该文件中提示信息进行修改。</div>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * 创建html文件
	 * @param tableName
	 * @param templatePath
	 * @param savePath
	 * @param listName
	 * @param servletName
	 * @param editUrl
	 * @param viewUrl
	 * @param pageSize
	 * @param listFields
	 * @param searchFields
	 * @param editName
	 * @param editFields
	 * @param hideFields
	 * @param viewName
	 * @param viewFields
	 * @return
	 */
	private static String createHtmls(String tableName, String templatePath,
			String savePath, String listName, String servletName,
			String editUrl, String viewUrl, String pageSize, String listFields,
			String searchFields, String editName, String editFields,
			String hideFields, String viewName, String viewFields) {
		
		String tbName = StringUtil.firstToLowerCase(tableName);
		String info = "";
		//创建文件
		try {
			String templateContent = "";
			//创建列表页面
			String fileNme = savePath+tbName+"\\list.html";//保存的html文件名
			byte[] bytes = readTemplate(templatePath+"list.html.template");
			templateContent = new String(bytes );
			templateContent = templateContent.replaceAll("###listName###", listName);
			templateContent = templateContent.replaceAll("###servletName###", servletName);
			templateContent = templateContent.replaceAll("###editUrl###", editUrl);
			templateContent = templateContent.replaceAll("###viewUrl###", viewUrl);
			templateContent = templateContent.replaceAll("###pageSize###", pageSize);
			templateContent = templateContent.replaceAll("###listFields###", listFields);
			templateContent = templateContent.replaceAll("###searchFields###", searchFields);
			// 使用平台的默认字符集将此 String 编码为 byte 序列，并将结果存储到一个新的 byte 数组中。
			byte tag_bytes[] = templateContent.getBytes();
			saveFile(fileNme, tag_bytes);
			
			//创建编辑页面
			fileNme = savePath+tbName+"\\"+editUrl;//保存的html文件名
			bytes = readTemplate(templatePath+"edit.html.template");
			templateContent = new String(bytes );
			templateContent = templateContent.replaceAll("###editName###", editName);
			templateContent = templateContent.replaceAll("###servletName###", servletName);
			templateContent = templateContent.replaceAll("###editFields###", editFields);
			templateContent = templateContent.replaceAll("###hideFields###", hideFields);
			tag_bytes = templateContent.getBytes();
			saveFile(fileNme, tag_bytes);
			
			//创建查看页面
			fileNme = savePath+tbName+"\\"+viewUrl;//保存的html文件名
			bytes = readTemplate(templatePath+"view.html.template");
			templateContent = new String(bytes );
			templateContent = templateContent.replaceAll("###viewName###", viewName);
			templateContent = templateContent.replaceAll("###servletName###", servletName);
			templateContent = templateContent.replaceAll("###viewFields###", viewFields);
			tag_bytes = templateContent.getBytes();
			saveFile(fileNme, tag_bytes);
			
			info ="<div style='margin:10px 0'>html文件已保存到"+savePath+tbName+"目录下，若需改动请根据文件中提示进行修改。</div>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	private static byte[] readTemplate(String templatePath) throws Exception {
		byte bytes[]=null;
		FileInputStream fileinputstream = new FileInputStream(templatePath);// 读取模板文件
		int lenght = fileinputstream.available();
		bytes = new byte[lenght];
		fileinputstream.read(bytes);
		fileinputstream.close();
		return bytes;
	}
	
	private static void saveFile(String fileNme, byte[] tag_bytes) throws Exception {
		//先创建父目录
		new File(fileNme).getParentFile().mkdirs();
		// 建立文件输出流
		FileOutputStream fileoutputstream = new FileOutputStream(fileNme);
		fileoutputstream.write(tag_bytes);
		fileoutputstream.close();
	}


}
