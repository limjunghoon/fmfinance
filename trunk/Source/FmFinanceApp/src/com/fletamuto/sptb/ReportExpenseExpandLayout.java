package com.fletamuto.sptb;


import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class ReportExpenseExpandLayout extends ReportExpandBaseLayout {
	
	
	protected CardItem mCard = null;
	private int mCardDisplayMode = -1;
    
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        updateChildView();
    }
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    }
    
    @Override
    public void setTitle(CharSequence title) {
    	if (mCard != null) {
    		title = String.format("%s 지출내역", mCard.getCompenyName().getName());
    	}
    	super.setTitle(title);
    }
    
    @Override
    public void initialize() {
    	mCard = (CardItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.CARD_ITEM);
    	mCardDisplayMode = getIntent().getIntExtra(MsgDef.ExtraNames.CARD_BILLING, -1);
    	super.initialize();
    }
    
    @Override
    protected boolean getItems(int itemType) {
    	if (isDisplayMonthOfYear() && mCard != null) {
    		if (mCardDisplayMode == CardItem.BILLING) {
    			Calendar currentDate = Calendar.getInstance();
    			currentDate.set(mYear, mMonth-1, currentDate.get(Calendar.DAY_OF_MONTH));
    			mItems = DBMgr.getCardExpenseItems(mCard.getID(), mCard.getStartBillingPeriod(currentDate), mCard.getEndBillingPeriod(currentDate));
    		}
    		else if (mCardDisplayMode == CardItem.NEXT_BILLING) {
    			Calendar currentDate = Calendar.getInstance();
    			currentDate.set(mYear, mMonth-1, currentDate.get(Calendar.DAY_OF_MONTH));
    			mItems = DBMgr.getCardExpenseItems(mCard.getID(), mCard.getNextStartBillingPeriod(currentDate), mCard.getNextEndBillingPeriod(currentDate));
    		}
    		else {
    			mItems = DBMgr.getCardExpenseItems(mYear, mMonth, mCard.getID());
    		}
			updateReportItem();
			return true;
		}
    	
    	return super.getItems(itemType);
    }    
    
    protected void setListViewText(FinanceItem financeItem, View convertView) {
    	ExpenseItem item = (ExpenseItem)financeItem;
		((TextView)convertView.findViewById(R.id.TVExpenseReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
		String categoryText = String.format("%s - %s", item.getCategory().getName(), item.getSubCategory().getName());
		((TextView)convertView.findViewById(R.id.TVExpenseReportListCategory)).setText("분류 : " + categoryText);
		if (mCard == null) {
			((TextView)convertView.findViewById(R.id.TVExpenseReportListPaymentMethod)).setText("결제 : " + item.getPaymentMethod().getText());
		}
		else {
			convertView.findViewById(R.id.TVExpenseReportListPaymentMethod).setVisibility(View.GONE);
		}
		
	}
    
    protected void setDeleteBtnListener(final View convertView, final int id, final int groupPosition, final int childPosition) {
    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportExpenseDelete);
    	
		btnDelete.setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				if (DBMgr.deleteItem(ExpenseItem.TYPE, id) == 0 ) {
					Log.e(LogTag.LAYOUT, "can't delete Item  ID : " + id);
				}

				updateExpandList();
			}
		});
	}
    
//    @Override
//	protected int deleteItemToDB(int id) {
//		return DBMgr.deleteItem(ExpenseItem.TYPE, id);
//	}

//	@Override
//	protected FinanceItem getItemInstance(int id) {
//		return DBMgr.getItem(ExpenseItem.TYPE, id);
//	}

	@Override
	protected int getChildLayoutResourceID() {
		return R.layout.report_list_expense_expand;
	}

	@Override
	protected int getItemType() {
		return ExpenseItem.TYPE;
	}
}