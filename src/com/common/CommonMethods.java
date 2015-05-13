package com.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;

import com.dao.DaoFactory;
import com.util.StringUtil;

public class CommonMethods {
	/**
	 * 根据条件从数据库中查询数据
	 * @param dateTime
	 * @return
	 */
	public static <T> Map<String, Object> queryData(HttpServletRequest request, Dao dao, T t,Condition cnd) {
	    int pageNum = Integer.valueOf(request.getParameter("pageNum"));
	    int pageSize = Integer.valueOf(request.getParameter("pageSize"));
	    List<T> list = dao.query((Class<T>) t, cnd, dao.createPager(pageNum, pageSize));
	    int count = dao.count((Class<T>) t, cnd);
	    count = count%pageSize==0 ? (count/pageSize) : (count/pageSize +1);
	    Map<String, Object> map = new HashMap<>();
	    map.put("list", list);//key必须为list
	    map.put("count", count);//key必须为count
	    //设置权限
	    setPermissionNoCheck(request, map);
	    return map;
	}

	/**
	 * 设置功能列表的操作权限
	 * @param request
	 * @param map
	 */
	/*public static void setPermission(HttpServletRequest request, Map<String, Object> map) {
		Dao cmsDao = DaoFactory.getDao();
	    HttpSession session = request.getSession();
		String userAccount = (String) session.getAttribute("userAccount");
		List<UserRole> roles = cmsDao.query(UserRole.class, Cnd.where("useraccount", "=", userAccount));
	    String nodeId = request.getParameter("nodeId");
	    map.put("functions", "");
	    if(nodeId!=null && !nodeId.equals("")){
		    Tree node = cmsDao.fetch(Tree.class, Long.valueOf(nodeId));
		    ArrayList editRoles = StringUtil.splitToArrayList(node.getEditRoles(), ";");
		    //超级管理员可编辑
		    editRoles.add("system");
		    boolean flag = false;
			for (UserRole userRole : roles) {
				if (editRoles.contains(userRole.getRole())) {
					flag = true;
					break;
				}
			}
			if(flag){
				String functions = "<div id=\"functionsDiv\" class=\"boxDiv\">"
						+ "<input type=\"button\" value=\"添加\" onclick=\"add();\">"
						+ "<input type=\"button\" value=\"修改\" onclick=\"edit();\">"
						+ "<input type=\"button\" value=\"删除\" onclick=\"del();\"></div>";
				map.put("functions", functions);
			}
	    }else {
			System.out.println("没有传入菜单id，不能判断编辑权限");
			String functions = "<div id=\"functionsDiv\" class=\"boxDiv\">"
					+ "<font color=\"red\" >没有传入菜单id，不能判断编辑权限</font></div>";
			map.put("functions", functions);
		}
	}*/

	/**
	 * 无需设置权限时的功能菜单，也可以在此处进行修改为需要的
	 * @param request
	 * @param map
	 */
	public static void setPermissionNoCheck(HttpServletRequest request, Map<String, Object> map) {
		String functions = "<div id=\"functionsDiv\" class=\"boxDiv\">"
				+ "<input type=\"button\" value=\"添加\" onclick=\"add();\">"
				+ "<input type=\"button\" value=\"修改\" onclick=\"edit();\">"
				+ "<input type=\"button\" value=\"删除\" onclick=\"del();\"></div>";
		map.put("functions", functions);
	}
   
	
	
}
