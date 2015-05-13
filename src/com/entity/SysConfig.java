package com.entity;

import java.util.Date;
import java.sql.*; 
import java.io.*; 

import org.nutz.dao.entity.annotation.*;

@Table("SysConfig")
public class SysConfig { 
	@Id
	private Integer configID;
	@Column
	private String type;
	@Column
	private String typeName;
	@Column
	private String code;
	@Column
	private String codeName;
	@Column
	private String value;

	public void setConfigID(Integer configid){
		this.configID = configid;
	}
	public Integer getConfigID(){
		return this.configID;
	}

	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return this.type;
	}

	public void setTypeName(String typename){
		this.typeName = typename;
	}
	public String getTypeName(){
		return this.typeName;
	}

	public void setCode(String code){
		this.code = code;
	}
	public String getCode(){
		return this.code;
	}

	public void setCodeName(String codename){
		this.codeName = codename;
	}
	public String getCodeName(){
		return this.codeName;
	}

	public void setValue(String value){
		this.value = value;
	}
	public String getValue(){
		return this.value;
	}
}
