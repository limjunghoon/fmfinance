package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ItemDef;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;

public class ReportInputAssetsCategoryLayout extends FmBaseActivity {
	public static final int VIEW_NORMAL = 0;
	public static final int VIEW_TWO_LINE = 1;
	
	private static final int SELECT_ASSETS = 0;
	private static final int SELECT_LIABILITY = 1;
	private static final int SELECT_CARD = 2;
	
	protected ArrayList<Category> mListItems = new ArrayList<Category>();
	protected ReportItemAdapter mItemAdapter = null;
	
	private int mSelectedSection = 0;
	protected ReportSeparationAdapter mSeparationAdapter = null;
	
	protected ListView mSeparationList;
	
	private int mViewMode = VIEW_TWO_LINE; 
	
	private String []mMainCategoryName = {"자산", "부채", "카드"};
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.report_base, true);
       
    }
	
	@Override
	protected void onResume() {
		setSeparationAdapterList();
	
		getData();
        setAdapterList();
        updateChildView();
        
		super.onResume();
	}
	
	  @Override
	public void initialize() {
		mSeparationList = (ListView) findViewById(R.id.LVSeparation);
 //   	mSelectedCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_ID, -1);
    	
		if (mViewMode == VIEW_TWO_LINE) {
			findViewById(R.id.LVSeparation).setVisibility(View.VISIBLE);
	//		findViewById(R.id.LLListTitle).setVisibility(View.VISIBLE);
		}
		
		setMenuVisible(View.GONE);
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
  	
	protected ArrayList<String> getListItems() {
    	ArrayList<String> listItems = new ArrayList<String>();
    	Collections.addAll(listItems, mMainCategoryName);
    	return listItems;
    }
	
	protected void setViewMode(int viewMode) {
		mViewMode = viewMode;
	}
	
	@Override
	protected void setTitleBtn() {
        setTitle("자산 추가");
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
		getItemsFromDB();
	}
	
	protected void setAdapterList() {
        
    	final ListView listItem = (ListView)findViewById(R.id.LVCurrentList);
    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_input_categoyr_select, getCategoryListItems());
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mSelectedSection == SELECT_ASSETS) {
					if (position == 0) {
						Intent intent = new Intent(ReportInputAssetsCategoryLayout.this, InputAccountLayout.class);		
						startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ACCOUNT);
					}
					else {
						startInputAssetsActivity(mItemAdapter.getItem(position));
					}
				}
				else if (mSelectedSection == SELECT_LIABILITY) {
					startInputLiabilityActivity(mItemAdapter.getItem(position));
				}
				else if (mSelectedSection == SELECT_CARD) {
					startInputCardActivity(mItemAdapter.getItem(position));
				}
			}
		});
    }
	
	protected ArrayList<Category> getCategoryListItems() {
		if (mSelectedSection == SELECT_ASSETS) {
			mListItems.add(0, new Category(-1, "보통예금"));
		}
		return mListItems;
	}
	
	protected void startInputCardActivity(Category category) {
		Intent intent = null;
    	if (category.getID() == CardItem.CREDIT_CARD) {
			intent = new Intent(this, InputCreditCardLayout.class);
		}
    	else if (category.getID() == CardItem.CHECK_CARD) {
			intent = new Intent(this, InputCheckCardLayout.class);
		}
    	else if (category.getID() == CardItem.PREPAID_CARD) {
    		intent = new Intent(this, InputPrepaidCardLayout.class);
    	}
    	
    	startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_CARD);
	}
	
	protected void startInputLiabilityActivity(Category category) {
		Intent intent = null;
    	if (category.getExtndType() == ItemDef.ExtendLiablility.LOAN) {
			intent = new Intent(this, InputLiabilityLoanLayout.class);
		}
    	else if (category.getExtndType() == ItemDef.ExtendLiablility.CASH_SERVICE) {
			intent = new Intent(this, InputLiabilityCashServiceLayout.class);
		}
    	else if (category.getExtndType() == ItemDef.ExtendLiablility.PERSON_LOAN) {
			intent = new Intent(this, InputLiabilityPersonLoanLayout.class);
		}
    	else {
    		intent = new Intent(this, InputLiabilityLayout.class);
    	}
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_LIABLITY);
	}

	protected void startInputAssetsActivity(Category category) {
		Intent intent = null;
		if (category.getExtndType() == ItemDef.ExtendAssets.DEPOSIT) {
			intent = new Intent(this, InputAssetsDepositLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.SAVINGS) {
			intent = new Intent(this, InputAssetsSavingsLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.STOCK) {
			intent = new Intent(this, InputAssetsStockLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.FUND) {
			intent = new Intent(this, InputAssetsFundLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.ENDOWMENT_MORTGAGE) {
			intent = new Intent(this, InputAssetsInsuranceLayout.class);
		}
		else if (category.getExtndType() == ItemDef.ExtendAssets.REAL_ESTATE) {
			intent = new Intent(this, InputAssetsRealEstateLayout.class);
		}
		else {
			intent = new Intent(this, InputAssetsLayout.class);
		}
    	
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
		startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ASSETS);
	}
	
 

	protected void getItemsFromDB() {
		
		if (mSelectedSection == SELECT_ASSETS) {
			mListItems = DBMgr.getCategory(AssetsItem.TYPE);
		}
		else if (mSelectedSection == SELECT_LIABILITY) {
			mListItems = DBMgr.getCategory(LiabilityItem.TYPE);
		}
		else if (mSelectedSection == SELECT_CARD) {
			mListItems.clear();
			mListItems.add(new Category(CardItem.CREDIT_CARD, CardItem.getCardTypeName(CardItem.CREDIT_CARD)));
			mListItems.add(new Category(CardItem.CHECK_CARD, CardItem.getCardTypeName(CardItem.CHECK_CARD)));
			mListItems.add(new Category(CardItem.PREPAID_CARD, CardItem.getCardTypeName(CardItem.PREPAID_CARD)));
		}
    }
	

	public class ReportItemAdapter extends ArrayAdapter<Category> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportItemAdapter(Context context, int resource,
				 List<Category> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Category item = (Category)getItem(position);
			
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
//		
//		@Override
//		public boolean isEnabled(int position) {
//			return !mListItems.get(position).isSeparator();
//		}
    }
	
	protected void setListViewText(Category category, View convertView) {
		TextView tvCategoryName = (TextView) convertView.findViewById(R.id.TVCategoryName);
		tvCategoryName.setText(category.getName());
	}
	
	protected void updateChildView() {
		
	}
	
	public class ReportSeparationAdapter extends ArrayAdapter<String> {
		private int mResource;
    	private LayoutInflater mInflater;

		public ReportSeparationAdapter(Context context, int resource,
				 List<String> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String category = (String)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			TextView tvSection = (TextView) convertView.findViewById(R.id.TVSeparator);
			tvSection.setText(category);
			
			return convertView;
		}
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MsgDef.ActRequest.ACT_ADD_ASSETS ||requestCode == MsgDef.ActRequest.ACT_ADD_LIABLITY
			|| requestCode == MsgDef.ActRequest.ACT_ADD_ACCOUNT ||requestCode == MsgDef.ActRequest.ACT_ADD_CARD) {
			if (resultCode == RESULT_OK) {
				finish();
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}