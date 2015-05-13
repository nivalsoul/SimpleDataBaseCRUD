package com.servlet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ResourceBundle;

import com.common.DB2JavaBean;

public class T {

	public static void main(String[] args) {
		//fun1();
		//fun2();
		DB2JavaBean d2j = new DB2JavaBean("config.SystemConfig");
		d2j.setTableName("Advice");
		d2j.init();
		System.out.println(d2j.getTableName());
	}

	private static void fun2() {
		String listFields = "[[\"id\",\"主键\",\"\",\"left\"],[\"title\",\"标题\",\"\",\"left\"]];";
		System.out.println(listFields.replaceAll("\\[\\[", "\\[\n\t\\[").replaceAll("\\],\\[", "\\],\n\t\\[").replaceAll("\\]\\]", "\\]\n\\["));
	}

	private static void fun1() {
		try {   
            Class.forName("com.mysql.jdbc.Driver");  
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sstcdcms","wlxu","wlxusstcd201403");   
            /*PreparedStatement ps = conn.prepareStatement("SELECT TABLE_SCHEMA,COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM information_schema.columns a WHERE TABLE_NAME='Story'");   
            ResultSet rs = ps.executeQuery();   
            while (rs.next()) {
				System.out.println(rs.getString(1)+"--"+rs.getString(2)+"--"+rs.getString(3)+"--"+rs.getString(4)); 
    		}*/
            
           DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet columnSet = databaseMetaData.getColumns(null, "%","Dept", "%");
    		if (null != columnSet) {
	    		while (columnSet.next()) {
	    			System.out.println();
					//列名
					String columnName = columnSet.getString("COLUMN_NAME");
					System.out.println("列名称: "+ columnName); 
					 //备注
					String columnComment = columnSet.getString("REMARKS");
					System.out.println("列名称: "+ columnComment);
					//列类型
					int sqlType = columnSet.getInt("DATA_TYPE");
					System.out.println("列名称: "+ sqlType);
					System.out.println("列名称: "+ columnSet.getString("TYPE_NAME"));
	    		}
    		}
               
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
	}

}
