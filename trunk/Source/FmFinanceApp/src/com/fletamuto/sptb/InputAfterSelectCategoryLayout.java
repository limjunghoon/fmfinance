package com.fletamuto.sptb;

import android.os.Bundle;

import com.fletamuto.sptb.data.Category;

public abstract class InputAfterSelectCategoryLayout extends SelectCategoryBaseLayout implements InputAfterSelected {
	
	protected abstract void startInputActivity(Category category);
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
	
	protected void onClickCategoryButton(Category category) {
		if (isInputmode()) {
			selectedCategory(category);
		}
		else {
			super.onClickCategoryButton(category);
		}
		
	}

	private void selectedCategory(Category category) {
		startInputActivity(category);
    }
	
	// 수정필요
	private boolean isInputmode() {
		return isSelectSubCategory();
	}
	
	
}
