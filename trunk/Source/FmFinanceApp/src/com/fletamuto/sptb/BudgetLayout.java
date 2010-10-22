package com.fletamuto.sptb;

import android.os.Bundle;

/**
 * 카드 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class BudgetLayout extends FmBaseActivity {  	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_budget, true);
        
        setTitle("예산");
    }
}