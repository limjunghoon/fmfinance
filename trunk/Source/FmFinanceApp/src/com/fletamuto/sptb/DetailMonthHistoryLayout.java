package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.CardPayment;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.TransferItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class DetailMonthHistoryLayout extends DetailBaseLayout {
	private static final int MOVE_SENSITIVITY = ItemDef.MOVE_SENSITIVITY;
	
	protected Calendar mCalendar = Calendar.getInstance();
	protected ReportAccountHistoryAdapter mAccountHistoryAdapter = null;
	
	protected AccountDailyItems mMonthlyItems[] = new AccountDailyItems[LAST_DAY_OF_MONTH];
	
	public abstract ArrayList<FinanceItem> getExpenseItem();
	public abstract ArrayList<FinanceItem> getIncomeItem();
	
	private float mTouchMove;
	private boolean mTouchMoveFlag = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    protected void onResume() {
    	updateMonthlyItems();
        setAccountHistoryList();
 //       updateChildView();
        
    	super.onResume();
    }
    
    
    @Override
	protected void setTitleBtn() {
		super.setTitleBtn();
	    
		updateCurrentDateText();
	}
    /** 현재 날짜 갱신 */
    protected void updateCurrentDateText() {
    	TextView tvCurrentMonth = (TextView)findViewById(R.id.TVCurrentMonth);
		tvCurrentMonth.setText(String.format("%d년 %d월", mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1));
	}
	
	protected void updateMonthlyItems() {
		clearMonthlyItems();
		
		ArrayList<FinanceItem> expenseItems = getExpenseItem();
    	ArrayList<FinanceItem> incomeItems = getIncomeItem();
    	
		int incomeItemSize = incomeItems.size();
		for (int index = 0; index < incomeItemSize; index++) {
			addMonthlyItem(incomeItems.get(index));
		}
		
		int expenseItemSize = expenseItems.size();
		for (int index = 0; index < expenseItemSize; index++) {
			addMonthlyItem(expenseItems.get(index));
		}
	}
	 
	protected void addMonthlyItem(FinanceItem item) {
		int day = item.getCreateDate().get(Calendar.DAY_OF_MONTH);
		
		if (mMonthlyItems[day] == null) {
			mMonthlyItems[day] = new AccountDailyItems(item.getCreateDate());
		}
		
		if (item.getType() == IncomeItem.TYPE) {
			AccountDailyItem dailyItem = new AccountDailyItem(STATE_INCOME, 
					item.getCategory().getName(), item.getMemo(), item.getTotalAmount());
			dailyItem.setTag(item);
			mMonthlyItems[day].add(dailyItem);
		}
		else if (item.getType() == ExpenseItem.TYPE) {
			AccountDailyItem dailyItem = new AccountDailyItem(STATE_EXPENSE, 
					item.getCategory().getName(), item.getMemo(), item.getTotalAmount());
			dailyItem.setTag(item);
			mMonthlyItems[day].add(dailyItem);
		}
	}
	    
	protected void clearMonthlyItems() {
		for (int daily = 0; daily < LAST_DAY_OF_MONTH; daily++) {
			mMonthlyItems[daily] = null;
		}
	}
		
    protected List<AccountDailyItem> getListItems() {
    	ArrayList<AccountDailyItem> listItems = new ArrayList<AccountDailyItem>();
    	for (int index = LAST_DAY_OF_MONTH-1; index >= 0; index--) {
    		if (mMonthlyItems[index] != null) {
    			ArrayList<AccountDailyItem> dailyItems = mMonthlyItems[index].mItems;
    			
    			AccountDailyItem title = new AccountDailyItem(-1);
    			title.setComment(String.format("%s일", mMonthlyItems[index].getDay()));
    			listItems.add(title);
    			
    			int size = dailyItems.size();
    			for (int dailyIndex = 0; dailyIndex < size; dailyIndex++) {
    				listItems.add(dailyItems.get(dailyIndex));
    			}
    		}
    	}
		return listItems;
	}
		
	protected void setAccountHistoryList() {
    	final ListView listIncome = (ListView)findViewById(R.id.LVCurrentList);
    	mAccountHistoryAdapter = new ReportAccountHistoryAdapter(this, R.layout.report_list_normal, getListItems());
    	listIncome.setAdapter(mAccountHistoryAdapter);
    	
    	listIncome.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onClickListItem(view, position, id);
				
			}
		});
    	
    	listIncome.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				setMoveViewMotionEvent(event);
		    	return false;
			}
		});
	}
		
	protected void onClickListItem(View view, int position, long id) {
		AccountDailyItem dailyItem = mAccountHistoryAdapter.getItem(position);
		Object object = dailyItem.getTag();
		if (object instanceof IncomeItem)  {
			Intent intent = new Intent(this, InputIncomeLayout.class);
	    	intent.putExtra("EDIT_ITEM_ID", ((IncomeItem)object).getID());
	    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
		}
		else if (object instanceof ExpenseItem)  {
			Intent intent = new Intent(this, InputExpenseLayout.class);
	    	intent.putExtra("EDIT_ITEM_ID", ((ExpenseItem)object).getID());
	    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
		}
		else if (object instanceof TransferItem) {
			Intent intent = new Intent(this, TransferAccountLayout.class);
	    	intent.putExtra("EDIT_ITEM_ID", ((TransferItem)object).getID());
	    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
		}
	}
	
	public void setMoveViewMotionEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		mTouchMove = event.getX();
    		mTouchMoveFlag = true;
    	}
    	else if (event.getAction() == MotionEvent.ACTION_MOVE && mTouchMoveFlag == true) {
    		
    		if (mTouchMove-event.getX()< -MOVE_SENSITIVITY) {
    			mTouchMoveFlag = false;
    			moveCurrentDate(1);
    		}
    		if (mTouchMove-event.getX()> MOVE_SENSITIVITY) {
    			mTouchMoveFlag = false;
    			moveCurrentDate(-1);
    		}
    	}
    }
	
	protected void moveCurrentDate(int dayValue) {
		mCalendar.add(Calendar.MONTH, dayValue);
		updateMonthlyItems();
        setAccountHistoryList();
        updateCurrentDateText();
	}
		
	public class AccountDailyItems {
    	private Calendar mCalendar;
    	ArrayList<AccountDailyItem> mItems;
    	
    	public AccountDailyItems(Calendar calendar) {
			mCalendar = calendar;
		}
    	
    	public void add(AccountDailyItem item) {
    		if (mItems == null) {
    			mItems = new ArrayList<AccountDailyItem>();
    		}
    		
    		mItems.add(item);
    	}
    	
    	public int getDay() {
			return mCalendar.get(Calendar.DAY_OF_MONTH);
		}
    
    	public Calendar getDate() {
			return mCalendar;
		}
    	
    }
	
	public class AccountDailyItem {
    	private int mState;
    	private String mComment;
    	private String mMemo;
    	private Object mTag;
		private long mAmount;
		
		public AccountDailyItem(int state) {
			mState = state;
		}
		
		public AccountDailyItem(int state, String comment, String memo, long amount) {
			mState = state;
			mComment = comment;
			mMemo = memo;
			mAmount = amount;
		}

		public void setState(int state) {
			mState = state;
		}

		public int getState() {
			return mState;
		}

		public void setComment(String comment) {
			mComment = comment;
		}

		public String getComment() {
			return mComment;
		}

		public void setMemo(String memo) {
			mMemo = memo;
		}

		public String getMemo() {
			return mMemo;
		}

		public void setTag(Object tag) {
			mTag = tag;
		}

		public Object getTag() {
			return mTag;
		}
		
		public String getStateText() {
			String msg = "";
			
			if (STATE_CREATE == mState) {
				msg = "생성";
			}
			else if (STATE_EDIT == mState) {
				msg = "수정";
			}
			else if (STATE_EXPENSE == mState) {
				msg = "지출";
			}
			else if (STATE_INCOME == mState) {
				msg = "수입";
			}
			else if (STATE_TRANSFOR_WITHDRAWAL == mState) {
				msg = "출금";
			}
			else if (STATE_TRANSFOR_DEPOSIT == mState) {
				msg = "입금";
			}
			else if (STATE_SETTLEMENT == mState) {
				msg = "결제";
			}
			
			return msg;
		}

		public void setAmount(long amount) {
			this.mAmount = amount;
		}

		public long getAmount() {
			return mAmount;
		}
	}
	
	public class ReportAccountHistoryAdapter extends ArrayAdapter<AccountDailyItem> {
    	int mResource;
    	private LayoutInflater mInflater;

		public ReportAccountHistoryAdapter(Context context, int resource,
				 List<AccountDailyItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AccountDailyItem item = (AccountDailyItem)getItem(position);
			
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
			
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			
			if (item.getState() == -1) {
				viewHolder.getLeftTextView().setText(item.getComment());
				viewHolder.getLeftTextView().setTextColor(Color.MAGENTA);
			}
			else {
				viewHolder.getLeftTextView().setText(item.getStateText());
				viewHolder.getRightTopTextView().setText(item.getComment());
				viewHolder.getCenterBottomTextView().setText(item.getMemo());
				if (item.getState() == STATE_EXPENSE || item.getState() == STATE_TRANSFOR_WITHDRAWAL ||
					item.getState() == STATE_SETTLEMENT) {
					viewHolder.getRightBottomTextView().setText(String.format("%,d원", -item.getAmount()));
					viewHolder.getRightBottomTextView().setTextColor(Color.RED);
				}
				else {
					viewHolder.getRightBottomTextView().setText(String.format("%,d원", item.getAmount()));
					viewHolder.getRightBottomTextView().setTextColor(Color.BLUE);
				}
			}
			
			return convertView;
		}
    }
	
	
	protected void updateMonthlyItem(CardPayment paymentItem) {
		int day = paymentItem.getPaymentDate().get(Calendar.DAY_OF_MONTH);
		
		CardItem card = DBMgr.getCardItem(paymentItem.getCardId());
		if (card == null) {
			return;
		}
		
		if (mMonthlyItems[day] == null) {
			mMonthlyItems[day] = new AccountDailyItems(paymentItem.getPaymentDate());
		}
		
		AccountDailyItem dailyItem = new AccountDailyItem(STATE_SETTLEMENT, 
				card.getCompenyName().getName(), card.getName(), paymentItem.getPaymentAmount());
		
		dailyItem.setTag(paymentItem);
		mMonthlyItems[day].add(dailyItem);
	}
	
	protected void updateMonthlyItem(TransferItem trans, int state) {
		int day = trans.getOccurrentceDate().get(Calendar.DAY_OF_MONTH);
		
		if (mMonthlyItems[day] == null) {
			mMonthlyItems[day] = new AccountDailyItems(trans.getOccurrentceDate());
		}
		
		AccountDailyItem dailyItem = new AccountDailyItem(state, 
				trans.getToAccount().getCompany().getName(), trans.getMemo(), trans.getAmount());
		
		dailyItem.setTag(trans);
		mMonthlyItems[day].add(dailyItem);
	}
	
//	protected void updateMonthlyItems(ArrayList<TransferItem> fromItems) {
//		int fromItemSize = fromItems.size();
//		for (int index = 0; index < fromItemSize; index++) {
//			TransferItem trans = fromItems.get(index);
//			int day = trans.getOccurrentceDate().get(Calendar.DAY_OF_MONTH);
//			
//			if (mMonthlyItems[day] == null) {
//				mMonthlyItems[day] = new AccountDailyItems(trans.getOccurrentceDate());
//			}
//			
//			AccountDailyItem dailyItem = new AccountDailyItem(STATE_TRANSFOR_WITHDRAWAL, 
//					trans.getToAccount().getCompany().getName(), trans.getMemo(), trans.getAmount());
//			
//			dailyItem.setTag(trans);
//			mMonthlyItems[day].add(dailyItem);
//		}
//	}
	
	
}