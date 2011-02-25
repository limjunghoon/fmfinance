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
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.CardPayment;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceDataFormat;
import com.fletamuto.sptb.view.FmBaseLayout;


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
		
		if (mCard == null) {
		}
		
		getCalendarDate();
		setBillingBtnClickListener();
	}
	
	private void getCalendarDate() {
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
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
		
		getCardExpenseItems();
		
		initWidget();
	}
	
	private void initWidget() {
		if(isBasicDate)
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setVisibility(View.VISIBLE);
		else
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setVisibility(View.INVISIBLE);
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
			int addMonth = 1;
			if((new Date()).getDate() >= mCard.getSettlementDay())
				addMonth = 2;
			Calendar tempCalendar = (Calendar) calendar.clone();
			tempCalendar.add(Calendar.MONTH, addMonth);
			tempCalendar.set(Calendar.DAY_OF_MONTH, mCard.getSettlementDay());
			
			billingDateLastText = tempCalendar.get(Calendar.YEAR) + "년 " + tempCalendar.get(Calendar.MONTH) + "월 " + tempCalendar.get(Calendar.DAY_OF_MONTH) + "일 " + billingDateLastText;
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setText(billingDateLastText);
		} else {
			billingDateLastText = "결제 됨";
			((TextView)findViewById(R.id.TVDetailCreditCardBillingDate)).setText(billingDateLastText);
		}
	}
	/**
	 * <p>기간별내역</p>
	 * <b>true</b><br/><dd>현재 이 후 기간</dd><br/>
	 * <b>false</b><br/><dd>지난 기간</dd>
	 */
	private boolean isNextBilling = true;
	/**
	 * 조회기간 갱신
	 */
	private void updateCardBillingTermText() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
		if(isBasicDate)
			((TextView)findViewById(R.id.TVDetailCreditCardBillingTerm)).setText(format.format(mCard.getStartBillingPeriod(calendar).getTime()) + " ~ " + format.format(mCard.getEndBillingPeriod(calendar).getTime()));
		else {
			Calendar startDate = (Calendar) calendar.clone();
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			Calendar endDate = (Calendar) calendar.clone();
			endDate.add(Calendar.MONTH, 1);
			endDate.set(Calendar.DAY_OF_MONTH, 1);
			endDate.add(Calendar.DAY_OF_MONTH, -1);
			((TextView)findViewById(R.id.TVDetailCreditCardBillingTerm)).setText(format.format(startDate.getTime()) + " ~ " + format.format(endDate.getTime()));
		}
	}
	Calendar calendar = (Calendar) Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA).clone();
	/**
	 * 아이템 얻기
	 */
	public void getCardExpenseItems() {
		CardItem card = mCardInfo.getCard();
		if(isBasicDate) {
			Calendar tatgetDate = (Calendar) calendar.clone();
			mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), card.getStartBillingPeriod(tatgetDate), card.getEndBillingPeriod(tatgetDate));
		} else {
			Calendar tatgetStartDate = (Calendar) calendar.clone();
			tatgetStartDate.set(Calendar.DAY_OF_MONTH, 1);
			Calendar tatgetEndDate = (Calendar) calendar.clone();
			tatgetEndDate.add(Calendar.MONTH, 1);
			tatgetEndDate.set(Calendar.DAY_OF_MONTH, 1);
			tatgetEndDate.add(Calendar.DAY_OF_MONTH, -1);
			mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), tatgetStartDate, tatgetEndDate);
		}
		
		updateCardBillingItemCountText();
		updateCardBillingAmountText();
		setAdapterList();
	}
	protected ArrayList<FinanceItem> mCardExpenseItems = null;
	protected ReportCardExpenseItemAdapter mCardExpenseAdapter = null;
	/**
	 * 
	 */
	protected void setAdapterList() {
    	final ListView list = (ListView)findViewById(R.id.LVDetailCreditCardBillingItems);
    	
    	mCardExpenseAdapter = new ReportCardExpenseItemAdapter(this, R.layout.report_list_normal, mCardExpenseItems);
    	list.setAdapter(mCardExpenseAdapter);
    	
    	list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
						(TextView)convertView.findViewById(R.id.TVTitle),
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
			//mCardExpenseItems.get(i).
			amount += mCardExpenseItems.get(i).getAmount();
		}
		((TextView)findViewById(R.id.TVDetailCreditCardBillingItemAmount)).setText(String.valueOf(amount));
	}
	/**
	 * 리스너 등록
	 */
	protected void setBillingBtnClickListener() {
		((Button)findViewById(R.id.BTDetailCreditCardExpenseAccount)).setOnClickListener(billingBtnClickListener);
		((Button)findViewById(R.id.BTDetailCreditCardBillingBasicDateButton)).setOnClickListener(billingBtnClickListener);
		((Button)findViewById(R.id.BTDetailCreditCardBillingMonthButton)).setOnClickListener(billingBtnClickListener);
		((Button)findViewById(R.id.BTDetailCreditCardBillingDatePreButton)).setOnClickListener(billingBtnClickListener);
		((Button)findViewById(R.id.BTDetailCreditCardBillingDateNextButton)).setOnClickListener(billingBtnClickListener);
	}
	private View.OnClickListener billingBtnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BTDetailCreditCardExpenseAccount:
				Toast.makeText(CardDetailCreditLayout.this, "계좌 상세보기 버튼을 누름", Toast.LENGTH_LONG).show();
				break;
			case R.id.BTDetailCreditCardBillingBasicDateButton:
				isBasicDate = true;
				//Toast.makeText(CardDetailCreditLayout.this, "기준일별내역 버튼을 누름", Toast.LENGTH_LONG).show();
				updateChild();
				break;
			case R.id.BTDetailCreditCardBillingMonthButton:
				isBasicDate = false;
				//Toast.makeText(CardDetailCreditLayout.this, "월별내역 버튼을 누름", Toast.LENGTH_LONG).show();
				updateChild();
				break;
			case R.id.BTDetailCreditCardBillingDatePreButton:
				calendar.add(Calendar.MONTH, -1);
				getCalendarDate();
				updateCardBillingDateTerm();
				break;
			case R.id.BTDetailCreditCardBillingDateNextButton:
				calendar.add(Calendar.MONTH, 1);
				getCalendarDate();
				updateCardBillingDateTerm();
				break;
			}
		}
	};
	private void updateCardBillingDateTerm() {
		//calendar.set(Calendar.YEAR, mYear);
		//calendar.set(Calendar.MONTH, mMonth);
		//calendar.set(Calendar.DAY_OF_MONTH, mDay);
		//Toast.makeText(CardDetailCreditLayout.this, calendar.getTime().toLocaleString(), Toast.LENGTH_LONG).show();
		Calendar tempCalendar = (Calendar) calendar.clone();
		tempCalendar.add(Calendar.MONTH, 1);
		tempCalendar.set(Calendar.DAY_OF_MONTH, mCard.getSettlementDay());
		
		Toast.makeText(CardDetailCreditLayout.this, tempCalendar.getTime().toLocaleString() + "\n" + (new Date()).toLocaleString(), Toast.LENGTH_LONG).show();
		if(tempCalendar.getTimeInMillis() >= (new Date()).getTime()) {
			isNextBilling = true;
		} else {
			isNextBilling = false;
		}
		updateCardBillingDateText();
		updateCardBillingTermText();
		getCardExpenseItems();
	}
	
	@Override
	public void setBtnClickListener() {
		
	}

	/**
	 * 일반적인 액티비티 호출 되었을 경우
	 */
	public static final int ACTION_DEFAULT = 0;
	/**
	 * 알림을 통해서 액티비티가 호출 되었을 경우
	 */
	public static final int ACTION_NOTIFICATION_INTO = 1;
	@Override
	protected void setTitleBtn() {
		super.setTitleBtn();
		
		switch(getIntent().getIntExtra("Action", ACTION_DEFAULT)) {
		case ACTION_DEFAULT:
			break;
		case ACTION_NOTIFICATION_INTO:
			setTitle("카드 결제");
			setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "완료");
			setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
			setTitleBtnText(FmBaseLayout.BTN_LEFT_01, "취소");
			setTitleBtnVisibility(FmBaseLayout.BTN_LEFT_01, View.VISIBLE);
			
			((Button)findViewById(R.id.BTDetailCreditCardBillingBasicDateButton)).setVisibility(View.INVISIBLE);
			((Button)findViewById(R.id.BTDetailCreditCardBillingMonthButton)).setVisibility(View.INVISIBLE);
			((Button)findViewById(R.id.BTDetailCreditCardBillingDatePreButton)).setVisibility(View.INVISIBLE);
			((Button)findViewById(R.id.BTDetailCreditCardBillingDateNextButton)).setVisibility(View.INVISIBLE);
			
			setButtonListener();
			break;
		}
	}
	public void setButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				AccountItem account = DBMgr.getAccountItem(mCardInfo.getCard().getAccount().getID());
				if (account != null) {
					long balance = account.getBalance();
					
					if (balance < mCardInfo.getBillingExpenseAmount()) {
						account.setBalance(0L);
					}else {
						account.setBalance(balance - mCardInfo.getBillingExpenseAmount());
					}
					
					DBMgr.updateAccount(account);
					
				}
				
				CardPayment cardPayment = new CardPayment();
				cardPayment.setCardId(mCardInfo.getCard().getID());
				cardPayment.setPaymentAmount(mCardInfo.getBillingExpenseAmount());
				cardPayment.setPaymentDate(mCardInfo.getSettlementDate());
				cardPayment.setRemainAmount(0L);
				cardPayment.setState(CardPayment.DONE);
				DBMgr.addCardPaymentItem(cardPayment);
				
				finish();
			}
		});
		setTitleButtonListener(FmTitleLayout.BTN_LEFT_01, new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
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