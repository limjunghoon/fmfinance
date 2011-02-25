package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fletamuto.sptb.CardPaymentLayout.ReportCardExpenseItemAdapter;
import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;
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
		//updateCardBillingItemCountText();
		//updateCardBillingAmountText();
		
		setBillingTermBtnClickListener();
		
		getCardExpenseItems();
		setAlarmAdapterList();
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
			if(calendar.getTime().getDate() < mCard.getSettlementDay()) {
				billingDateLastText = (calendar.getTime().getMonth()+1) + "월 " + mCard.getSettlementDay() + "일 " + billingDateLastText; 
			} else {
				billingDateLastText = (calendar.getTime().getMonth()+2) + "월 " + mCard.getSettlementDay() + "일 " + billingDateLastText;
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
		((TextView)findViewById(R.id.TVDetailCreditCardBillingTerm)).setText(format.format(mCard.getStartBillingPeriod((Calendar)calendar.clone()).getTime()) + " ~ " + format.format(mCard.getEndBillingPeriod((Calendar)calendar.clone()).getTime()));
	}
	Calendar calendar = (Calendar) Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA).clone();
	/**
	 * 아이템 얻기
	 */
	public void getCardExpenseItems() {
		CardItem card = mCardInfo.getCard();
		Calendar tatgetDate = mCardInfo.getSettlementDate();
		if(isBasicDate)
			mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), card.getStartBillingPeriod(tatgetDate), card.getEndBillingPeriod(tatgetDate));
		else
			mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), card.getStartBillingPeriod(tatgetDate), card.getEndBillingPeriod(tatgetDate));	// FIXME 이 부분을 각 달의 첫날과 마지막 달로 넣는 것으로 바꾸어야 함
		
		updateCardBillingItemCountText();
		updateCardBillingAmountText();
	}
	protected ArrayList<FinanceItem> mCardExpenseItems = null;
	protected ReportCardExpenseItemAdapter mCardExpenseAdapter = null;
	/**
	 * 
	 */
	protected void setAlarmAdapterList() {
    	final ListView listAlarm = (ListView)findViewById(R.id.LVDetailCreditCardBillingItems);
    	
    	mCardExpenseAdapter = new ReportCardExpenseItemAdapter(this, R.layout.report_list_normal, mCardExpenseItems);
    	listAlarm.setAdapter(mCardExpenseAdapter);
    	
    	listAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
			}
		});
    }
	public class ReportCardExpenseItemAdapter extends ArrayAdapter<FinanceItem> {
    	int mResource;
    	private LayoutInflater mInflater;

		public ReportCardExpenseItemAdapter(Context context, int resource,
				 List<FinanceItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinanceItem item = (FinanceItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
				
				ViewHolder viewHolder = new ViewHolder(
						(TextView)convertView.findViewById(R.id.TVListLeft), 
						(TextView)convertView.findViewById(R.id.TVListCenterTop), 
						(TextView)convertView.findViewById(R.id.TVListCenterBottom), 
						(TextView)convertView.findViewById(R.id.TVListRightTop), 
						(TextView)convertView.findViewById(R.id.TVListRightBottom));
				
				convertView.setTag(viewHolder);
			}
			setExpenseListViewText((ExpenseItem)item, convertView);
			
			return convertView;
		}
		protected void setExpenseListViewText(ExpenseItem expense, View convertView) {
	    	ViewHolder viewHolder = (ViewHolder) convertView.getTag();
	    	viewHolder.getLeftTextView().setText(expense.getCategory().getName());
			TextView tvSubCategory = viewHolder.getCenterTopTextView() ;
			tvSubCategory.setText(expense.getSubCategory().getName());
			
			TextView tvAmount = viewHolder.getRightTopTextView(); 
			tvAmount.setText(String.format("%,d원", -expense.getAmount()));
			tvAmount.setTextColor(Color.RED);
			
			TextView tvMemo = viewHolder.getCenterBottomTextView() ;
			if (expense.getMemo().length() != 0) {
				tvMemo.setText(expense.getMemo());
			}
			else {
				tvMemo.setVisibility(View.GONE);
			}
			
			TextView tvMothod = viewHolder.getRightBottomTextView(); 
			tvMothod.setText(expense.getPaymentMethod().getText());
	    }
    }
	
	/**
	 * 아이템 개수 갱신
	 */
	private void updateCardBillingItemCountText() {
		((TextView)findViewById(R.id.TVDetailCreditCardBillingItemCount)).setText(String.valueOf(mCardExpenseItems.size()));
	}
	/**
	 * 항목 전체 합산 금액  갱신
	 */
	private void updateCardBillingAmountText() {
		long amount = 0;
		for(int i = 0, count = mCardExpenseItems.size(); i < count; i++) {
			amount += mCardExpenseItems.get(i).getAmount();
		}
		((TextView)findViewById(R.id.TVDetailCreditCardBillingItemAmount)).setText(String.valueOf(amount));
	}
	/**
	 * 리스너 등록
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