package com.fletamuto.sptb;

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
    	setTitle("월 수입");

		super.setTitleBtn();
	}
	
	@Override
	protected void setAdapterList() {
		setAdapterList(R.layout.report_list_income);
	}
	
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	IncomeItem item = (IncomeItem)financeItem;
    	((TextView)convertView.findViewById(R.id.TVIncomeReportListDate)).setText("날짜 : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVIncomeReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVIncomeReportListMemo)).setText("메모 : " + item.getMemo());
		((TextView)convertView.findViewById(R.id.TVIncomeReportListCategory)).setText("분류 : " + item.getCategory().getName());
	}
}