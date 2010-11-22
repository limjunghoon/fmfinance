package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
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
		
		mFinanceItems = DBMgr.getItemsFromCategoryID(mType, mMainCategoryID, mYear, mMonth);
		
		int itemSize = mFinanceItems.size();
		for (int index = 0; index < itemSize; index++) {
			mTotalAmout += mFinanceItems.get(index).getAmount();
		}
//		mTotalAmout = DBMgr.getTotalAmountMonth(mType, mYear, mMonth);
		updateMapCategory();
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
		
		((TextView)convertView.findViewById(R.id.TVExpenseReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVExpenseReportListMemo)).setText("메모 : " + item.getMemo());
		String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
		((TextView)convertView.findViewById(R.id.TVExpenseReportListTag)).setText("태그 : " + item.getTag().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + item.getPaymentMethod().getText());
	}
	
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
		Intent intent = new Intent(ReportMonthCompareExpenseSubLayout.this, ReportExpenseLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, getYear());
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, getMonth());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
		startActivity(intent);
	}
	
	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_expense_expand;
	}
}