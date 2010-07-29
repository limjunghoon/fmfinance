package com.fletamuto.sptb;


import android.os.Bundle;

import com.fletamuto.sptb.data.LiabilityItem;

public class SelectCategoryLiabilityLayout extends SelectCategoryBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getCategoryList();
    }

    @Override
	protected void getCategoryList() {
    	getCategoryList(LiabilityItem.TYPE);
    }
}
