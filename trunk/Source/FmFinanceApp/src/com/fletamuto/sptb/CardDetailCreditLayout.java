package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CardDetailCreditLayout extends CardDetailBaseLayout {  	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_detail_credit_card, true);
        updateChild();
        
        setBtnClickListener();
    }
	
	public void updateChild() {
		updateCardNameText();
		updateCardSettlementDayText();
		updateCardNumberText();
		updateCardBalanceText();
		updateCardExpenseAmountBtnText();
	}

	private void updateCardExpenseAmountBtnText() {
		((Button) findViewById(R.id.BtnDetailCreditCardExepnseAmount)).setText(String.format("%,d원", mTotalExpenseAmount));
	}

	private void updateCardBalanceText() {
		String amount = "지정되지 않았습니다.";
		
		if (mCard.getBalance() != 0L) {
			amount = String.valueOf(mCard.getBalance());
		}
		((TextView) findViewById(R.id.TVDetailCreditCardBalanceAmount)).setText(amount);
	}

	public void updateCardNumberText() {
		((TextView) findViewById(R.id.TVDetailCreditCardNumber)).setText(mCard.getNumber());
	}

	public void updateCardSettlementDayText() {
		((TextView) findViewById(R.id.TVDetailCreditCardSettlementDay)).setText(String.format("결제일 %d일", mCard.getSettlementDay()));
	}

	public void updateCardNameText() {
		((TextView) findViewById(R.id.TVDetailCreditCardName)).setText(mCard.getName());
	}

	@Override
	public void setBtnClickListener() {
		findViewById(R.id.BtnDetailCreditCardExepnseAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(CardDetailCreditLayout.this, ReportExpenseExpandLayout.class);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
				startActivity(intent);
			}
		});
	}
}