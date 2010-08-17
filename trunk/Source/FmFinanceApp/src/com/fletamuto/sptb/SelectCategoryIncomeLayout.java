package com.fletamuto.sptb;


import android.os.Bundle;

import com.fletamuto.sptb.data.IncomeItem;

public class SelectCategoryIncomeLayout extends SelectCategoryBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setType(IncomeItem.TYPE);
        getCategoryList();
    }

}
