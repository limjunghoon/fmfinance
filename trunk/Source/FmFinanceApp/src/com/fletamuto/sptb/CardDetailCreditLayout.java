package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.util.FinanceDataFormat;


public class CardDetailCreditLayout extends CardDetailBaseLayout {
	protected int mYear = -1;
	protected int mMonth = -1;
	protected int mDay = -1;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_detail_credit_card, true);
        updateChild();
        
        setBtnClickListener();
    }
	
	@Override
	protected void initialize() {
		super.initialize();
		
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		if (mCard == null) {
		}
	}
	
	public void updateChild() {
		//updateCardNameText();
		//updateCardSettlementDayText();
		//updateCardNumberText();
		//updateCardBalanceText();
		//updateCardExpenseAmountBtnText();
		//updateCardBillingAmountBtnText();
		//updateCardNextBillingAmountBtnText();
		updateCardNameText();
		updateCardNumberText();
		updateCardTypeText();
		updateCardExpenseAccountBtnText();
		updateCardBillingCategoryBtnText();
		updateCardBillingDateText();
		updateCardBillingTermText();
		updateCardBillingItemCountText();
		updateCardBillingAmountText();
		
		setBillingTermBtnClickListener();
	}
	
	/**
	 * ī��� ����
	 */
	private void updateCardNameText() {
		((TextView)findViewById(R.id.TVDetailCreditCardName)).setText(mCard.getName());
	}
	/**
	 * ī���/ī���ȣ ����
	 */
	private void updateCardNumberText() {
		((TextView)findViewById(R.id.TVDetailCreditCardNumber)).setText(mCard.getCompenyName().getName() + " " + mCard.getNumber());
	}
	/**
	 * ī�� ���� ����
	 */
	private void updateCardTypeText() {
		((TextView)findViewById(R.id.TVDetailCreditCardType)).setText(mCard.getCardTypeName());
	}
	/**
	 * ���Ұ��� ��ư ����
	 */
	private void updateCardExpenseAccountBtnText() {
		((Button)findViewById(R.id.BTDetailCreditCardExpenseAccount)).setText(mCard.getAccount().getName() + "\n(" + mCard.getAccount().getNumber() + ")");
	}
	/**
	 * �����Ϻ�����/�������� ��ư ����
	 */
	private void updateCardBillingCategoryBtnText() {
		//���¸� �����ϴ� flag�� ���
		if(isBasicDate) {
			
		} else {
			
		}
	}
	private boolean isBasicDate = true;
	/**
	 * ���� ������ ����
	 */
	private void updateCardBillingDateText() {
		String billingDateLastText = null;
		if(isNextBilling) {
			billingDateLastText = "���� ����";
			if(calendar.getTime().getDate() < mCard.getSettlementDay()) {
				billingDateLastText = (calendar.getTime().getMonth()+1) + "�� " + mCard.getSettlementDay() + "�� " + billingDateLastText; 
			} else {
				billingDateLastText = (calendar.getTime().getMonth()+2) + "�� " + mCard.getSettlementDay() + "�� " + billingDateLastText;
			}
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setText(billingDateLastText);
		} else {
			billingDateLastText = "���� ��";
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setText(billingDateLastText);
		}
	}
	/**
	 * �Ⱓ������
	 * @true	���� �� �� �Ⱓ
	 * @false	���� �Ⱓ
	 */
	private boolean isNextBilling = true;
	/**
	 * ��ȸ�Ⱓ ����
	 */
	private void updateCardBillingTermText() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy�� MM�� dd��");
		((TextView)findViewById(R.id.TVDetailCreditCardBillingTerm)).setText(format.format(mCard.getStartBillingPeriod((Calendar)calendar.clone()).getTime()) + " ~ " + format.format(mCard.getEndBillingPeriod((Calendar)calendar.clone()).getTime()));
	}
	Calendar calendar = (Calendar) Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA).clone();
	/**
	 * ������ ���� ����
	 */
	private void updateCardBillingItemCountText() {
		
	}
	/**
	 * �ݾ� ���� ����
	 */
	private void updateCardBillingAmountText() {
		
	}
	/**
	 * ������ ���
	 */
	protected void setBillingTermBtnClickListener() {
		((Button)findViewById(R.id.BTDetailCreditCardBillingDatePreButton)).setOnClickListener(billingTermBtnClickListener);
		((Button)findViewById(R.id.BTDetailCreditCardBillingDateNextButton)).setOnClickListener(billingTermBtnClickListener);
	}
	private View.OnClickListener billingTermBtnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BTDetailCreditCardBillingDatePreButton:
				mMonth -= 1;
				if(mMonth < 1) {
					mYear -= 1;
					mMonth = 12;
				}
				break;
			case R.id.BTDetailCreditCardBillingDateNextButton:
				mMonth += 1;
				if(mMonth > 12) {
					mYear += 1;
					mMonth = 1;
				}
				break;
			}
			calendar.set(Calendar.YEAR, mYear);
			calendar.set(Calendar.MONTH, mMonth);
			calendar.set(Calendar.DAY_OF_MONTH, mDay);
			//Toast.makeText(CardDetailCreditLayout.this, calendar.getTime().toLocaleString(), Toast.LENGTH_LONG).show();
			int year = mYear-1900, month = mMonth+1, day = mCard.getSettlementDay();
			if(mMonth > 12) {
				year += 1;
				month = 1;
			} else if(mMonth < 1) {
				year -= 1;
				month = 12;
			}
			Date dateTemp = new Date(Date.UTC(year, month, day, 0, 0, 0));
			
			Toast.makeText(CardDetailCreditLayout.this, dateTemp.toLocaleString() + "\n" + (new Date()).toLocaleString(), Toast.LENGTH_LONG).show();
			if(dateTemp.getTime() >= (new Date()).getTime()) {
				isNextBilling = true;
			} else {
				isNextBilling = false;
			}
			updateCardBillingDateText();
			updateCardBillingTermText();
		}
	};
	
	@Override
	public void setBtnClickListener() {
		
	}
	
