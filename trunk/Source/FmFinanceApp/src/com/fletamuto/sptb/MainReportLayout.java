package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import com.fletamuto.sptb.ReportAccountLayout.AccountItemAdapter;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.db.DBMgr;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MainReportLayout extends FmBaseActivity {
	
	private ArrayList<ReportActivity> mReportActivityList = new ArrayList<ReportActivity>();
	private ArrayList<String> mReportList = new ArrayList<String>();
	private ArrayAdapter<String> mReportAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_base, true);
        
        setAdapterList();
      
//        ArrayAdapter<CharSequence> adapter;
//        adapter = ArrayAdapter.createFromResource(this, R.array.report_list, android.R.layout.simple_list_item_1);
//       
//        setListAdapter(adapter);
//    
////        mReportList.add(ReportExpenseLayout.class);
////        mReportList.add(ReportIncomeLayout.class);
////        mReportList.add(ReportAssetsLayout.class);
////        mReportList.add(ReportLiabilityLayout.class);
        mReportActivityList.add(new ReportActivity(ReportMonthOfYearLayout.class));
        mReportActivityList.add(new ReportActivity(ReportMonthCompareExpenseToIncomeLayout.class));
        mReportActivityList.add(new ReportActivity(ReportCategoryCompareLayout.class));
        mReportActivityList.add(new ReportActivity(SelectTagLayout.class, MsgDef.ActRequest.ACT_TAG_SELECTED));
        
    }
    
    @Override
    	protected void setTitleBtn() {
    		setTitle("¸ñ·Ï");
    		super.setTitleBtn();
    	}
    
    protected void setAdapterList() {

    	String []reportList = getResources().getStringArray(R.array.report_list);
    	for (int index = 0; index < reportList.length; index++) {
    		mReportList.add(reportList[index]);
    	}
        
    	final ListView listReport = (ListView)findViewById(R.id.LVReportList);
    	mReportAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mReportList);
    	listReport.setAdapter(mReportAdapter);
    	
    	listReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mReportActivityList.size() <= position) return;
		    	Intent intent = new Intent(MainReportLayout.this, mReportActivityList.get(position).getClassName());
		    	
		    	if (mReportActivityList.get(position).getRequestCode() != -1) {
		    		startActivityForResult(intent, mReportActivityList.get(position).getRequestCode());
		    	}
		    	else {
		    		startActivity(intent);
		    	}
				
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == MsgDef.ActRequest.ACT_TAG_SELECTED) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(MainReportLayout.this, ReportTagExpenseLayout.class);
				intent.putExtra(MsgDef.ExtraNames.TAG_ID, data.getIntExtra(MsgDef.ExtraNames.TAG_ID, -1));
				intent.putExtra(MsgDef.ExtraNames.TAG_NAME, data.getStringExtra(MsgDef.ExtraNames.TAG_NAME));
				startActivity(intent);
			}
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    public class ReportActivity {
    	
    	private Class<?> mClassName;
    	private int mRequestCode = -1;
    	
    	public ReportActivity(Class<?> className) {
			mClassName = className;
		}
    	
    	public ReportActivity(Class<?> className, int requestCode) {
			mClassName = className;
			mRequestCode = requestCode;
		}
    	
    	Class<?> getClassName() {
    		return mClassName;
    	}
    	
    	int getRequestCode() {
    		return mRequestCode;
    	}
    	
    }
    
//    
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//    	
//    	if (mReportList.size() <= position) return;
//    	Intent intent = new Intent(this, mReportList.get(position));
//		startActivity(intent);
//    }

}