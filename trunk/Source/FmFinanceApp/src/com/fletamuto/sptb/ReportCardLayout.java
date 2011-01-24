package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardExpenseInfo;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

public class ReportCardLayout extends FmBaseActivity {
	public static final int VIEW_NORMAL = 0;
	public static final int VIEW_TWO_LINE = 1;
	
	protected ArrayList<CardItem> mArrCard = null;
	protected ArrayList<CardItem> mListItems = new ArrayList<CardItem>();
	private ArrayList<CardExpenseInfo> mArrCardExpenseInfo = new ArrayList<CardExpenseInfo>();
	protected ReportItemAdapter mItemAdapter = null;
	
	private int mSelectedSection = 0;
	private int mSelectedCategoryID = -1;
	protected Map<Integer, CategoryAmount> mCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ReportSeparationAdapter mSeparationAdapter = null;
	
	protected ListView mSeparationList;
	
	private int mViewMode = VIEW_TWO_LINE; 
	
	private int mSelectedType = -1;
	private Calendar mCurrentCalendar = Calendar.getInstance();
	private long mTatalExpenseAmount = 0L;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_base, true);
       
    }
	
	@Override
	protected void onResume() {
		getSeparationData();
		setSeparationAdapterList();
	
		setSelectedPosition();
		
		getData();
        setAdapterList();
        updateChildView();
        
		super.onResume();
	}
	
	  @Override
	public void initialize() {
		mSeparationList = (ListView) findViewById(R.id.LVSeparation);
    	mSelectedCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_ID, -1);
    	
		if (mViewMode == VIEW_TWO_LINE) {
			findViewById(R.id.LVSeparation).setVisibility(View.VISIBLE);
			findViewById(R.id.LLListTitle).setVisibility(View.VISIBLE);
		}
	}
	  
  	protected void getSeparationData() {
		//updateListItem(DBMgr.getAllItems(getItemType()));
  		updateListItem(DBMgr.getCardItems());
	}
  	
  	protected void setSeparationAdapterList() {
		if (mSeparationList == null) return;
    	
    	mSeparationAdapter = new ReportSeparationAdapter(this, R.layout.report_list_separation, getListItems());
    	mSeparationList.setAdapter(mSeparationAdapter);
    	
    	mSeparationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onClickSectionListItem(parent, view, position, id);
			}
		});
    }
  	
    protected void onClickSectionListItem(AdapterView<?> parent, View view,
			int position, long id) {
		
    	chageSectionView(position);
	}
    
	protected void chageSectionView(int position) {
		mSelectedSection = position;
		getData();
        setAdapterList();
        updateChildView();
        
        mSeparationAdapter.notifyDataSetInvalidated();
//        mSeparationList.invalidate();
	}
  	
	protected ArrayList<CategoryAmount> getListItems() {
    	ArrayList<CategoryAmount> listItems = new ArrayList<CategoryAmount>();
    	Collection<CategoryAmount> categoryAmountItems = mCategoryItems.values();
    	
    	for (CategoryAmount iterator:categoryAmountItems) {
			listItems.add(iterator);
		}
    	
    	return listItems;
    }
	  
	protected void setSelectedPosition() {
		if (mSeparationAdapter != null) {
        	int size = mSeparationAdapter.getCount();
        	for (int index = 0; index < size; index++) {
        		CategoryAmount categoryAmount = mSeparationAdapter.getItem(index);
        		if (categoryAmount.getCategoryID() == mSelectedCategoryID) {
        			mSelectedSection = index;
        		}
        	}
        }
	}
	
	protected void setViewMode(int viewMode) {
		mViewMode = viewMode;
	}
	
	@Override
	protected void setTitleBtn() {
		setTitleBtnText(FmBaseLayout.BTN_RIGTH_01, "추가");
        setAddButtonListener();
        setTitleBtnVisibility(FmBaseLayout.BTN_RIGTH_01, View.VISIBLE);
        
        setTitle("카드");
        
		super.setTitleBtn();
	}
	
	
	public void setAddButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				onClickAddButton();
			}
		});
	}
	
	protected void onClickAddButton() {
		Intent intent = new Intent(this, SelectCardLayout.class);
		startActivity(intent);
	}
	

	
	protected void getData() {
		if (mSeparationAdapter == null) return;		
		CategoryAmount categoryAmount = mSeparationAdapter.getItem(mSelectedSection);
		mSelectedType = categoryAmount.getCategoryID();
//		mSelectedCategoryID = mCategoryID;
		
		((TextView)findViewById(R.id.TVListTitleRight)).setText(String.format("총 금액 %,d원", categoryAmount.getTotalAmount()));
		
		
		getItemsFromDB();
		
		mListItems.clear();
		updateListItem();
	}
	
	protected void updateListItem() {
		int itemSize = mArrCard.size();
//		mTotalAmount = 0L;
		
		for (int index = 0; index < itemSize; index++) {
			CardItem item = mArrCard.get(index);
			mListItems.add(item);
		}
	}
	
	protected void updateListItem(ArrayList<CardItem> arrItems) {
		mCategoryItems.clear();
		
		int itemSize = arrItems.size();
		for (int index = 0; index < itemSize; index++) {
			CardItem item = arrItems.get(index);
		
			
			Integer cardType = item.getType();
			CategoryAmount categoryAmount = mCategoryItems.get(cardType);
			
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(item.getType());
				categoryAmount.set(cardType, item.getCardTypeName(), item.getBalance());
				mCategoryItems.put(cardType, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getBalance());
			}
		}
	}
	
	protected void setAdapterList() {
        
    	final ListView listItem = (ListView)findViewById(R.id.LVCurrentList);
    	mItemAdapter = new ReportItemAdapter(this, getCardListResource(), mArrCardExpenseInfo);
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CardExpenseInfo item = (CardExpenseInfo)mArrCardExpenseInfo.get(position);
				startDetailInputActivity(item, getDetailCardClass(item.getCard().getType()));
			}
		});
    }
	
	protected void startDetailInputActivity(CardExpenseInfo item,
			Class<?> cls) {
		if (cls == null) return;
		
		Intent intent = new Intent(this, cls);
    	intent.putExtra(MsgDef.ExtraNames.CARD_EXPENSE_INFO_ITEM, item);
    	intent.putExtra(MsgDef.ExtraNames.CALENDAR_MONTH, mCurrentCalendar.get(Calendar.MONTH) + 1);
    	intent.putExtra(MsgDef.ExtraNames.CALENDAR_YEAR, mCurrentCalendar.get(Calendar.YEAR));
    	startActivity(intent);
	}
	
	protected Class<?> getDetailCardClass(int type) {
		if (type == CardItem.CREDIT_CARD) {
			return CardDetailCreditLayout.class;
		}
		else if (type == CardItem.CHECK_CARD) {
			return CardDetailCheckLayout.class;
		}
		else if (type == CardItem.PREPAID_CARD) {
			return CardDetailPrepaidLayout.class;
		}
		return null;
	}
	
 

	//	
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		mLatestSelectPosition = position;
//    }
//	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(ReportCardLayout.this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	protected int getCardListResource() {
		
		if (mSelectedType == CardItem.CREDIT_CARD) {
			return R.layout.report_list_credit_card;
		}
		else if (mSelectedType == CardItem.CHECK_CARD) {
			return R.layout.report_list_check_card;
		}
		else if (mSelectedType == CardItem.PREPAID_CARD) {
			return R.layout.report_list_prepaid_card;
		}
		
		return R.layout.report_list_credit_card;
	}
	
	protected void getItemsFromDB() {
		mArrCardExpenseInfo.clear();
		mArrCard = DBMgr.getCardItems(mSelectedType);
		
		mTatalExpenseAmount = 0L;
		int size = mArrCard.size();
		for (int index = 0; index < size; index++) {
			CardItem card = mArrCard.get(index);
			if (card.getAccount().getID() != -1) {
				card.setAccount(DBMgr.getAccountItem(card.getAccount().getID()));
			}
			CardExpenseInfo cardInfo = new CardExpenseInfo(card);
			long totalExpenseAmount = DBMgr.getCardTotalExpense(mCurrentCalendar.get(Calendar.YEAR), mCurrentCalendar.get(Calendar.MONTH)+1, cardInfo.getCard().getID());
			long billingExpenseAmout = DBMgr.getCardTotalExpense(card.getID(), card.getStartBillingPeriod(Calendar.getInstance()), card.getEndBillingPeriod(Calendar.getInstance()));
			long billingNextExpenseAmout = DBMgr.getCardTotalExpense(card.getID(), card.getNextStartBillingPeriod(Calendar.getInstance()), card.getNextEndBillingPeriod(Calendar.getInstance()));
			cardInfo.setTotalExpenseAmount(totalExpenseAmount);
			cardInfo.setBillingExpenseAmount(billingExpenseAmout);
			cardInfo.setNextBillingExpenseAmount(billingNextExpenseAmout);
			mTatalExpenseAmount += totalExpenseAmount;
			mArrCardExpenseInfo.add(cardInfo);
		}
    }
	

	public class ReportItemAdapter extends ArrayAdapter<CardExpenseInfo> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportItemAdapter(Context context, int resource,
				 List<CardExpenseInfo> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			CardExpenseInfo item = (CardExpenseInfo)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			setListViewText(item, convertView);			
			
			return convertView;
		}
		

		
		public View createSeparator(LayoutInflater inflater, ViewGroup parent, CardItem item) {
			View convertView = inflater.inflate(R.layout.list_separators, parent, false);
			TextView tvTitle = (TextView)convertView.findViewById(R.id.TVSeparator);
			tvTitle.setText(item.getSeparatorTitle());
			tvTitle.setTextColor(Color.BLACK);
			convertView.setBackgroundColor(Color.WHITE);
			return convertView;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !mListItems.get(position).isSeparator();
		}
    }
	
	protected void setListViewText(CardExpenseInfo cardInfo, View convertView) {
		CardItem card = cardInfo.getCard();
		
		if (card.getType() == CardItem.CREDIT_CARD) {
			((TextView)convertView.findViewById(R.id.TVCreditCardName)).setText(card.getCompenyName().getName());
			((TextView)convertView.findViewById(R.id.TVCreditCardType)).setText(getCardTypeName(card.getType()));
			((TextView)convertView.findViewById(R.id.TVCreditCardTotalExpeanseAmount)).setText(String.format("총 지출 금액  : %,d 원", cardInfo.getTotalExpenseAmount()));
			((TextView)convertView.findViewById(R.id.TVCreditCardExpectAmount)).setText(String.format("%d일 결제 예정금액 : %,d 원",card.getSettlementDay(), cardInfo.getBillingExpenseAmount()));
		}
		else if (card.getType() == CardItem.CHECK_CARD) {
			((TextView)convertView.findViewById(R.id.TVCheckCardName)).setText(card.getCompenyName().getName());
			((TextView)convertView.findViewById(R.id.TVCheckCardType)).setText(getCardTypeName(card.getType()));
			((TextView)convertView.findViewById(R.id.TVCheckCardTotalExpeanseAmount)).setText(String.format("총 지출 금액  : %,d 원", cardInfo.getTotalExpenseAmount()));
			((TextView)convertView.findViewById(R.id.TVCheckCardAccount)).setText(String.format("계좌 잔액  : %,d 원", card.getAccount().getBalance()));
		}
		else if (card.getType() == CardItem.PREPAID_CARD) {
			((TextView)convertView.findViewById(R.id.TVPrepaidCardName)).setText(card.getCompenyName().getName());
			((TextView)convertView.findViewById(R.id.TVPrepaidCardType)).setText(getCardTypeName(card.getType()));
			((TextView)convertView.findViewById(R.id.TVPrepaidCardTotalExpeanseAmount)).setText(String.format("총 지출 금액  : %,d 원", cardInfo.getTotalExpenseAmount()));
			((TextView)convertView.findViewById(R.id.TVPrepaidRemainAmount)).setText(String.format("남은 금액  : %,d 원", card.getBalance() - cardInfo.getTotalExpenseAmount()));
			setBudgetPorgress(convertView, cardInfo);
		}
	}
	
	private CharSequence getCardTypeName(int type) {
		if (type == CardItem.CREDIT_CARD) {
			return  "신용카드";
		}
		else if (type == CardItem.CHECK_CARD) {
			return  "체크카드";
		}
		else if (type == CardItem.PREPAID_CARD) {
			return "선불카드";
		}
		
		return "";
	}
	
	private void setBudgetPorgress(View convertView, CardExpenseInfo cardInfo) {
		ProgressBar progress = (ProgressBar)convertView.findViewById(R.id.PBPrepaidCardExpense);
		long maxBalance = cardInfo.getCard().getBalance();
		long totalExpenseAmount = cardInfo.getTotalExpenseAmount();
		long sumAmount = maxBalance - totalExpenseAmount;
		
		
		if (sumAmount < 0) {
			progress.setMax(100);
			progress.setProgress(5);
		}
		else {
			// 테스트 코드
			int max = (int)(maxBalance/100);
			int pos = max - (int)(totalExpenseAmount/100);
			
			progress.setMax(max);
			progress.setProgress(pos);
		}
		
	}


	
	protected void updateChildView() {
		
	}
	
	public class ReportSeparationAdapter extends ArrayAdapter<CategoryAmount> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportSeparationAdapter(Context context, int resource,
				 List<CategoryAmount> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CategoryAmount category = (CategoryAmount)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			TextView tvSection = (TextView) convertView.findViewById(R.id.TVSeparator);
			tvSection.setText(category.getName());
			
			TextView tvCount = (TextView) convertView.findViewById(R.id.TVSeparatorCount);
			tvCount.setText(String.format("(%d)", category.getCount()));
			
			convertView.setBackgroundColor((category.getCategoryID() == mSelectedCategoryID) ? Color.MAGENTA : Color.BLACK);
			
			return convertView;
		}
		
		
    }
	
}