package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.json.Json;

import com.entity.Advice;
import com.dao.DaoFactory;
import com.common.CommonMethods;
import com.util.BeanUtil;

/**
 * <b>注意：下面的urlPatterns为Servlet访问路径，如果不恰当需要自行修改</b>
 */
@WebServlet(description = "Advice管理", urlPatterns = { "/advice/adviceServlet" })
public class AdviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdviceServlet() {
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
		request.setCharacterEncoding("utf-8");
		
		response.setContentType("text/html;charset=utf-8"); 
	    response.setCharacterEncoding("utf-8"); 
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter out = response.getWriter(); 
	    String opt = request.getParameter("opt");
	    
	    Dao dao = DaoFactory.getDao();
	    
	    if("search".equals(opt)){
	    	String searchType = request.getParameter("searchType");
		    String keywords = request.getParameter("keywords");
		    //需要根据具体的表修改查询条件;
		    Condition cnd = null;
		    //注意，如果需要对查询条件做其他处理，请修改！;
		    if(keywords==null || keywords.equals("")){
		    	cnd = Cnd.orderBy().asc("id");
		    }else{
		    	cnd = Cnd.where(searchType, "like", "%"+keywords+"%").asc("id");
		    }
		    //调用公共方法进行查询，返回的map中包含了记录项list和总页数count
		    Map map = CommonMethods.queryData(request, dao, Advice.class, cnd);
		    out.print(Json.toJson(map));
	    }else if ("delete".equals(opt)) {
			String Ids = request.getParameter("Ids");
			dao.clear(Advice.class, Cnd.where("id", "in", Ids));
			out.print("ok");
		}else if ("getInfo".equals(opt)) {
			String id = request.getParameter("id");
			Map<String, Object> map = new HashMap<>();
			Advice info = null;
			if(id!=null && !id.equals("")){
				info = dao.fetch(Advice.class, Long.valueOf(id));
			    map.put("info", info);
			}
			out.print(Json.toJson(map));
		}else if ("save".equals(opt)) {
			Advice formData = (Advice) BeanUtil.fillBean(Advice.class, request);
			//如果生成的有误，则需要修改这里的id
			Integer id = formData.getId();
			Advice data = new Advice();
			if(id==null || id.equals("")){
				dao.insert(formData);
				data = formData;
			}else {
				data = dao.fetch(Advice.class, id.longValue());
				BeanUtil.copyProperties(data, formData);
				dao.update(data);
			}
			id = data.getId();
			
			out.print("<script type=\"text/javascript\">");
			out.print("alert('保存成功！');window.location='advice/list.html'");
			out.print("</script>");
		}

	    out.flush();  
	    out.close();  
	}
	

}
