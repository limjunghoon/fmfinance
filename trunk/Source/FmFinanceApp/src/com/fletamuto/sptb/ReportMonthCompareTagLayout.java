package com.fletamuto.sptb;

import java.util.Calendar;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;
import com.fletamuto.sptb.view.FmBaseLayout;

public class ReportMonthCompareTagLayout extends ReportBaseMonthCompare {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    }
    
    @Override
    protected void initialize() {
    	setItemType(ExpenseItem.TYPE);
    	super.initialize();
    }
    
	@Override
	protected void setTitleBtn() {
    	
    	setTitleBtnText(FmBaseLayout.BTN_LEFT_01, "수입");
		setTitleBtnVisibility(FmBaseLayout.BTN_LEFT_01, View.VISIBLE);
		
		super.setTitleBtn();
	}
	
	@Override
	public void setTitleName() {
		if (mViewMode == VIEW_MONTH) {
			setTitle("월간 태그");
		}
		else {
			setTitle("년간 태그");
		}
		
		super.setTitleName();
	}
	
	@Override
	protected void getData() {
		
		mTotalAmout = 0L;
		
		if (mViewMode == VIEW_MONTH) {
    		mFinanceItems = DBMgr.getItems(mType, mMonthCalendar.get(Calendar.YEAR), mMonthCalendar.get(Calendar.MONTH)+1);
    	}
    	else {
    		mFinanceItems = DBMgr.getItems(mType, mYear);
    	}
		
		int size = mFinanceItems.size();
		for (int index = 0; index < size; index++) {
			ExpenseItem expense = (ExpenseItem) mFinanceItems.get(index);
			if (expense.getTag().getID() == -1 || expense.getTag().getID() == 1) {
				mFinanceItems.remove(index);
				index--; size--;
			}
			else {
				mTotalAmout += expense.getAmount();
			}
		}
		
		updateMapCategory();
		updateReportItem();
	}
	
	@Override
	public void updateMapCategory() {
		mCategoryAmount.clear();
		
		int itemSize = mFinanceItems.size();
		
		for (int index = 0; index < itemSize; index++) {
			ExpenseItem expense = (ExpenseItem) mFinanceItems.get(index);
			ExpenseTag tag = expense.getTag();
			if (tag == null) {
				Log.w(LogTag.LAYOUT, ":: INVAILD TAG :: ");
				continue;
			}
			Integer tagID = tag.getID();
			
			CategoryAmount categoryAmount = mCategoryAmount.get(tagID);
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(expense.getType());
				categoryAmount.set(tagID, tag.getName(), expense.getAmount());
				mCategoryAmount.put(tagID, categoryAmount);
			}
			else {
				categoryAmount.addAmount(expense.getAmount());
			}
		}
	}
	
	@Override
	protected void updateChildView() {
		setTitleName();
		super.updateChildView();
	}
	
	@Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(this, ReportMonthCompareIncomeLayout.class);
		startActivity(intent);
		
    	finish();
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
		Intent intent = new Intent(this, ReportTagExpenseLayout.class);
		
		intent.putExtra(MsgDef.ExtraNames.VIEW_MODE, mViewMode);
		if (mViewMode == VIEW_MONTH) {
			intent.putExtra(MsgDef.ExtraNames.CALENDAR, getMonthCalender());
		}
		else {
			intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
		}
		
		intent.putExtra(MsgDef.ExtraNames.TAG_ID, categoryAmount.getCategoryID());
		intent.putExtra(MsgDef.ExtraNames.TAG_NAME, categoryAmount.getName());
		
		startActivity(intent);
		
//		Intent intent = new Intent(ReportMonthCompareTagLayout.this, ReportMonthCompareExpenseSubLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.VIEW_MODE, mViewMode);
//		if (mViewMode == VIEW_MONTH) {
//			intent.putExtra(MsgDef.ExtraNames.CALENDAR, getMonthCalender());
//		}
//		else {
//			intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
//		}
//		
//		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
//		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, categoryAmount.getName());
//		startActivity(intent);
	}

	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_expense_expand;
	}

	@Override
	protected void onClickTotalAmountBtn() {
//		Intent intent = new Intent(this, ReportMonthOfYearCategoryLayout.class);
//		intent.putExtra(MsgDef.ExtraNames.VIEW_MODE, mViewMode);
//		if (mViewMode == VIEW_MONTH) {
//			intent.putExtra(MsgDef.ExtraNames.CALENDAR, getMonthCalender());
//		}
//		else {
//			intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
//		}
//		intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, ExpenseItem.TYPE);
//		startActivity(intent);
	}
}