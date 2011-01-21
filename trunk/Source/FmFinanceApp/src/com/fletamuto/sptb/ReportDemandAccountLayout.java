package com.fletamuto.sptb;

import java.util.ArrayList;
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
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.view.FmBaseLayout;

public class ReportDemandAccountLayout extends FmBaseActivity {
	public static final int VIEW_NORMAL = 0;
	public static final int VIEW_TWO_LINE = 1;
	
	protected ArrayList<AccountItem> mArrAccount = null;
	protected ArrayList<AccountItem> mListItems = new ArrayList<AccountItem>();
	protected ReportItemAdapter mItemAdapter = null;
	
	private int mSelectedSection = 0;
	private int mSelectedCategoryID = -1;
	protected Map<Integer, CategoryAmount> mCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ReportSeparationAdapter mSeparationAdapter = null;
	
	protected ListView mSeparationList;
	
	private int mViewMode = VIEW_TWO_LINE; 
	
	
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
  		updateListItem(DBMgr.getAccountAllItems());
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
        
        setTitle("요구불");
        
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
		Intent intent = new Intent(this, InputAccountLayout.class);
		startActivity(intent);
	}
	

	
	protected void getData() {
		if (mSeparationAdapter == null) return;		
		CategoryAmount categoryAmount = mSeparationAdapter.getItem(mSelectedSection);
//		mCategoryID = categoryAmount.getCategoryID();
//		mSelectedCategoryID = mCategoryID;
		
		((TextView)findViewById(R.id.TVListTitleRight)).setText(String.format("총 금액 %,d원", categoryAmount.getTotalAmount()));
		
		
		getItemsFromDB();
		
		mListItems.clear();
		updateListItem();
	}
	
	protected void updateListItem() {
		int itemSize = mArrAccount.size();
//		mTotalAmount = 0L;
		
		for (int index = 0; index < itemSize; index++) {
			AccountItem item = mArrAccount.get(index);
			mListItems.add(item);
		}
	}
	
	protected void updateListItem(ArrayList<AccountItem> arrItems) {
		mCategoryItems.clear();
		
		int itemSize = arrItems.size();
		for (int index = 0; index < itemSize; index++) {
			AccountItem item = arrItems.get(index);
		
			
			Integer accountType = item.getType();
			CategoryAmount categoryAmount = mCategoryItems.get(accountType);
			
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(item.getType());
				categoryAmount.set(accountType, item.getTypeName(), item.getBalance());
				mCategoryItems.put(accountType, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getBalance());
			}
		}
	}
	
	protected void setAdapterList() {
        
    	final ListView listItem = (ListView)findViewById(R.id.LVCurrentList);
    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_account, mListItems);
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				onClickListItem(parent, view, position, id);
			}
		});
    }
	
//	
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		mLatestSelectPosition = position;
//    }
//	
	protected void startEditInputActivity(Class<?> cls, int itemId) {
		Intent intent = new Intent(ReportDemandAccountLayout.this, cls);
    	intent.putExtra("EDIT_ITEM_ID", itemId);
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_EDIT_ITEM);
	}
	
	protected void getItemsFromDB() {
		
		mArrAccount = DBMgr.getAccountAllItems();
	
		int size = mArrAccount.size();
		for (int index = 0; index < size; index++) {
			AccountItem account = mArrAccount.get(index); 
			
			if (account.getType() == AccountItem.TIME_DEPOSIT || account.getType() == AccountItem.SAVINGS) {
				continue;
			}
			
			mListItems.add(account);
		}
    }
	

	public class ReportItemAdapter extends ArrayAdapter<AccountItem> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportItemAdapter(Context context, int resource,
				 List<AccountItem> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			AccountItem item = (AccountItem)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			setListViewText(item, convertView);			
			
			return convertView;
		}
		

		
		public View createSeparator(LayoutInflater inflater, ViewGroup parent, AccountItem item) {
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
	
	protected void setListViewText(AccountItem account, View convertView) {
		
		((TextView)convertView.findViewById(R.id.TVAccountReportListNumer)).setText("번호 : " + account.getNumber());			
		((TextView)convertView.findViewById(R.id.TVAccountReportListBalance)).setText(String.format("잔액 : %,d원", account.getBalance()));
		((TextView)convertView.findViewById(R.id.TVAccountReportListType)).setText("종류 : " + account.getTypeName());
		((TextView)convertView.findViewById(R.id.TVAccountReportListInstitution)).setText("기관 : " + account.getCompany().getName());
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