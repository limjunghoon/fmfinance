package com.fletamuto.sptb.data;

public class Category {
	private String name;
	private int id;
	
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	
}
