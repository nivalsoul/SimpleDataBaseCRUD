package com.dao;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Dao工厂，用于获取操作数据库的Dao，适用于Nutz框架
 * @author wlxu
 *
 */
public class DaoFactory {
	private static DruidDataSource dds=null;
	private static Dao dao = null;
	
	public static Dao getDao() {
		if (dao != null) {
			return dao;
		} else if (dds != null) {
			dao = new NutDao(dds);
			return dao;
		} else {
			ResourceBundle rb = ResourceBundle.getBundle("config.DBConfig");
			dds = new DruidDataSource();
			dds.setDriverClassName(rb.getString("driver"));
			dds.setUrl(rb.getString("url"));
			dds.setUsername(rb.getString("userName"));
			dds.setPassword(rb.getString("password"));
			try {
				dds.setFilters("stat");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dao = new NutDao(dds);
			return dao;
		}
	}
	
	/**
	 * 重置dao
	 */
	public static void reset() {
		dao=null;
	}
	
	/**
	 * /关闭池内所有连接
	 */
	public static void closeAllConnection() {
		dds.close(); 
	}
	
	private DaoFactory() {
		// can not use this constructor
	}
}
