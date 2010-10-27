package com.fletamuto.sptb;


import android.os.Bundle;

import com.fletamuto.sptb.data.IncomeItem;

public class SelectCategoryIncomeLayout extends SelectCategoryBaseLayout {
	
	public SelectCategoryIncomeLayout() {
		setType(IncomeItem.TYPE);
	}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        
//        getCategoryList();
 //       setCategoryAdaper();
    }

}
