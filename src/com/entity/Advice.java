package com.entity;

import java.util.Date;
import java.sql.*; 
import java.io.*; 

import org.nutz.dao.entity.annotation.*;

@Table("Advice")
public class Advice { 
	@Id
	private Integer id;
	@Column
	private String title;
	@Column
	private String description;
	@Column
	private String contact;
	@Column
	private Timestamp subTime;
	@Column
	private String answer;
	@Column
	private String category;
	@Column
	private Timestamp dealTime;
	@Column
	private Integer hits;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return this.id;
	}

	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}

	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}

	public void setContact(String contact){
		this.contact = contact;
	}
	public String getContact(){
		return this.contact;
	}

	public void setSubTime(Timestamp subtime){
		this.subTime = subtime;
	}
	public Timestamp getSubTime(){
		return this.subTime;
	}

	public void setAnswer(String answer){
		this.answer = answer;
	}
	public String getAnswer(){
		return this.answer;
	}

	public void setCategory(String category){
		this.category = category;
	}
	public String getCategory(){
		return this.category;
	}

	public void setDealTime(Timestamp dealtime){
		this.dealTime = dealtime;
	}
	public Timestamp getDealTime(){
		return this.dealTime;
	}

	public void setHits(Integer hits){
		this.hits = hits;
	}
	public Integer getHits(){
		return this.hits;
	}
}
