package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.ItemDef;

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
    	setTitle("월 지출");
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
		Intent intent = new Intent(ReportMonthCompareExpenseLayout.this, ReportMonthCompareExpenseSubLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, getYear());
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, getMonth());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, categoryAmount.getName());
		startActivity(intent);
	}

	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_expense_expand;
	}
}