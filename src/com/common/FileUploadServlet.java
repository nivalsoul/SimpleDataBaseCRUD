package com.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import com.util.DES;
import com.util.SFTPFileUpload;
import com.dao.DaoFactory;
import com.entity.SysConfig;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet(description = "文件上传", urlPatterns = { "/fileUploadServlet" })
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
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
		 //fileUpload(request,response);
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			String callback = request.getParameter("CKEditorFuncNum");
			Map<String, List<FileItem>> map = upload.parseParameterMap(request);
			List<FileItem> list = map.get("upload");
			FileItem fileItem = list.get(0);
			//从请求参数中获取上传文件的类型：File/Image/Flash 
	        String expandedName = fileItem.getName().substring(fileItem.getName().lastIndexOf("."));    
	        if (expandedName.equals(".jpg") || expandedName.equals(".png") || expandedName.equals(".gif") || expandedName.equals(".bmp")) {      
	            //expandedName = ".jpg";    
	        }else{    
	            out.println("<script type=\"text/javascript\">");      
	            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",''," + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");     
	            out.println("</script>");       
	        }    
	        if(fileItem.getSize() > 6000*1024){    
	            out.println("<script type=\"text/javascript\">");      
	            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",''," + "'文件大小不得大于6M');");     
	            out.println("</script>");     
	        }    
	        String fileName = java.util.UUID.randomUUID().toString();  //采用时间+UUID的方式随机命名    
	        fileName += expandedName; 
	        String url = request.getRequestURL().toString().split("/sstcdcms")[0];
	        
	        Dao dao = DaoFactory.getDao();
	        List<SysConfig> urlList = dao.query(SysConfig.class,Cnd.where("type", "=", "url").and("code", "=", "imgUrl"));
	        String imgUrl = urlList.get(0).getValue();//图片访问路径，不包括前面的ip部分，形如：/data/youth1/images
	        List<SysConfig> sysConfigs = dao.query(SysConfig.class,Cnd.where("type", "=", "file").and("code", "=", "saveToRemote"));
	        //如果要上传到远程服务器
	        if(sysConfigs.get(0).getValue().equals("yes")){
	        	//上传后保存到服务器上的临时文件
		        File storeLocation = ((DiskFileItem) fileItem).getStoreLocation();
		        String srcFile =storeLocation.getAbsolutePath();
		        srcFile = srcFile.substring(0, srcFile.length()-4)+expandedName;
		        //先临时保存在本地
		        fileItem.write(new File(srcFile));
	        	String host="", user="", password="", savePath="/data";
	        	int port=22;
	        	sysConfigs = dao.query(SysConfig.class,Cnd.where("type", "=", "file")
	        			.and("code", "in", "'host','port','user','password','savePath'"));
	        	for (SysConfig config : sysConfigs) {
	        		String code = config.getCode();
	        		String value = config.getValue();
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
		        new SFTPFileUpload(host, port, user, password).sshSftp(srcFile, savePath+"uploadImages/"+fileName);
		        //删除临时文件
		        new File(srcFile).delete();
	        }else{//保存到本地服务器
	        	String uploadPath = request.getServletContext().getRealPath("/");    
				//将文件存到与该web项目同级的data文件夹下，以便重新部署时不受影响
	        	if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){//windows系统
	        		uploadPath = new File(uploadPath).getParent()+imgUrl.replaceAll("/", "\\\\")+"\\uploadImages";
	        	}else {
	        		uploadPath = new File(uploadPath).getParent()+imgUrl+"/uploadImages";
				}
		        File file = new File(uploadPath);  
		        if(!file.exists()){  //如果路径不存在，创建  
		            file.mkdirs();  
		        }  
		        File toFile = new File(uploadPath, fileName); 
				fileItem.write(toFile);
	        }

			 // 返回"图像"选项卡并显示图片    
	        
	        url += imgUrl;
	        out.println("<script type=\"text/javascript\">");      
	        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" +url+ "/uploadImages/" + fileName + "','')");      
	        out.println("</script>"); 

			out.flush();
		}catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			out.close();
		}
	}

	/** 
     * 图片上传 
	 * @param response 
	 * @param request 
     * @return 
     * @throws IOException  
     */  
    public void fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException{  
    	 File upload;       //文件  
    	 String uploadContentType;   //文件类型  
    	 String uploadFileName;       //文件名 
    	 
        response.setCharacterEncoding("utf-8");  
        PrintWriter out =  response.getWriter();    
        // CKEditor提交的很重要的一个参数    
        String callback = request.getParameter("CKEditorFuncNum");  
        //从请求参数中获取上传文件的类型：File/Image/Flash 
        uploadContentType = request.getParameter("Type");
        String expandedName = "";  //文件扩展名    
        if (uploadContentType.equals("image/pjpeg") || uploadContentType.equals("image/jpeg")) {    
            //IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg    
            expandedName = ".jpg";    
        }else if(uploadContentType.equals("image/png") || uploadContentType.equals("image/x-png")){    
            //IE6上传的png图片的headimageContentType是"image/x-png"    
            expandedName = ".png";    
        }else if(uploadContentType.equals("image/gif")){    
            expandedName = ".gif";    
        }else if(uploadContentType.equals("image/bmp")){    
            expandedName = ".bmp";    
        }else{    
            out.println("<script type=\"text/javascript\">");      
            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",''," + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");     
            out.println("</script>");    
        }    
       /* if(upload.length() > 600*1024){    
            out.println("<script type=\"text/javascript\">");      
            out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",''," + "'文件大小不得大于600k');");     
            out.println("</script>");    
            return null;    
        }  */  
            
       /* InputStream is = new FileInputStream(upload);    
        String uploadPath = request.getServletContext().getRealPath("/img/uploadImgs");    
        String fileName = java.util.UUID.randomUUID().toString();  //采用时间+UUID的方式随即命名    
        fileName += expandedName;    
        File file = new File(uploadPath);  
        if(!file.exists()){  //如果路径不存在，创建  
            file.mkdirs();  
        }  
        File toFile = new File(uploadPath, fileName);    
        OutputStream os = new FileOutputStream(toFile);       
        byte[] buffer = new byte[1024];       
        int length = 0;    
        while ((length = is.read(buffer)) > 0) {       
            os.write(buffer, 0, length);       
        }       
        is.close();    
        os.close();    
            
        // 返回"图像"选项卡并显示图片    
        out.println("<script type=\"text/javascript\">");      
        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + "/eHomePlus/img/uploadImgs" + fileName + "','')");      
        out.println("</script>");    
        return null;  */
    }
}
