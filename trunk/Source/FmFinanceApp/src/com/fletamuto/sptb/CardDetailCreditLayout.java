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
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.CardPayment;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

import com.fletamuto.sptb.view.FmBaseLayout;


public class CardDetailCreditLayout extends CardDetailBaseLayout {
	protected int mYear = -1;
	protected int mMonth = -1;
	protected int mDay = -1;
	
	/**
	 * <p>�Ⱓ������</p>
	 * <b>true</b><br/><dd>���� �� �� �Ⱓ</dd><br/>
	 * <b>false</b><br/><dd>���� �Ⱓ</dd>
	 */
	private boolean isNextBilling = true;
	/**
	 * <p>�Ⱓ��/��������</p>
	 * <b>true</b><br/><dd>�Ⱓ������</dd><br/>
	 * <b>false</b><br/><dd>��������</dd>
	 */
	private boolean isBasicDate = true;
	/**
	 * <p>�ֱ� ���� ������ ���� ����</p>
	 * <b>true</b><br/><dd>������</dd><br/>
	 * <b>false</b><br/><dd>�����ȵ�</dd>
	 */
	private boolean isDailyListItem = false;
	Calendar mCalendar = (Calendar) Calendar.getInstance(TimeZone.getDefault(), Locale.KOREA).clone();
	protected ArrayList<FinanceItem> mCardExpenseItems = null;
	protected ReportCardExpenseItemAdapter mCardExpenseAdapter = null;
	/**
	 * �Ϲ����� ��Ƽ��Ƽ ȣ�� �Ǿ��� ���
	 */
	public static final int ACTION_DEFAULT = 0;
	/**
	 * �˸��� ���ؼ� ��Ƽ��Ƽ�� ȣ�� �Ǿ��� ���
	 */
	public static final int ACTION_NOTIFICATION_INTO = 1;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_detail_card, true);
        //updateChild();
        
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
		//updateCardBillingDateText();
		updateCardBillingTermText();
		//updateCardBillingItemCountText();
		//updateCardBillingAmountText();
		
		getCardExpenseItems();
		//updateCardBillingDateText();
		
		initWidget();
	}
	
	private void initWidget() {
		if(isBasicDate)
			((TextView)findViewById(R.id.TVDetailCardBillingDate)).setVisibility(View.VISIBLE);
		else
			((TextView)findViewById(R.id.TVDetailCardBillingDate)).setVisibility(View.INVISIBLE);
	}

	/**
	 * ī��� ����
	 */
	private void updateCardNameText() {
		((TextView)findViewById(R.id.TVDetailCardName)).setText(mCard.getName());
	}
	/**
	 * ī���/ī���ȣ ����
	 */
	private void updateCardNumberText() {
		((TextView)findViewById(R.id.TVDetailCardNumber)).setText(mCard.getCompenyName().getName() + " " + mCard.getNumber());
	}
	/**
	 * ī�� ���� ����
	 */
	private void updateCardTypeText() {
		((TextView)findViewById(R.id.TVDetailCardType)).setText(mCard.getCardTypeName());
	}
	/**
	 * ���Ұ��� ��ư ����
	 */
	private void updateCardExpenseAccountBtnText() {
		//((Button)findViewById(R.id.BTDetailCardExpenseAccount)).setText(mCard.getAccount().getName() + "\n(" + mCard.getAccount().getNumber() + ")");
		((Button)findViewById(R.id.BTDetailCardExpenseAccount)).setText(mCard.getAccount().getCompany().getName() + "\n" + mCard.getAccount().getNumber());
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
	/**
	 * ���� ������ ����
	 */
	private void updateCardBillingDateText() {
		String billingDateLastText = null;
		if(isNextBilling || isDailyListItem) {
			billingDateLastText = "���� ����";
			int addMonth = 1;
			if(mCard.getSettlementDay() >= (new Date()).getDate())
				addMonth = 0;
			Calendar tempCalendar = (Calendar) mCalendar.clone();
			tempCalendar.add(Calendar.MONTH, addMonth);
			tempCalendar.set(Calendar.DAY_OF_MONTH, mCard.getSettlementDay());
			
			billingDateLastText = tempCalendar.get(Calendar.YEAR) + "�� " + (tempCalendar.get(Calendar.MONTH)+1) + "�� " + tempCalendar.get(Calendar.DAY_OF_MONTH) + "�� " + billingDateLastText;
			((TextView)findViewById(R.id.TVDetailCardBillingDate)).setText(billingDateLastText);
		} else {
			billingDateLastText = "���� ��";
			((TextView)findViewById(R.id.TVDetailCardBillingDate)).setText(billingDateLastText);
		}
	}
	/**
	 * ��ȸ�Ⱓ ����
	 */
	private void updateCardBillingTermText() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy�� MM�� dd��");
		if(isBasicDate)
			((TextView)findViewById(R.id.TVDetailCardBillingTerm)).setText(format.format(mCard.getStartBillingPeriod(mCalendar).getTime()) + " ~ " + format.format(mCard.getEndBillingPeriod(mCalendar).getTime()));
		else {
			Calendar startDate = (Calendar) mCalendar.clone();
			startDate.set(Calendar.DAY_OF_MONTH, 1);
			Calendar endDate = (Calendar) mCalendar.clone();
			endDate.add(Calendar.MONTH, 1);
			endDate.set(Calendar.DAY_OF_MONTH, 1);
			endDate.add(Calendar.DAY_OF_MONTH, -1);
			((TextView)findViewById(R.id.TVDetailCardBillingTerm)).setText(format.format(startDate.getTime()) + " ~ " + format.format(endDate.getTime()));
		}
	}
	/**
	 * ������ ���
	 */
	public void getCardExpenseItems() {
		CardItem card = mCardInfo.getCard();
		if(isBasicDate) {
			Calendar tatgetDate = (Calendar) mCalendar.clone();
			mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), card.getStartBillingPeriod(tatgetDate), card.getEndBillingPeriod(tatgetDate));
			
			ArrayList<FinanceItem> expenseItems = DBMgr.getItems(ExpenseItem.TYPE, tatgetDate);
			if(expenseItems.size() > 0)
				isDailyListItem = true;
			else
				isDailyListItem = false;
			//Toast.makeText(this, ""+expenseItems.size(), Toast.LENGTH_SHORT).show();
		} else {
			Calendar tatgetStartDate = (Calendar) mCalendar.clone();
			tatgetStartDate.set(Calendar.DAY_OF_MONTH, 1);
			Calendar tatgetEndDate = (Calendar) mCalendar.clone();
			tatgetEndDate.add(Calendar.MONTH, 1);
			tatgetEndDate.set(Calendar.DAY_OF_MONTH, 1);
			tatgetEndDate.add(Calendar.DAY_OF_MONTH, -1);
			mCardExpenseItems = DBMgr.getCardExpenseItems(card.getID(), tatgetStartDate, tatgetEndDate);
			isDailyListItem = false;
		}
		
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
    			Intent intent = new Intent(CardDetailCreditLayout.this, cls);
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
					if(e2.getX() > e1.getX()) {	//��ġ �� ���������� �ø�
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
				if(getIntent().getIntExtra("Action", ACTION_DEFAULT) == ACTION_NOTIFICATION_INTO)
						return false;
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
			tvAmount.setText(String.format("%,d��", -expense.getAmount()));
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
	 * ������ ���� ����
	 */
	private void updateCardBillingItemCountText() {
		((TextView)findViewById(R.id.TVDetailCardBillingItemCount)).setText(String.valueOf(mCardExpenseItems.size()));
	}
	/**
	 * �׸� ��ü �ջ� �ݾ�  ����
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
	 * ������ ���
	 */
	protected void setBillingBtnClickListener() {
		((Button)findViewById(R.id.BTDetailCardExpenseAccount)).setOnClickListener(billingBtnClickListener);
		((TextView)findViewById(R.id.TVDetailCardBillingBasicDate)).setOnClickListener(billingBtnClickListener);
		((TextView)findViewById(R.id.TVDetailCardBillingMonth)).setOnClickListener(billingBtnClickListener);
		//((Button)findViewById(R.id.BTDetailCardBillingDatePreButton)).setOnClickListener(billingBtnClickListener);
		//((Button)findViewById(R.id.BTDetailCardBillingDateNextButton)).setOnClickListener(billingBtnClickListener);
	}
	private View.OnClickListener billingBtnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.BTDetailCardExpenseAccount:
				//Toast.makeText(CardDetailCreditLayout.this, "���� �󼼺��� ��ư�� ����", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(CardDetailCreditLayout.this, DetailAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, mCard.getAccount());
				startActivity(intent);
				break;
			case R.id.TVDetailCardBillingBasicDate:
				isBasicDate = true;
				//Toast.makeText(CardDetailCreditLayout.this, "�����Ϻ����� ��ư�� ����", Toast.LENGTH_LONG).show();
				updateChild();
				break;
			case R.id.TVDetailCardBillingMonth:
				isBasicDate = false;
				//Toast.makeText(CardDetailCreditLayout.this, "�������� ��ư�� ����", Toast.LENGTH_LONG).show();
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
	 * �Ⱓ ����
	 */
	private void updateCardBillingDateTerm() {
		//calendar.set(Calendar.YEAR, mYear);
		//calendar.set(Calendar.MONTH, mMonth);
		//calendar.set(Calendar.DAY_OF_MONTH, mDay);
		Calendar tempCalendar = (Calendar) mCalendar.clone();
		tempCalendar.add(Calendar.MONTH, 1);
		tempCalendar.set(Calendar.DAY_OF_MONTH, mCard.getSettlementDay());
		
		if(tempCalendar.getTimeInMillis() >= (new Date()).getTime()) {
			isNextBilling = true;
		} else {
			isNextBilling = false;
		}
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
		
		switch(getIntent().getIntExtra("Action", ACTION_DEFAULT)) {
		case ACTION_DEFAULT:
			setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "����");
			setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
			break;
		case ACTION_NOTIFICATION_INTO:
			setTitle("ī�� ����");
			setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "�Ϸ�");
			setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
			setTitleBtnText(FmBaseLayout.BTN_LEFT_01, "���");
			setTitleBtnVisibility(FmBaseLayout.BTN_LEFT_01, View.VISIBLE);
			
			((TextView)findViewById(R.id.TVDetailCardBillingBasicDate)).setVisibility(View.INVISIBLE);
			((TextView)findViewById(R.id.TVDetailCardBillingMonth)).setVisibility(View.INVISIBLE);
			//((Button)findViewById(R.id.BTDetailCardBillingDatePreButton)).setVisibility(View.INVISIBLE);
			//((Button)findViewById(R.id.BTDetailCardBillingDateNextButton)).setVisibility(View.INVISIBLE);
			break;
		}
		setButtonListener();
	}
	protected Class<?> getEditCardClass(int type) {
		if (type == CardItem.CREDIT_CARD) {
			return InputCreditCardLayout.class;
		}
		else if (type == CardItem.CHECK_CARD) {
			return InputCheckCardLayout.class;
		}
		else if (type == CardItem.PREPAID_CARD) {
			return InputPrepaidCardLayout.class;
		}
		return null;
	}
	protected void startEditInputActivity(int itemId, Class<?> cls) {
		Intent intent = new Intent(this, cls);
    	intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, itemId);
    	startActivityForResult(intent, EditCardLayout.ACT_EDIT_ITEM);
	}
	public void setButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				switch(getIntent().getIntExtra("Action", ACTION_DEFAULT)) {
				case ACTION_DEFAULT:
					startEditInputActivity(mCard.getID(), getEditCardClass(mCard.getType()));
					break;
				case ACTION_NOTIFICATION_INTO:
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
					break;
				}
				
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