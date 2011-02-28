package com.fletamuto.sptb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;


public class CardDetailCheckLayout extends CardDetailBaseLayout {
	protected int mYear = -1;
	protected int mMonth = -1;
	protected int mDay = -1;
	
	Calendar mCalendar = (Calendar) Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA).clone();
	
	protected ArrayList<FinanceItem> mCardExpenseItems = null;
	protected ReportCardExpenseItemAdapter mCardExpenseAdapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_detail_card, true);
        
        setBtnClickListener();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		updateChild();
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
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
	}

	public void updateChild() {
		updateCardNameText();
		updateCardNumberText();
		updateCardTypeText();
		updateCardExpenseAccountBtnText();
		updateCardBillingTermText();
		
		getCardExpenseItems();
		
		initWidget();
	}
	
	private void initWidget() {
		((Button)findViewById(R.id.BTDetailCardBillingBasicDateButton)).setVisibility(View.INVISIBLE);
		((Button)findViewById(R.id.BTDetailCardBillingMonthButton)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.TVDetailCardBillingDate)).setVisibility(View.INVISIBLE);
	}
	/**
	 * 카드명 갱신
	 */
	private void updateCardNameText() {
		((TextView)findViewById(R.id.TVDetailCardName)).setText(mCard.getName());
	}
	/**
	 * 카드사/카드번호 갱신
	 */
	private void updateCardNumberText() {
		((TextView)findViewById(R.id.TVDetailCardNumber)).setText(mCard.getCompenyName().getName() + " " + mCard.getNumber());
	}
	/**
	 * 카드 종류 갱신
	 */
	private void updateCardTypeText() {
		((TextView)findViewById(R.id.TVDetailCardType)).setText(mCard.getCardTypeName());
	}
	/**
	 * 지불계좌 버튼 갱신
	 */
	private void updateCardExpenseAccountBtnText() {
		//((Button)findViewById(R.id.BTDetailCardExpenseAccount)).setText(mCard.getAccount().getName() + "\n(" + mCard.getAccount().getNumber() + ")");
		((Button)findViewById(R.id.BTDetailCardExpenseAccount)).setText(mCard.getAccount().getCompany().getName() + "\n" + mCard.getAccount().getNumber());
	}
	/**
	 * 결제 예정일 갱신
	 */
	private void updateCardBillingDateText() {
	}
	/**
	 * 조회기간 갱신
	 */
	private void updateCardBillingTermText() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
		Calendar startDate = (Calendar) mCalendar.clone();
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		Calendar endDate = (Calendar) mCalendar.clone();
		endDate.add(Calendar.MONTH, 1);
		endDate.set(Calendar.DAY_OF_MONTH, 1);
		endDate.add(Calendar.DAY_OF_MONTH, -1);
		((TextView)findViewById(R.id.TVDetailCardBillingTerm)).setText(format.format(startDate.getTime()) + " ~ " + format.format(endDate.getTime()));
	}
	/**
	 * 아이템 얻기
	 */
	public void getCardExpenseItems() {
		CardItem card = mCardInfo.getCard();
		Calendar tatgetStartDate = (Calendar) mCalendar.clone();
		tatgetStartDate.set(Calendar.DAY_OF_MONTH, 1);
		Calendar tatgetEndDate = (Calendar) mCalendar.clone();
		tatgetEndDate.add(Calendar.MONTH, 1);
		tatgetEndDate.set(Calendar.DAY_OF_MONTH, 1);
		tatgetEndDate.add(Calendar.DAY_OF_MONTH, -1);
		mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), tatgetStartDate, tatgetEndDate);
		
		updateCardBillingItemCountText();
		updateCardBillingAmountText();
		updateCardBillingDateText();
		setAdapterList();
	}
	/**
	 * 
	 */
	protected void setAdapterList() {
    	final ListView list = (ListView)findViewById(R.id.LVDetailCardBillingItems);
    	
    	mCardExpenseAdapter = new ReportCardExpenseItemAdapter(this, R.layout.report_list_normal, mCardExpenseItems);
    	list.setAdapter(mCardExpenseAdapter);
    	
    	list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    			FinanceItem item = mCardExpenseItems.get(position);
    			
    	    	if (item.getType() == ExpenseItem.TYPE) {
    	    		startEditInputActivity(InputExpenseLayout.class, item.getID());
    	    	}
    	    	else {
    	    		startEditInputActivity(InputIncomeLayout.class, item.getID());
    	    	}
    		}
    		protected void startEditInputActivity(Class<?> cls, int itemId) {
    			Intent intent = new Intent(CardDetailCheckLayout.this, cls);
    	    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
    		}
		});
    	
    	final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
			
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			public void onShowPress(MotionEvent e) {
			}
			
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				return false;
			}
			
			public void onLongPress(MotionEvent e) {
			}
			
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				if(Math.abs(e2.getX()-e1.getX()) > 100) {
					if(e2.getX() > e1.getX()) {	//터치 후 오른쪽으로 플링
						mCalendar.add(Calendar.MONTH, -1);
						getCalendarDate();
						updateCardBillingDateTerm();
					} else {
						mCalendar.add(Calendar.MONTH, 1);
						getCalendarDate();
						updateCardBillingDateTerm();
					}
				}
				return false;
			}
			
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
    	list.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
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
		((TextView)findViewById(R.id.TVDetailCardBillingItemCount)).setText(String.valueOf(mCardExpenseItems.size()));
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
		((TextView)findViewById(R.id.TVDetailCardBillingItemAmount)).setText(String.format("%,d", amount));
	}
	/**
	 * 리스너 등록
	 */
	protected void setBillingBtnClickListener() {
		((Button)findViewById(R.id.BTDetailCardExpenseAccount)).setOnClickListener(billingBtnClickListener);
		((Button)findViewById(R.id.BTDetailCardBillingBasicDateButton)).setOnClickListener(billingBtnClickListener);
		((Button)findViewById(R.id.BTDetailCardBillingMonthButton)).setOnClickListener(billingBtnClickListener);
//		((Button)findViewById(R.id.BTDetailCardBillingDatePreButton)).setOnClickListener(billingBtnClickListener);
//		((Button)findViewById(R.id.BTDetailCardBillingDateNextButton)).setOnClickListener(billingBtnClickListener);
	}
	private View.OnClickListener billingBtnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BTDetailCardExpenseAccount:
				//Toast.makeText(CardDetailCreditLayout.this, "계좌 상세보기 버튼을 누름", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(CardDetailCheckLayout.this, DetailAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, mCard.getAccount());
				startActivity(intent);
				break;
			case R.id.BTDetailCardBillingBasicDateButton:
				//Toast.makeText(CardDetailCreditLayout.this, "기준일별내역 버튼을 누름", Toast.LENGTH_LONG).show();
				updateChild();
				break;
			case R.id.BTDetailCardBillingMonthButton:
				//Toast.makeText(CardDetailCreditLayout.this, "월별내역 버튼을 누름", Toast.LENGTH_LONG).show();
				updateChild();
				break;
