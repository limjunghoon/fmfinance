package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.view.FmBaseLayout;

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
    	setTitle("���� ����");
    	
    	setTitleBtnText(FmBaseLayout.BTN_LEFT_01, "����");
		setTitleBtnVisibility(FmBaseLayout.BTN_LEFT_01, View.VISIBLE);
		
		super.setTitleBtn();
	}
	
	@Override
	protected void updateChildView() {
		if (mViewMode == VIEW_MONTH) {
			setTitle("���� ����");
		}
		else {
			setTitle("�Ⱓ ����");
		}
		super.updateChildView();
	}
	
	
	@Override
    protected void onClickLeft1TitleBtn() {
    	Intent intent = new Intent(this, ReportMonthCompareExpenseLayout.class);
		startActivity(intent);
		
    	finish();
    }
	
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	IncomeItem item = (IncomeItem)financeItem;
		((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("�޸� : " + item.getMemo());
		((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("�з� : " + item.getCategory().getName());
	}
    
	protected void onClickCategoryButton(CategoryAmount categoryAmount) {
		Intent intent = new Intent(ReportMonthCompareIncomeLayout.this, ReportIncomeExpandLayout.class);
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, getMonthCalender().get(Calendar.YEAR));
		intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, getMonthCalender().get(Calendar.MONTH));
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, categoryAmount.getCategoryID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, categoryAmount.getName());
		startActivity(intent);
	}
	
	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_income_expand;
	}
}