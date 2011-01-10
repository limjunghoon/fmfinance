package com.fletamuto.sptb;

import com.fletamuto.sptb.db.DBMgr;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * ���� ���̾ƿ� Ŭ����
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
        		.setIndicator("����/����")
        		.setContent(new Intent(this, MainIncomeAndExpenseLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("assets")
        		.setIndicator("�ڻ�")
        		.setContent(new Intent(this, MainAssetsLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("report")
        		.setIndicator("���")
        		.setContent(new Intent(this, MainReportLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("budget")
        		.setIndicator("����")
        		.setContent(new Intent(this, BudgetLayout.class)));
        
        tabHost.addTab(tabHost.newTabSpec("setting")
        		.setIndicator("����")
        		.setContent(new Intent(this, MainSettingLayout.class)));
        
  
    }
    
    

}