//			case R.id.BTDetailCardBillingDatePreButton:
//				mCalendar.add(Calendar.MONTH, -1);
//				getCalendarDate();
//				updateCardBillingDateTerm();
//				break;
//			case R.id.BTDetailCardBillingDateNextButton:
//				mCalendar.add(Calendar.MONTH, 1);
//				getCalendarDate();
//				updateCardBillingDateTerm();
//				break;
			}
		}
	};
	/**
	 * 기간 갱신
	 */
	private void updateCardBillingDateTerm() {
		//calendar.set(Calendar.YEAR, mYear);
		//calendar.set(Calendar.MONTH, mMonth);
		//calendar.set(Calendar.DAY_OF_MONTH, mDay);
		//Toast.makeText(CardDetailCreditLayout.this, calendar.getTime().toLocaleString(), Toast.LENGTH_LONG).show();
		Calendar tempCalendar = (Calendar) mCalendar.clone();
		tempCalendar.add(Calendar.MONTH, 1);
		tempCalendar.set(Calendar.DAY_OF_MONTH, mCard.getSettlementDay());
		
		//Toast.makeText(CardDetailCheckLayout.this, tempCalendar.getTime().toLocaleString() + "\n" + (new Date()).toLocaleString(), Toast.LENGTH_LONG).show();

		//updateCardBillingDateText();
		updateCardBillingTermText();
		getCardExpenseItems();
	}
	
	@Override
	public void setBtnClickListener() {
		
	}

	@Override
	protected void setTitleBtn() {
		super.setTitleBtn();
	}
	
	
/*	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.report_detail_check_card, true);
        setContentView(R.layout.report_detail_card, true);
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
		((TextView) findViewById(R.id.TVDetailCheckCardAccountBalance)).setText(String.format("%s : 잔액 %,d원", mCard.getAccount().getCompany().getName(), mCard.getAccount().getBalance()));
	}

	private void updateCardExpenseAmountBtnText() {
		((Button) findViewById(R.id.BtnDetailCheckCardExepnseAmount)).setText(String.format("%,d원", mCardInfo.getTotalExpenseAmount()));
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
	}*/
}