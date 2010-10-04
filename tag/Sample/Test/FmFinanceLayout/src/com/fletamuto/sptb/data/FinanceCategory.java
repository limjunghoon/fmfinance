package com.fletamuto.sptb.data;

import java.util.ArrayList;

public class FinanceCategory {
	
	private Category mainCategory;
	private ArrayList<Category> subCategory;
	
	public FinanceCategory(int id, String name){
		mainCategory.setId(id);
		mainCategory.setName(name);
	}
	
	public FinanceCategory(int id, String name, ArrayList<Category> subCategory){
		mainCategory.setId(id);
		mainCategory.setName(name);
		this.subCategory = subCategory;
	}

	public void addSubCategory(int id, String name) {
		subCategory.add(new Category(id, name));
	}
	
	public void addSubCategory(Category subCategory) {
		this.subCategory.add(subCategory);
	}
	
	public int getID() {
		return mainCategory.getId();
	}
	
	public String getName() {
		return mainCategory.getName();
	}
	
	public void setName(String name) {
		mainCategory.setName(name);
	}
	
	public Category getCategory() {
		return mainCategory;
	}
	
	public ArrayList<Category> getSubCategory() {
		return subCategory;
	}
}
