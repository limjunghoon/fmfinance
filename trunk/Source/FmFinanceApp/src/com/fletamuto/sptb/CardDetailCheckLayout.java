package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CardDetailCheckLayout extends CardDetailBaseLayout {  	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_detail_check_card, true);
        updateChild();
        
        setBtnClickListener();
    }
	
	public void updateChild() {
		updateCardNameText();
		updateCardNumberText();
		updateCardExpenseAmountBtnText();
		updateCardAccountBalance();
	}



	private void updateCardAccountBalance() {
		((TextView) findViewById(R.id.TVDetailCheckCardAccountBalance)).setText(String.format("%s : ÀÜ¾× %,d¿ø", mCard.getAccount().getCompany().getName(), mCard.getAccount().getBalance()));
	}

	private void updateCardExpenseAmountBtnText() {
		((Button) findViewById(R.id.BtnDetailCheckCardExepnseAmount)).setText(String.format("%,d¿ø", mCardInfo.getTotalExpenseAmount()));
	}


	public void updateCardNumberText() {
		((TextView) findViewById(R.id.TVDetailCheckCardNumber)).setText(mCard.getNumber());
	}

	public void updateCardNameText() {
		((TextView) findViewById(R.id.TVDetailCheckCardName)).setText(mCard.getName());
	}

	@Override
	public void setBtnClickListener() {
		findViewById(R.id.BtnDetailCheckCardExepnseAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(CardDetailCheckLayout.this, ReportExpenseExpandLayout.class);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
				startActivity(intent);
			}
		});
	}
}