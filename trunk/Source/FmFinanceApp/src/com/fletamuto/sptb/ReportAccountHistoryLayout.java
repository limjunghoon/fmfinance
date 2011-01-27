package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
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

import com.fletamuto.sptb.MainIncomeAndExpenseLayout.DailyItem;
import com.fletamuto.sptb.MainIncomeAndExpenseLayout.ReportDailyItemAdapter;
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
//    
//	@Override
//	protected void onClickListItem(AdapterView<?> parent, View view,
//			int position, long id) {
//   // 	FinanceItem item = (FinanceItem)mItemAdapter.getItem(position);
//    	//startEditInputActivity(InputAssetsLayout.class, item.getID());
//	}

//    
//    protected void setListViewText(FinanceItem financeItem, View convertView) {
//    	AssetsItem item = (AssetsItem)financeItem;
//    	
//    	if (mItem.getExtendType() == AssetsStockItem.EXEND_TYPE) {
//    		setListViewTextStock(item, convertView);
//		}
//    	else {
//    		//((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setText("제목 : " + item.getTitle());
//        	((TextView)convertView.findViewById(R.id.TVAssetsReportListTitle)).setVisibility(View.GONE);
//    		((TextView)convertView.findViewById(R.id.TVAssetsReportListDate)).setText("날짜 : " + item.getCreateDateString());			
//    		((TextView)convertView.findViewById(R.id.TVAssetsReportListAmount)).setText(String.format("금액 : %,d원", item.getAmount()));
//    		((TextView)convertView.findViewById(R.id.TVAssetsReportListCategory)).setVisibility(View.GONE);
//    		
//    		if (item.getMemo() == null) {
//    			((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setVisibility(View.GONE);
//    		}
//    		else {
//    			((TextView)convertView.findViewById(R.id.TVAssetsReportListMemo)).setText(String.format("메모 : %s", item.getMemo()));
//    		}
//    		
//    		((TextView)convertView.findViewById(R.id.TVAssetsReportListRevenue)).setText(String.format("수익율 : %s", Revenue.getString(mPurchasePrice, item.getAmount())));
//    	}
//		
//    	
//	}
//    
//    protected void setListViewTextStock(AssetsItem stock, View convertView) {
//    	TextView tvChangeDate = (TextView) convertView.findViewById(R.id.TVAssetsStockChageDate);
//    	tvChangeDate.setVisibility(View.VISIBLE);
//    	tvChangeDate.setText("날짜 : " + stock.getCreateDateString());
//    	TextView tvTitle = ((TextView)convertView.findViewById(R.id.TVAssetsStockTitle)); 
//    	tvTitle.setText((stock.getState() == AssetsStockItem.SELL) ? "매도" : "매수");
//    	tvTitle.setTextColor((stock.getState() == AssetsStockItem.SELL) ? Color.BLUE : Color.RED);
//		((TextView)convertView.findViewById(R.id.TVAssetsStockAmount)).setText(String.format("금액 :  %,d원", stock.getTotalAmount()));
//		((TextView)convertView.findViewById(R.id.TVAssetsStockMeanPrice)).setText(String.format("주당 금액 : %,d원", stock.getAmount()));
//		((TextView)convertView.findViewById(R.id.TVAssetsStockCount)).setText("수량 : " + stock.getCount() + "주");
//		((TextView)convertView.findViewById(R.id.TVAssetsStockMemo)).setText("메모 : " + stock.getMemo());
//		((TextView)convertView.findViewById(R.id.TVAssetsStockCurrentPrice)).setVisibility(View.GONE);
//		((TextView)convertView.findViewById(R.id.TVAssetsStockRevenue)).setVisibility(View.GONE);
//    }
//    
//    protected void setDeleteBtnListener(View convertView, int itemId, int position) {
//    	Button btnDelete = (Button)convertView.findViewById(R.id.BtnReportAssetsDelete);
//		btnDelete.setTag(R.id.delete_id, new Integer(itemId));
//		btnDelete.setTag(R.id.delete_position, new Integer(position));
//		btnDelete.setOnClickListener(deleteBtnListener);
//		btnDelete.setVisibility(View.GONE);
//    }
//    
//    protected void getData() {
//    	mItems = DBMgr.getAssetsStateItems(mItem.getID());
//		
//		mListItems.clear();
//		updateListItem();
//	}
//
//	@Override
//	protected int getItemType() {
//		// TODO Auto-generated method stub
//		return AssetsItem.TYPE;
//	}
//
//	@Override
//	protected int getAdapterResource() {
//		if (mItem.getExtendType() == AssetsStockItem.EXEND_TYPE) {
//			return R.layout.report_list_assets_stock;
//		}
//		else {
//			return R.layout.report_list_assets;
//		}
//		
//	}
//
//	protected void updateListItem() {
//		mListItems =mItems;
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	protected void onClickAddButton() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	protected int getLayoutResources(FinanceItem item) {
//		return getAdapterResource();
//	}
}