//	private void updateCardNextBillingAmountBtnText() {
//		String startDate = FinanceDataFormat.getDateFormat(mCard.getNextStartBillingPeriod(Calendar.getInstance()).getTime());
//		String endDate = FinanceDataFormat.getDateFormat(mCard.getNextEndBillingPeriod(Calendar.getInstance()).getTime());
//		
//		((TextView) findViewById(R.id.TVDetailCreditCardNextBillingDate)).setText(startDate + " ~ " + endDate);
//		((Button) findViewById(R.id.BtnDetailCreditCardNextBillingAmount)).setText(String.format("%,d��", mCardInfo.getNextBillingExpenseAmount()));
//	}
//
//	private void updateCardBillingAmountBtnText() {
//		String startDate = FinanceDataFormat.getDateFormat(mCard.getStartBillingPeriod(Calendar.getInstance()).getTime());
//		String endDate = FinanceDataFormat.getDateFormat(mCard.getEndBillingPeriod(Calendar.getInstance()).getTime());
//		
//		((TextView) findViewById(R.id.TVDetailCreditCardBillingDate)).setText(startDate + " ~ " + endDate);
//		((Button) findViewById(R.id.BtnDetailCreditCardBillingAmount)).setText(String.format("%,d��", mCardInfo.getBillingExpenseAmount()));
//	}
//
//	private void updateCardExpenseAmountBtnText() {
//		((Button) findViewById(R.id.BtnDetailCreditCardExepnseAmount)).setText(String.format("%,d��", mCardInfo.getTotalExpenseAmount()));
//	}
//
//	private void updateCardBalanceText() {
//		String amount = "�������� �ʾҽ��ϴ�.";
//		
//		if (mCard.getBalance() != 0L) {
//			amount = String.valueOf(mCard.getBalance());
//		}
//		((TextView) findViewById(R.id.TVDetailCreditCardBalanceAmount)).setText(amount);
//	}
//
//	public void updateCardNumberText() {
//		((TextView) findViewById(R.id.TVDetailCreditCardNumber)).setText(mCard.getNumber());
//	}
//
//	public void updateCardSettlementDayText() {
//		((TextView) findViewById(R.id.TVDetailCreditCardSettlementDay)).setText(String.format("������ %d��", mCard.getSettlementDay()));
//	}
//
//	public void updateCardNameText() {
//		((TextView) findViewById(R.id.TVDetailCreditCardName)).setText(mCard.getName());
//	}
//
//	@Override
//	public void setBtnClickListener() {
//		findViewById(R.id.BtnDetailCreditCardExepnseAmount).setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(CardDetailCreditLayout.this, ReportExpenseExpandLayout.class);
//				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
//				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
//				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
//				startActivity(intent);
//			}
//		});
//		
//		findViewById(R.id.BtnDetailCreditCardBillingAmount).setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				Intent intent = new Intent(CardDetailCreditLayout.this, ReportExpenseExpandLayout.class);
//				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
//				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
//				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
//				intent.putExtra(MsgDef.ExtraNames.CARD_BILLING, CardItem.BILLING);
//				startActivity(intent);
//			}
//		});
//
//	findViewById(R.id.BtnDetailCreditCardNextBillingAmount).setOnClickListener(new View.OnClickListener() {
//	
//			public void onClick(View v) {
//				Intent intent = new Intent(CardDetailCreditLayout.this, ReportExpenseExpandLayout.class);
//				intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mMonth);
//				intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mYear);
//				intent.putExtra(MsgDef.ExtraNames.CARD_ITEM, mCard);
//				intent.putExtra(MsgDef.ExtraNames.CARD_BILLING, CardItem.NEXT_BILLING);
//				startActivity(intent);
//			}
//		});
//	}
}