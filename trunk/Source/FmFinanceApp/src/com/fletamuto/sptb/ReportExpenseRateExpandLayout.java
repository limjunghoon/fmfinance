package com.fletamuto.sptb;


import java.util.ArrayList;

import android.os.Bundle;

import com.fletamuto.sptb.data.FinanceItem;

public class ReportExpenseRateExpandLayout extends ReportExpenseExpandLayout {
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @SuppressWarnings("unchecked")
	public void initialize() {
    	super.initialize();
    	mItems = (ArrayList<FinanceItem>) getIntent().getSerializableExtra(MsgDef.ExtraNames.GET_EXPENSE_ITEMS);
    }
    
    public void updateExpandList() {
    	updateReportItem();
        setExpandListAdapter();
	}
}