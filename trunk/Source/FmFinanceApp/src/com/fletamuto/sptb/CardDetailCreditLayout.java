package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.util.FinanceDataFormat;


public class CardDetailCreditLayout extends CardDetailBaseLayout {  	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_detail_credit_card, true);
        updateChild();
        
        setBtnClickListener();
    }
	
	@Override
	protected void initialize() {
		super.initialize();
		
		if (mCard == null) {
		}
	}
	
	public void updateChild() {
		updateCardNameText();
		updateCardSettlementDayText();
		updateCardNumberText();
		updateCardBalanceText();
		updateCardExpenseAmountBtnText();
		updateCardBillingAmountBtnText();
		updateCardNextBillingAmountBtnText();
	}
	
	private void updateCardNextBillingAmountBtnText() {
		String startDate = FinanceDataFormat.getDateFormat(mCard.getNextStartBillingPeriod(Calendar.getInstance()).getTime());
		String endDate = FinanceDataFormat.getDateFormat(mCard.getNextEndBillingPeriod(Calendar.getInstance()).getTime());
		
		((TextView) findViewById(R.id.TVDetailCreditCardNextBilingDate)).setText(startDate + " ~ " + endDate);
		((Button) findViewById(R.id.BtnDetailCreditCardNextBilingAmount)).setText(String.format("%,d원", mCardInfo.getNextBillingExpenseAmount()));
	}

	private void updateCardBillingAmountBtnText() {
		String startDate = FinanceDataFormat.getDateFormat(mCard.getStartBillingPeriod(Calendar.getInstance()).getTime());
		String endDate = FinanceDataFormat.getDateFormat(mCard.getEndBillingPeriod(Calendar.getInstance()).getTime());
		
		((TextView) findViewById(R.id.TVDetailCreditCardBilingDate)).setText(startDate + " ~ " + endDate);
		((Button) findViewById(R.id.BtnDetailCreditCardBilingAmount)).setText(String.format("%,d원", mCardInfo.getBillingExpenseAmount()));
	}

	private void updateCardExpenseAmountBtnText() {
		((Button) findViewById(R.id.BtnDetailCreditCardExepnseAmount)).setText(String.format("%,d원", mCardInfo.getTotalExpenseAmount()));
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
		
		findViewById(R.id.BtnDetailCreditCardBilingAmount).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(CardDetailCreditLayout.this, ReportExpenseExpandLayout.class);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
				intent.putExtra(MsgDef.ExtraNames.CARD_BILLING, CardItem.BILLING);
				startActivity(intent);
			}
		});

	findViewById(R.id.BtnDetailCreditCardNextBilingAmount).setOnClickListener(new View.OnClickListener() {
	
			public void onClick(View v) {
				Intent intent = new Intent(CardDetailCreditLayout.this, ReportExpenseExpandLayout.class);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
				intent.putExtra(MsgDef.ExtraNames.CARD_BILLING, CardItem.NEXT_BILLING);
				startActivity(intent);
			}
		});
	}
}