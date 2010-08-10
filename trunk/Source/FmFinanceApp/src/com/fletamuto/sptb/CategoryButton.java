package com.fletamuto.sptb;

import android.content.Context;
import android.widget.Button;

import com.fletamuto.sptb.data.Category;

public class CategoryButton extends Button {
	private Category mCategory;

	public CategoryButton(Context context, Category category) {
		super(context);
		this.mCategory = category;
	}
	
	public int getCategoryID() {
		return mCategory.getId();
	}
	
	public String getCategoryName() {
		return mCategory.getName();
	}
}
