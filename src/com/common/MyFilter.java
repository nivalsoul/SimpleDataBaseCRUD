package com.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import com.dao.DaoFactory;
import com.entity.SysConfig;
import com.util.StringUtil;

public class MyFilter implements javax.servlet.Filter
{
	private String filterPages; //需要过滤的页面
    private String filterPaths; //需要过滤的路径开头
    
    public void init(FilterConfig config) throws ServletException
    {
    	//存储web.xml里面配置的filter的init-param的init-value
    	filterPages = config.getInitParameter("filterPages"); 
    	filterPaths = config.getInitParameter("filterPaths"); 
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        request.setCharacterEncoding("utf-8");
        // 使用与HTTP协议相关 API，需要将参数转为子类型
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
       httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setDateHeader("Expires", -1);
        httpServletResponse.setHeader("Pragma", "no-cache");
        
        //通过过虑器进行转码
       /* Map<String, String> paras = new HashMap<String, String>();
        Enumeration names = httpRequest.getParameterNames();
        while (names.hasMoreElements())
        {
            String key = (String)names.nextElement();
            paras.put(key, new String(request.getParameter(key).getBytes("iso8859-1"), "utf-8"));
        }
        chain.doFilter(new ConvertedRequest(httpRequest, paras), response);*/
        
        String uri = httpRequest.getRequestURI();
        String rootPath=httpRequest.getContextPath();
        String path = httpRequest.getServletPath();
        
        HttpSession session = httpRequest.getSession();
    	String isLogin = (String) session.getAttribute("isLogin");
    	Dao dao = DaoFactory.getDao();
    	ArrayList filterPathsList = StringUtil.splitAllToArrayList(filterPaths,";");
    	String[] folder = path.split("/");
    	if(path.equals("/admin/login.html")||path.equals("/admin/error.html")){
    		chain.doFilter(request, response);
    	}else if (filterPathsList.contains(folder[1]) && (path.endsWith(".html")||path.endsWith(".htm"))) {
    		if(isLogin == null || !isLogin.equals("yes")){
				SysConfig sysConfig = dao.fetch(SysConfig.class,Cnd.where("type", "=", "domain").and("code", "=", "sstcd"));
		    	String root = "http://" + sysConfig.getValue();
		    	httpServletResponse.sendRedirect(root + "/admin/error.html?errorCode=needLogin");
    		}else {
    			boolean flag=false;
    			String userAccount = (String) session.getAttribute("userAccount");
            	/*List<UserRole> roles =dao.query(UserRole.class, Cnd.wrap("userAccount='"+userAccount+"'"));
    	    	List<String> roleNames = new ArrayList<>();
    	    	for (UserRole role : roles) {
    				roleNames.add(role.getRole());
    			}
    	    	SysConfig config = dao.fetch(SysConfig.class, Cnd.where("type", "=", "roles").and("code", "=", "adminRoles"));
    	    	String[] adminRoles = config.getValue().split(";");//默认进入系统管理界面的角色
    	    	for (String adminRole : adminRoles) {
    				if(roleNames.contains(adminRole)){
    					flag = true;
    					break;
    				}
        		}*/
    	    	//权限验证
    	    	if( !flag){
    	    		SysConfig sysConfig = dao.fetch(SysConfig.class,Cnd.where("type", "=", "domain").and("code", "=", "sstcd"));
    		    	String root = "http://" + sysConfig.getValue();
    		    	httpServletResponse.sendRedirect(root + "/admin/error.html?errorCode=needRight");
    	    	}else{
                    chain.doFilter(request, response);
    	    	}
			}
		}else {
        	chain.doFilter(request, response);
		}
    }

    public void destroy()
    {
    }

    private class ConvertedRequest extends HttpServletRequestWrapper
    {
        private Map<String, String> paras;

        public ConvertedRequest(HttpServletRequest request, Map<String, String> paras)
        {
            super(request);
            this.paras = paras;
        }

        @Override
        public String getParameter(String key)
        {
            return this.paras.get(key);
        }
    }
}
