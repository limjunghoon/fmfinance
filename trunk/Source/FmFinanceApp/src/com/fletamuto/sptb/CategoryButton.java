package com.fletamuto.sptb;

import com.fletamuto.sptb.data.Category;

import android.content.Context;
import android.widget.Button;

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
