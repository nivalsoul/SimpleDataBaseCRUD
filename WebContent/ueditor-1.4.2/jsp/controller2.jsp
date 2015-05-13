<%@page import="com.util.SFTPFileUpload"%>
<%@page import="com.util.DES"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItem"%>
<%@page import="org.nutz.dao.Cnd"%>
<%@page import="com.entity.SysConfig"%>
<%@page import="com.dao.DaoFactory"%>
<%@page import="org.nutz.dao.Dao"%>
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
	response.setCharacterEncoding("UTF-8");
	
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
	        String url = request.getRequestURL().toString().split("/sstcdcms")[0];
	       
	        Dao dao = DaoFactory.getDao();
	        SysConfig sc = dao.fetch(SysConfig.class,Cnd.where("type", "=", "domain").and("code", "=", "sstcd"));
	        if(sc != null)
	        	url = "http://" + sc.getValue();
	        sc = dao.fetch(SysConfig.class,Cnd.where("type", "=", "url").and("code", "=", "imgUrl"));
	        String imgUrl = sc.getValue();//图片访问路径，不包括前面的ip部分，形如：/data/youth1/images
	        if(!imgUrl.endsWith("/"))
	        	imgUrl += "/";
	        sc = dao.fetch(SysConfig.class,Cnd.where("type", "=", "file").and("code", "=", "saveToRemote"));
	        //如果要上传到远程服务器
	        if(sc.getValue().equals("yes")){
	        	//上传后保存到服务器上的临时文件
		        File storeLocation = ((DiskFileItem) fileItem).getStoreLocation();
		        String srcFile =storeLocation.getAbsolutePath();
		        srcFile = srcFile.substring(0, srcFile.length()-4)+expandedName;
		        //先临时保存在本地
		        fileItem.write(new File(srcFile));
	        	String host="", user="", password="", savePath="/data";
	        	int port=22;
	        	List<SysConfig> sysConfigs = dao.query(SysConfig.class,Cnd.where("type", "=", "file").and("code", "<>", "saveToRemote"));
	        	for (SysConfig sysConfig : sysConfigs) {
	        		String code = sysConfig.getCode();
	        		String value = sysConfig.getValue();
					if("host".equals(code)){
						host = value;
					}else if ("port".equals(code)) {
						port = Integer.valueOf(port);
					}else if ("user".equals(code)) {
						user = value;
					}else if ("password".equals(code)) {
						password = value;
						password = new DES("sstcd_youth1").decrypt(password);
					}else if ("savePath".equals(code)) {
						savePath = value;
					}
				}
	        	url = "http://"+host+":8080";
	        	if(!savePath.endsWith("/"))
	        		savePath += "/";
		        new SFTPFileUpload(host, port, user, password).sshSftp(srcFile, savePath+"uploadImages/"+fileName);
		        //删除临时文件
		        new File(srcFile).delete();
	        }else{//保存到本地服务器
	        	String uploadPath = request.getServletContext().getRealPath("/");    
				//将文件存到与该web项目同级的data文件夹下，以便重新部署时不受影响
	        	if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){//windows系统
	        		uploadPath = new File(uploadPath).getParent()+imgUrl.replaceAll("/", "\\\\")+"uploadImages";
	        	}else {
	        		sc = dao.fetch(SysConfig.class,Cnd.where("type", "=", "file").and("code", "=", "savePath"));
	        		uploadPath = sc.getValue();
	        		if(!uploadPath.endsWith("/"))
	        			uploadPath+="/";
	        		uploadPath += "uploadImages";
				}
		        File file = new File(uploadPath);  
		        if(!file.exists()){  //如果路径不存在，创建  
		            file.mkdirs();  
		        }  
		        File toFile = new File(uploadPath, fileName); 
				fileItem.write(toFile);
	        }
	        
	        // 返回"图像"选项卡并显示图片    
	        url += imgUrl+ "uploadImages/" + fileName;
			result.put("state", "SUCCESS");
			result.put("url", url);
			result.put("title", fileItem.getName());
			result.put("original", fileItem.getName());
			out.println(Json.toJson(result));
		}
	}
%>