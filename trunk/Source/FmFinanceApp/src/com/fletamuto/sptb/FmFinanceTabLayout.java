package com.fletamuto.sptb;

import com.fletamuto.sptb.db.DBMgr;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 메인 레이아웃 클레스
 * @author yongbban
 * @version  1.0.0.1
 */
public class FmFinanceTabLayout extends TabActivity {  	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        DBMgr.initialize(getApplicationContext());
        DBMgr.addRepeatItems();

        final TabHost tabHost = getTabHost();
        
        
        tabHost.addTab(tabHost.newTabSpec("income")
        		.setIndicator("수입/지출")
        		.setContent(new Intent(this, MainIncomeAndExpenseLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("assets")
        		.setIndicator("자산")
        		.setContent(new Intent(this, MainAssetsLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("report")
        		.setIndicator("통계")
        		.setContent(new Intent(this, MainReportLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("budget")
        		.setIndicator("예산")
        		.setContent(new Intent(this, BudgetLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("setting")
        		.setIndicator("설정")
        		.setContent(new Intent(this, MainSettingLayout.class)));
        
  
    }
    
    

}