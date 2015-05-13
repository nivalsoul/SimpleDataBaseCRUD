package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.json.Json;

import com.dao.DaoFactory;
import com.entity.SysConfig;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet(description = "文件上传", urlPatterns = { "/admin/uploadFileServlet" })
public class uploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public uploadFileServlet() {
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
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		Dao dao = DaoFactory.getDao();
		
		try {
			Map result = new HashMap();
			Map<String, List<FileItem>> map = upload.parseParameterMap(request);
			List<FileItem> list = map.get("file5");
			FileItem fileItem = list.get(0);
			//从请求参数中获取上传文件的类型：File/Image/Flash 
			String expandedName = fileItem.getName().substring(fileItem.getName().lastIndexOf(".")); 
	        String fileName = java.util.UUID.randomUUID().toString();  //采用时间+UUID的方式随即命名    
	        fileName += expandedName;    
	        //文件的存储及访问路径，如：/data/youth1/images/cover
	        String path = "";
	        SysConfig sysConfig = dao.fetch(SysConfig.class, Cnd.where("type", "=", "url").and("code", "=", "imgUrl"));
	        path = sysConfig.getValue() + "/info/";
	        String uploadPath = request.getServletContext().getRealPath("/"); 
        	if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){//windows系统保存到tomcat的相应项目下
        		uploadPath = new File(uploadPath).getParent()+path.replaceAll("/", "\\\\");
        	}else { //linux系统时直接保存到path路径下
        		uploadPath = path;
			}
	        File file = new File(uploadPath);  
	        if(!file.exists()){  //如果路径不存在，创建  
	            file.mkdirs();  
	        }  
	        File toFile = new File(uploadPath, fileName); 
			fileItem.write(toFile);
	        
			//取得域名，如：www.sstcd.net
			sysConfig = dao.fetch(SysConfig.class, Cnd.where("type", "=", "domain").and("code", "=", "main"));
			//文件访问路径
	        String url = "http://"+sysConfig.getValue();
	        url += path+fileName;
	       
	        result.put("state", "SUCCESS");
			result.put("url", url);
			result.put("title", fileItem.getName());
			result.put("original", fileItem.getName());
			out.println(Json.toJson(result));
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
