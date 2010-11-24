package com.fletamuto.sptb;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;

public class ReportExpenseLayout extends ReportBaseLayout {
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        
//        if (getItemsFromDB(ExpenseItem.TYPE) == false) {
//        	return;
//        }
//        
//        setListAdapter(R.layout.report_list_expense);
    }
    
    @Override
	protected void onClickListItem(AdapterView<?> parent, View view,
			int position, long id) {
    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
    	startEditInputActivity(InputExpenseLayout.class, item.getID());
	}
    
//    
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//    	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
//    	startEditInputActivity(InputExpenseLayout.class, item.getID());
//    	super.onListItemClick(l, v, position, id);
//    }
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	ExpenseItem item = (ExpenseItem)financeItem;
		
		((TextView)convertView.findViewById(R.id.TVExpenseReportListDate)).setText("날짜 : " + item.getCreateDateString());			
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
	protected void onClickAddButton() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected int getAdapterResource() {
		return R.layout.report_list_expense;
	}
}