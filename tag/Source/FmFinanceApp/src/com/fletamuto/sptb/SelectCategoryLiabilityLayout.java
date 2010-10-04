package com.fletamuto.sptb;


import android.os.Bundle;

import com.fletamuto.sptb.data.LiabilityItem;

public class SelectCategoryLiabilityLayout extends SelectCategoryBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setType(LiabilityItem.TYPE);
        getCategoryList();
        setCategoryAdaper();
    }

}
