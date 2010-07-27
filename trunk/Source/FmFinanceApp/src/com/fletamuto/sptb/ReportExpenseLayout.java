package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportExpenseLayout extends ReportBaseListLayout {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayList<FinanceItem> items = DBMgr.getInstance().getAllItems(ExpenseItem.TYPE);
       /* 
        ArrayList<HashMap<String,String>> expenseItems = DBMgr.getInstance().getExpenseInfo();
	*/
        ArrayAdapter<FinanceItem> adapter2 = new ArrayAdapter<FinanceItem>(this, R.layout.report_list, items);
		this.setListAdapter(adapter2); 
        
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }
}