package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;

import com.fletamuto.sptb.data.Category;

public abstract class InputAfterSelectCategoryLayout extends SelectCategoryBaseLayout implements InputAfterSelected {
	
	protected abstract void startInputActivity(Category category);
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
	
	protected void onClickCategoryButton(Category category) {
		selectedCategory(category);
	}

	private void selectedCategory(Category category) {
		startInputActivity(category);
    }
	
	
}
