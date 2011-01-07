package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ReportItemAdapter;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceCurrentDate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainAssetsLayout extends FmBaseActivity {
	protected ArrayList<FinanceItem> mAssetsItems = null;
	protected ArrayList<FinanceItem> mLiabilityItems = null;
	protected ArrayList<FinanceItem> mListItems = new ArrayList<FinanceItem>();
	protected ReportItemAdapter mItemAdapter = null;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main_asserts);
    	setButtonClickListener();
    	
    	getListItem();
    	setAdapterList();
    }
    
    protected void setButtonClickListener() {
		
		Button btnExpense = (Button)findViewById(R.id.BtnAssets);
		btnExpense.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainAssetsLayout.this, ReportAssetsLayout.class);
				startActivity(intent);
			}
		});
		
		Button btnIncome = (Button)findViewById(R.id.BtnLiability);
		btnIncome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainAssetsLayout.this, ReportLiabilityLayout.class);
				startActivity(intent);
			}
		});
	}
    
    protected void getListItem() {
    	mAssetsItems = DBMgr.getItems(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	mLiabilityItems = DBMgr.getItems(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	
    	updateListItem();
    }
    
    protected void setAdapterList() {
    	if (mListItems == null) return;
        
    	final ListView listItem = (ListView)findViewById(R.id.LVIncomeExpense);
    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_assets, mListItems);
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				onClickListItem(parent, view, position, id);
			}
		});
    }
    
    public class ReportItemAdapter extends ArrayAdapter<FinanceItem> {
    	private LayoutInflater mInflater;

		public ReportItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
//			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinanceItem item = (FinanceItem)getItem(position);
			
			if (item.isSeparator()) {
				return createSeparator(mInflater, parent, item);
			}
			else {
//				convertView = mInflater.inflate(getAdapterResource(item.getType()), parent, false);
			}
			
//			setListViewText(item, convertView);
//			setDeleteBtnListener(convertView, item, position);
//			
			return convertView;
		}
		
		public View createSeparator(LayoutInflater inflater, ViewGroup parent, FinanceItem item) {
			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
			tvTitle.setText(item.getSeparatorTitle());
			tvTitle.setTextColor(Color.BLACK);
			convertView.setBackgroundColor(Color.WHITE);
			return convertView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mListItems.get(position).isSeparator();
		}
    }
    
    protected void updateListItem() {
//    	mListItems.clear();
//    	
//    	if (mAssetsItems.size() > 0) {
//    		AssetsItem separator = new AssetsItem();
//    		separator.setSeparatorTitle("수입");
//    		mListItems.add(separator);
//        	mListItems.addAll(mAssetsItems);
//    	}
//    	if (mLiabilityItems.size() > 0) {
//    		ExpenseItem separator = new ExpenseItem();
//    		separator.setSeparatorTitle("지출");
//    		mListItems.add(separator);
//    		mListItems.addAll(mLiabilityItems);
//    	}
	}
}