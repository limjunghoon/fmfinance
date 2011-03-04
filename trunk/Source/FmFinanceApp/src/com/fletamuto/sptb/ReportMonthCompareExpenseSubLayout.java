package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class ReportMonthCompareExpenseSubLayout extends ReportBaseMonthCompare {
	private int mMainCategoryID = -1;
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	

    }
    
    @Override
    protected void initialize() {
    	setItemType(ExpenseItem.TYPE);        
    	mMainCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.CATEGORY_ID, -1);
    	super.initialize();
    }
    
	protected void getData() {
		if (mMainCategoryID == -1) return;
		
		mFinanceItems = DBMgr.getItemsFromCategoryID(mType, mMainCategoryID, getMonthCalender().get(Calendar.YEAR), getMonthCalender().get(Calendar.MONTH)+1);
		
		int itemSize = mFinanceItems.size();
		for (int index = 0; index < itemSize; index++) {
			mTotalAmout += mFinanceItems.get(index).getAmount();
		}
		updateMapCategory();
		updateReportItem();
	}
	
	public Category getCategory(FinanceItem item) {
		if (item == null) return null;
		return item.getSubCategory();
	}
	
	@Override
	protected void setTitleBtn() {
    	setTitle(getIntent().getStringExtra(MsgDef.ExtraNames.CATEGORY_NAME));

		super.setTitleBtn();
	}



	@Override
    protected void setListViewText(FinanceItem financeItem, View convertView) {
		ExpenseItem item = (ExpenseItem)financeItem;
		
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		
		String categoryText;
		if (item.getCategory().getExtndType() == ItemDef.NOT_CATEGORY) {
			categoryText = String.format("%s", item.getCategory().getName());
		}
		else {
			categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
		}
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
		((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + item.getPaymentMethod().getText());
	}
	
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
		Intent intent = new Intent(ReportMonthCompareExpenseSubLayout.this, ReportExpenseExpandLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, getMonthCalender().get(Calendar.YEAR));
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, getMonthCalender().get(Calendar.MONTH));
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_SUB_ID, categoryAmount.getCategoryID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, categoryAmount.getName());
		startActivity(intent);
	}
	
	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_expense_expand;
	}
}