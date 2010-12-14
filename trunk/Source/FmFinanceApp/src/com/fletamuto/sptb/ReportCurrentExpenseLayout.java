package com.fletamuto.sptb;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.util.FinanceCurrentDate;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.util.LogTag;

public class ReportCurrentExpenseLayout extends ReportBaseLayout {
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    protected void setTitleBtn() {
    	setTitle("지출");
    	super.setTitleBtn();
    }
    
    @Override
    public void initialize() {
    	TextView tvCurrentDay = (TextView)findViewById(R.id.TVCurrentDay);
    	tvCurrentDay.setTextColor(Color.MAGENTA);
    	
    	super.initialize();
    }
    
    protected void updateChildView() {
    	LinearLayout llMoveDay = (LinearLayout) findViewById(R.id.LLMoveDay);
		llMoveDay.setVisibility(View.VISIBLE);
		
    	TextView tvCurrentDay = (TextView)findViewById(R.id.TVCurrentDay);
    	tvCurrentDay.setText(FinanceDataFormat.getDotDateFormat(FinanceCurrentDate.getTime()));
    	tvCurrentDay.setTextColor(Color.MAGENTA);
    	
    }
    
	@Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	startEditInputActivity(InputExpenseLayout.class, item.getID());
	}
    
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
//    	startEditInputActivity(InputExpenseLayout.class, item.getID());
//    	super.onListItemClick(l, v, position, id);
//    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	ExpenseItem item = (ExpenseItem)financeItem;
    	
    	((TextView)convertView.findViewById(R.id.TVExpenseReportListDate)).setVisibility(View.GONE);
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVExpenseReportListMemo)).setText("메모 : " + item.getMemo());
		String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
		((TextView)convertView.findViewById(R.id.TVExpenseReportListTag)).setText("태그 : " + item.getTag().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + item.getPaymentMethod().getText());
	}
    
    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportExpenseDelete);
		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
		btnDelete.setTag(R.id.delete_position, new Integer(position));
		btnDelete.setOnClickListener(deleteBtnListener);
    }
    

	@Override
	protected int getItemType() {
		// TODO Auto-generated method stub
		return ExpenseItem.TYPE;
	}

	@Override
	protected void getDate() {
		if (getItemsFromDB(getItemType(), FinanceCurrentDate.getDate()) == false) {
			Log.e(LogTag.LAYOUT, "::: Error GET DATE");
        }
		
		mListItems.clear();
		updateListItem();
	}

	@Override
	protected void onClickAddButton() {
    	Intent intent = new Intent(ReportCurrentExpenseLayout.this, InputExpenseLayout.class);
		startActivity(intent);
	}
	
	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_expense;
	}
	
	protected void updateListItem() {
		mListItems = mItems;
	}

}