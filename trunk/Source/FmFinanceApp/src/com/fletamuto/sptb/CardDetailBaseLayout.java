package com.fletamuto.sptb;

import java.util.Calendar;

import android.os.Bundle;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.db.DBMgr;


public abstract class CardDetailBaseLayout extends FmBaseActivity {  	
	protected CardItem mCard;
	protected long mTotalExpenseAmount = 0L;
	protected int mMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
	protected int mYear = Calendar.getInstance().get(Calendar.YEAR);
	
	public abstract void updateChild();
	public abstract void setBtnClickListener();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
	
	

	@Override
	protected void initialize() {
		mMonth = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1);
		mYear = getIntent().getIntExtra(MsgDef.ExtraNames.CALENDAR_YEAR, Calendar.getInstance().get(Calendar.YEAR));
		mCard = (CardItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.CARD_ITEM);
		mTotalExpenseAmount = DBMgr.getCardTotalExpense(mYear, mMonth, mCard.getID());
		super.initialize();
	}
	
	@Override
	protected void setTitleBtn() {
		setTitle(mCard.getCompenyName().getName());
		super.setTitleBtn();
	}
}