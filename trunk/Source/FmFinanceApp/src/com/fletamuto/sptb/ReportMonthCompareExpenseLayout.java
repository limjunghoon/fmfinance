package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.view.FmBaseLayout;

public class ReportMonthCompareExpenseLayout extends ReportBaseMonthCompare {

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
			setTitle("월간 지출");
		}
		else {
			setTitle("년간 지출");
		}
		
		super.setTitleName();
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
		Intent intent = new Intent(ReportMonthCompareExpenseLayout.this, ReportMonthCompareExpenseSubLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, getMonthCalender().get(Calendar.YEAR));
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, getMonthCalender().get(Calendar.MONTH));
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, categoryAmount.getName());
		startActivity(intent);
	}

	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_expense_expand;
	}

	@Override
	protected void onClickTotalAmountBtn() {
		Intent intent = new Intent(this, ReportMonthOfYearCategoryLayout.class);
		intent.putExtra(MsgDef.ExtraNames.ITEM_TYPE, ExpenseItem.TYPE);
		startActivity(intent);
	}
}