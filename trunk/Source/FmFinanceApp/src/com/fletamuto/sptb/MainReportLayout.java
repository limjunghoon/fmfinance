package com.fletamuto.sptb;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainReportLayout extends ListActivity {
	
	private ArrayList<Class<?>> reportList = new ArrayList<Class<?>>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this, R.array.report_list, android.R.layout.simple_list_item_1);
       
        setListAdapter(adapter);
    
        reportList.add(ReportExpenseLayout.class);
        reportList.add(ReportIncomeLayout.class);
        reportList.add(ReportAssetsLayout.class);
        reportList.add(ReportLiabilityLayout.class);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    	if (reportList.size() <= position) return;
    	Intent intent = new Intent(this, reportList.get(position));
		startActivity(intent);
    }

}