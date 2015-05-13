<%@page import="org.nutz.json.Json"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	
	Map result = new HashMap();
	//各种请求的处理
	String act = request.getParameter("action");
	if("config".equals(act)){
		out.write( new ActionEnter( request, rootPath ).exec() );
	}else if("uploadimage".equals(act)){
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		Map<String, List<FileItem>> map = upload.parseParameterMap(request);
		List<FileItem> list = map.get("upfile");
		for(FileItem fileItem : list){
			String expandedName = fileItem.getName().substring(fileItem.getName().lastIndexOf(".")); 
	        String fileName = java.util.UUID.randomUUID().toString();  //采用时间+UUID的方式随即命名    
	        fileName += expandedName;    

        	String uploadPath = request.getServletContext().getRealPath("/uploadImages");    
	        File file = new File(uploadPath);  
	        if(!file.exists()){  //如果路径不存在，创建  
	            file.mkdirs();  
	        }  
	        File toFile = new File(uploadPath, fileName); 
			fileItem.write(toFile);
	        
	        // 返回"图像"选项卡并显示图片    
	        String url = request.getContextPath() + "/uploadImages/" + fileName;
			result.put("state", "SUCCESS");
			result.put("url", url);
			result.put("title", fileItem.getName());
			result.put("original", fileItem.getName());
			out.println(Json.toJson(result));
		}
	}
%>