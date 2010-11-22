package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;

public class ReportMonthCompareIncomeLayout extends ReportBaseMonthCompare {
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    }
    @Override
    protected void initialize() {
    	setItemType(IncomeItem.TYPE);
    	super.initialize();
    }
    
	@Override
	protected void setTitleBtn() {
    	setTitle("�� ����");

		super.setTitleBtn();
	}
	

	
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	IncomeItem item = (IncomeItem)financeItem;
    	((TextView)convertView.findViewById(R.id.TVIncomeReportListDate)).setText("��¥ : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("�޸� : " + item.getMemo());
		((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("�з� : " + item.getCategory().getName());
	}
    
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
		Intent intent = new Intent(ReportMonthCompareIncomeLayout.this, ReportIncomeLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, getYear());
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, getMonth());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
		startActivity(intent);
	}
	
	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_income;
	}
}