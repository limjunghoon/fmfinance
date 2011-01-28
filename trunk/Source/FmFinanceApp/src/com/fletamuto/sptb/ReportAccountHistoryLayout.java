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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ViewHolder;
import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.db.DBMgr;

public class ReportAccountHistoryLayout extends ReportBaseHistoryLayout {
	private static final int LAST_DAY_OF_MONTH = ItemDef.LAST_DAY_OF_MONTH;
	public static final int STATE_CREATE = 0;
	public static final int STATE_EDIT = 1;
	public static final int STATE_EXPENSE = 2;
	public static final int STATE_INCOME = 3;
	public static final int STATE_TRANSFOR = 4;
	public static final int STATE_SETTLEMENT = 5;
	
    private AccountItem mAccount;
    protected ReportAccountHistoryAdapter mAccountHistoryAdapter = null;
    
    protected AccountDailyItems mMonthlyItems[] = new AccountDailyItems[LAST_DAY_OF_MONTH];
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
    
    @Override
    protected void onResume() {
    	updateMonthlyItems();
        setAccountHistoryList();
        updateChildView();
        
    	super.onResume();
    }
    
    @Override
    public void initialize() {
    	super.initialize();
    	
    	mAccount = (AccountItem) getIntent().getSerializableExtra(MsgDef.ExtraNames.ACCOUNT_ITEM);
    	setVisibleTitle();
    	
    }


	protected void updateChildView() {
		Button btnBalance = (Button)findViewById(R.id.BtnTitle);
    	btnBalance.setText(String.format("잔액 %,d원", mAccount.getBalance()));
    	btnBalance.setVisibility(View.VISIBLE);
	}
    
    @Override
    protected void setTitleBtn() {
    	super.setTitleBtn();
    	
    	setTitle(mAccount.getCompany().getName() + " 계좌 내역");
    	findViewById(R.id.BtnTitle).setVisibility(View.VISIBLE);
  
    	Button btnTrans = (Button)findViewById(R.id.BtnFunction);
    	btnTrans.setText("이체");
    	
    	btnTrans.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ReportAccountHistoryLayout.this, TransferAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_ITEM, mAccount);
				startActivityForResult(intent, MsgDef.ActRequest.ACT_TRANFER_ACCOUNT);
			}
		});
    }
    
	protected void setAccountHistoryList() {
    	final ListView listIncome = (ListView)findViewById(R.id.LVCurrentList);
    	mAccountHistoryAdapter = new ReportAccountHistoryAdapter(this, R.layout.report_list_normal, getListItems());
    	listIncome.setAdapter(mAccountHistoryAdapter);
    	
    	listIncome.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
	//			setVisibleDeleteButton((Button)view.findViewById(R.id.BtnCategoryDelete));
				
			}
		});
    	
    	listIncome.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
		//		setMoveViewMotionEvent(event);
		    	return false;
			}
		});
	}
	
	@Override
	public void onEditBtnClick() {
    	Intent intent = new Intent(this, InputAccountLayout.class);
    	intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mAccount.getID());
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
//		intent.putExtra(MsgDef.ExtraNames.GET_ACCOUNT_ITEMS, mAccountListItems);
//		startActivityForResult(intent, ACT_EDIT_ACCOUNT);
	}
    
    protected List<AccountDailyItem> getListItems() {
    	ArrayList<AccountDailyItem> listItems = new ArrayList<AccountDailyItem>();
    	for (int index = 0; index < LAST_DAY_OF_MONTH; index++) {
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
			else if (STATE_TRANSFOR == mState) {
				msg = "이체";
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
    
    protected void updateMonthlyItems() {
		clearMonthlyItems();
		
		ArrayList<FinanceItem> expenseItems = DBMgr.getExpenseItemFromAccount(mAccount.getID(), getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH)+1);
    	ArrayList<FinanceItem> incomeItems = DBMgr.getIncomeItemFromAccount(mAccount.getID(), getCalendar().get(Calendar.YEAR), getCalendar().get(Calendar.MONTH)+1);
    	
		
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
			mMonthlyItems[day].add(dailyItem);
		}
		else if (item.getType() == ExpenseItem.TYPE) {
			AccountDailyItem dailyItem = new AccountDailyItem(STATE_EXPENSE, 
					item.getCategory().getName(), item.getMemo(), item.getTotalAmount());
			mMonthlyItems[day].add(dailyItem);
		}
		
	}
    
	protected void clearMonthlyItems() {
		for (int daily = 0; daily < LAST_DAY_OF_MONTH; daily++) {
			mMonthlyItems[daily] = null;
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
				if (item.getState() == STATE_EXPENSE) {
					viewHolder.getRightBottomTextView().setText(String.format("%,d원", -item.getAmount()));
					viewHolder.getRightBottomTextView().setTextColor(Color.RED);
				}
				else {
					viewHolder.getRightBottomTextView().setText(String.format("%,d원", item.getAmount()));
					viewHolder.getRightBottomTextView().setTextColor(Color.BLUE);
				}
			}
			
//			TextView tvDaily = (TextView)convertView.findViewById(R.id.TVDaily);
//			tvDaily.setText(String.format("%d일", item.getDay()));
			
//			setListViewText(item, convertView);
			return convertView;
		}
    }
}