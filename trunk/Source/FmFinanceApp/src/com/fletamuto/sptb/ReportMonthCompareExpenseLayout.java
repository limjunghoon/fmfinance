package com.fletamuto.sptb;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;

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
    	setTitle("�� ����");

		super.setTitleBtn();
	}

	@Override
	protected void setAdapterList() {
		setAdapterList(R.layout.report_list_expense);
	}

	@Override
    protected void setListViewText(FinanceItem financeItem, View convertView) {
		ExpenseItem item = (ExpenseItem)financeItem;
		
		((TextView)convertView.findViewById(R.id.TVExpenseReportListDate)).setText("��¥ : " + item.getCreateDateString());			
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("�ݾ� : %,d��", item.getAmount()));
		((TextView)convertView.findViewById(R.id.TVExpenseReportListMemo)).setText("�޸� : " + item.getMemo());
		String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("�з� : " + categoryText);
		((TextView)convertView.findViewById(R.id.TVExpenseReportListTag)).setText("�±� : " + item.getTag().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("���� : " + item.getPaymentMethod().getText());
	}
}