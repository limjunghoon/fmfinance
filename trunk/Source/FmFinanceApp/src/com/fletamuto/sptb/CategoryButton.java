package com.fletamuto.sptb;

import android.content.Context;
import android.widget.Button;

import com.fletamuto.sptb.data.Category;

public class CategoryButton extends Button {
	private Category category;

	public CategoryButton(Context context, Category category) {
		super(context);
		this.category = category;
	}
	
	public int getCategoryID() {
		return category.getId();
	}
	
	public String getCategoryName() {
		return category.getName();
	}
}
