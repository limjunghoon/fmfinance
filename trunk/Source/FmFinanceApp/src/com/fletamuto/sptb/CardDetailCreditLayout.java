package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		
		mYear = Calendar.getInstance().YEAR;
		mMonth = Calendar.getInstance().MONTH + 1;
		mDay = Calendar.getInstance().DAY_OF_MONTH;
		
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
	}
	
	/**
	 * 카드명 갱신
	 */
	private void updateCardNameText() {
		((TextView)findViewById(R.id.TVDetailCreditCardName)).setText(mCard.getName());
	}
	/**
	 * 카드사/카드번호 갱신
	 */
	private void updateCardNumberText() {
		((TextView)findViewById(R.id.TVDetailCreditCardNumber)).setText(mCard.getCompenyName().getName() + " " + mCard.getNumber());
	}
	/**
	 * 카드 종류 갱신
	 */
	private void updateCardTypeText() {
		((TextView)findViewById(R.id.TVDetailCreditCardType)).setText(mCard.getCardTypeName());
	}
	/**
	 * 지불계좌 버튼 갱신
	 */
	private void updateCardExpenseAccountBtnText() {
		((Button)findViewById(R.id.BTDetailCreditCardExpenseAccount)).setText(mCard.getAccount().getName() + "\n(" + mCard.getAccount().getNumber() + ")");
	}
	/**
	 * 기준일별내역/월별내역 버튼 갱신
	 */
	private void updateCardBillingCategoryBtnText() {
		//상태를 구분하는 flag를 사용
		if(isBasicDate) {
			
		} else {
			
		}
	}
	private boolean isBasicDate = true;
	/**
	 * 결제 예정일 갱신
	 */
	private void updateCardBillingDateText() {
		String billingDateLastText = null;
		if(isNextBilling) {
			billingDateLastText = "결제 예정";
			if(Calendar.getInstance().getTime().getDate() < mCard.getSettlementDay()) {
				billingDateLastText = (Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA).getTime().getMonth()+1) + "월 " + mCard.getSettlementDay() + "일 " + billingDateLastText; 
			} else {
				billingDateLastText = (Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA).getTime().getMonth()+2) + "월 " + mCard.getSettlementDay() + "일 " + billingDateLastText;
			}
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setText(billingDateLastText);
		} else {
			billingDateLastText = "결제 됨";
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setText(billingDateLastText);
		}
	}
	/**
	 * 기간별내역
	 * @true	현재 이 후 기간
	 * @false	지난 기간
	 */
	private boolean isNextBilling = true;
	/**
	 * 조회기간 갱신
	 */
	private void updateCardBillingTermText() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
		((TextView)findViewById(R.id.TVDetailCreditCardBillingTerm)).setText(format.format(mCard.getStartBillingPeriod(Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA)).getTime()) + " ~ " + format.format(mCard.getEndBillingPeriod(Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA)).getTime()));
	}
	/**
	 * 아이템 개수 갱신
	 */
	private void updateCardBillingItemCountText() {
		
	}
	/**
	 * 금액 개수 갱신
	 */
	private void updateCardBillingAmountText() {
		
	}
	/**
	 * 리스너 등록
	 */
	protected void setBillingTermBtnClickListener() {
		((Button)findViewById(R.id.BTDetailCreditCardBilingDatePreButton)).setOnClickListener(billingTermBtnClickListener);
		((Button)findViewById(R.id.BTDetailCreditCardBilingDateNextButton)).setOnClickListener(billingTermBtnClickListener);
	}
	private View.OnClickListener billingTermBtnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BTDetailCreditCardBilingDatePreButton:
				mMonth -= 1;
				if(mMonth < 1) {
					mYear -= 1;
					mMonth = 12;
				}
				break;
			case R.id.BTDetailCreditCardBilingDateNextButton:
				mMonth += 1;
				if(mMonth > 12) {
					mYear += 1;
					mMonth = 1;
				}
				break;
			}
			mCard.setBillingPeriodMonth(mMonth);
			mCard.setBillingPeriodDay(mDay);
			updateCardBillingDateText();
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
//		((Button) findViewById(R.id.BtnDetailCreditCardNextBillingAmount)).setText(String.format("%,d원", mCardInfo.getNextBillingExpenseAmount()));
//	}
//
//	private void updateCardBillingAmountBtnText() {
//		String startDate = FinanceDataFormat.getDateFormat(mCard.getStartBillingPeriod(Calendar.getInstance()).getTime());
//		String endDate = FinanceDataFormat.getDateFormat(mCard.getEndBillingPeriod(Calendar.getInstance()).getTime());
//		
//		((TextView) findViewById(R.id.TVDetailCreditCardBillingDate)).setText(startDate + " ~ " + endDate);
//		((Button) findViewById(R.id.BtnDetailCreditCardBillingAmount)).setText(String.format("%,d원", mCardInfo.getBillingExpenseAmount()));
//	}
//
//	private void updateCardExpenseAmountBtnText() {
//		((Button) findViewById(R.id.BtnDetailCreditCardExepnseAmount)).setText(String.format("%,d원", mCardInfo.getTotalExpenseAmount()));
//	}
//
//	private void updateCardBalanceText() {
//		String amount = "지정되지 않았습니다.";
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
//		((TextView) findViewById(R.id.TVDetailCreditCardSettlementDay)).setText(String.format("결제일 %d일", mCard.getSettlementDay()));
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