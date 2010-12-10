package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CardDetailPrepaidLayout extends CardDetailBaseLayout {  	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_detail_prepaid_card, true);
        updateChild();
        
        setBtnClickListener();
    }
	
	public void updateChild() {
		updateCardNameText();
		updateCardSettlementDayText();
		updateCardNumberText();
		updateCardExpenseAmountBtnText();
		updateCardBalanceText();
		updateCardRemainAmountText();
	}

	private void updateCardExpenseAmountBtnText() {
		((Button) findViewById(R.id.BtnDetailPrepaidCardExepnseAmount)).setText(String.format("%,d원", mCardInfo.getTotalExpenseAmount()));
	}

	public void updateCardNumberText() {
		((TextView) findViewById(R.id.TVDetailPrepaidCardNumber)).setText(mCard.getNumber());
	}

	public void updateCardSettlementDayText() {
		((TextView) findViewById(R.id.TVDetailPrepaidCardSettlementDay)).setText(String.format("충전 %d일", mCard.getSettlementDay()));
	}

	public void updateCardNameText() {
		((TextView) findViewById(R.id.TVDetailPrepaidCardName)).setText(mCard.getName());
	}
	
	public void updateCardBalanceText() {
		((TextView) findViewById(R.id.TVDetailPrepaidCardBalance)).setText(String.format("충전 금액 : %,d원", mCard.getBalance()));
	}
	
	public void updateCardRemainAmountText() {
		((TextView) findViewById(R.id.TVDetailPrepaidCardRemainAmount)).setText(String.format("남은 금액 : %,d원", mCard.getBalance()- mCardInfo.getTotalExpenseAmount()));
	}

	@Override
	public void setBtnClickListener() {
		findViewById(R.id.BtnDetailPrepaidCardExepnseAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(CardDetailPrepaidLayout.this, ReportExpenseExpandLayout.class);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
				startActivity(intent);
			}
		});
	}
}