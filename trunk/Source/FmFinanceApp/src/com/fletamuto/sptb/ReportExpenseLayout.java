package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fletamuto.sptb.db.DBMgr;

public class ReportExpenseLayout extends ReportBaseListLayout {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayList<HashMap<String,String>> expenseItems = DBMgr.getInstance().getExpenseInfo();
	
        SimpleAdapter adapter2 = new SimpleAdapter( 
				this, 
				expenseItems,
				R.layout.report_list,
				new String[] { "_date","_type","_much","_memo" },
				new int[] { R.id.TVReportListDate, R.id.TVReportListCategory, R.id.TVReportListAmount,R.id.TVReportListMemo }  );
		this.setListAdapter(adapter2); 
        
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }
}