package com.util;

import java.lang.reflect.Field;  
import java.math.BigDecimal;  
import java.sql.Timestamp;
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  

import javax.servlet.http.HttpServletRequest; 

import org.apache.log4j.Logger;

public class BeanUtil {
	private static final Logger log = Logger.getLogger(BeanUtil.class);
	
	/** 
     * 从request中获取值填充bean对象 
     * 
     * @param objClass bean的类 
     * @param request 请求对象 
     * @return object对象 
     */  
    public static Object fillBean(Class objClass, HttpServletRequest request) {  
        Object objInstance = null;  
        try {  
            objInstance = objClass.newInstance();  
        } catch (InstantiationException e1) {  
            log.error(e1.getMessage(), e1);   
        } catch (IllegalAccessException e1) {  
            log.error(e1.getMessage(), e1);   
        }  
  
        Field field;  
        String fieldName;  
        String fieldValue = "";  
        int len;  
        len = objClass.getDeclaredFields().length;  
        for (int i = 0; i < len; i++) {  
            field = objClass.getDeclaredFields()[i];  
            fieldName = field.getName();  
  
            try {  
                fieldValue = request.getParameter(fieldName);  
            } catch (Exception e1) {  
                log.error(e1.getMessage(), e1);   
            }  
  
            if (fieldValue != null) {  
                try {  
                    setFieldValue(field, objInstance, fieldValue);  
                } catch (IllegalAccessException e) {  
                    log.error(e.getMessage(), e);     
                }  
            }  
        }  
        objClass = objClass.getSuperclass();  
        return objInstance;  
    }  
  
    /** 
     * 将数据赋值给指定对象的相应属性 
     * 
     * @param field 字段 
     * @param objInstance 指定对象 
     * @param value 数据 
     * @throws IllegalAccessException 
     */  
    private static void setFieldValue(Field field, Object objInstance, String value) throws IllegalAccessException {  
        String fieldType = field.getType().getName();// 取字段的数据类型  
        field.setAccessible(true);  
        try {  
            if (fieldType.equals("java.lang.String")) {  
                field.set(objInstance, value);  
            } else if (fieldType.equals("java.lang.Integer") || fieldType.equals("int")) {  
                field.set(objInstance, Integer.valueOf(value));  
            } else if (fieldType.equals("java.lang.Long") || fieldType.equals("long")) {  
                field.set(objInstance, Long.valueOf(value));  
            } else if (fieldType.equals("java.lang.Float") || fieldType.equals("float")) {  
                field.set(objInstance, Float.valueOf(value));  
            } else if (fieldType.equals("java.lang.Double") || fieldType.equals("double")) {  
                field.set(objInstance, Double.valueOf(value));  
            } else if (fieldType.equals("java.math.BigDecimal")) {  
                field.set(objInstance, new BigDecimal(value));  
            } else if (fieldType.equals("java.util.Date")) {  
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
                formatter.setLenient(false);  
                field.set(objInstance, formatter.parse(value));  
            } else if (fieldType.equals("java.sql.Date")) {  
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
                formatter.setLenient(false);  
                Date date = formatter.parse(value);  
                field.set(objInstance, new java.sql.Date(date.getTime()));  
            } else if (fieldType.equals("java.sql.Timestamp")) {  
            	Timestamp date = DateUtil.getTimestamp(value);
            	field.set(objInstance, date);  
            } else if (fieldType.equals("java.lang.Boolean") || fieldType.equals("boolean")) {  
                field.set(objInstance, Boolean.valueOf(value));  
            } else if (fieldType.equals("java.lang.Byte") || fieldType.equals("byte")) {  
                field.set(objInstance, Byte.valueOf(value));  
            } else if (fieldType.equals("java.lang.Short") || fieldType.equals("short")) {  
                field.set(objInstance, Short.valueOf(value));  
            }  
        } catch (NumberFormatException ex) {  
            field.set(objInstance, null); //当使用简单数据类型会抛出异常  
            log.error(ex.getMessage(), ex);   
        } catch (ParseException e) {  
            log.error(e.getMessage(), e);     
            field.set(objInstance, null);  
        } catch (Exception e) {  
            log.error(e.getMessage(), e);     
            field.set(objInstance, null);  
        }  
    }  
    
    /** 
     * 复制orig属性值赋值给dest，用于修改数据库记录时复制对象
     * <p> 
     * 满足如下条件时进行复制处理： 
     * (1) dest与orig的属性名称相同； 
     * (2) dest与orig的属性类型相同； 
     * (3) dest的属性类型不为Class； 
     * (4) orig字段的值不为null 
     * </p> 
     * 
     * @param dest 目标对象 
     * @param orig 源对象 
     * @return 如果dest或者orig为null则返回null/如果发生异常返回null/否则返回复制填充后的dest 
     */  
    public static Object copyProperties(Object dest, Object orig) {  
        if (dest == null || orig == null) {  
            return dest;  
        }  
        Field[] origFields = orig.getClass().getDeclaredFields();
        Field[] destFields = dest.getClass().getDeclaredFields();
        int len = origFields.length;  
        try {  
            for (int i = 0; i < len; i++) {  
            	origFields[i].setAccessible(true);
            	Class origType = origFields[i].getType();
                for(int j = 0; j < destFields.length; j++){
                	destFields[j].setAccessible(true);
                	Class destType = destFields[j].getType();
	                if (destType != null && destType.equals(origType) && destFields[j].getName().equals(origFields[i].getName())
	                		&& origFields[i].get(orig)!=null  && !destType.equals(Class.class)) {  
	                   destFields[j].set(dest, origFields[i].get(orig));
	                }  
                }
            }  
  
            return dest;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return null;  
        }  
    }  
}
