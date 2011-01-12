package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.LiabilityItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class MainAssetsLayout extends FmBaseActivity {
	protected ArrayList<FinanceItem> mAssetsItems = null;
	protected Map<Integer, CategoryAmount> mAssetsCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ArrayList<FinanceItem> mLiabilityItems = null;
	protected Map<Integer, CategoryAmount> mLiabilityCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ReportItemAdapter mItemAdapter = null;
	
	private long mTotalAssetsAmount;
	private long mTotalLiabilityAmount;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main_asserts, false);
    	setButtonClickListener();
    	
    	getListItem();
    	setAdapterList();
    	updateChildView();
    }

	@Override
    protected void onResume() {
//    	getListItem();
//    	setAdapterList();
    	
    	super.onResume();
    }
	
	protected void updateChildView() {
		TextView tvTotalAssets = (TextView) findViewById(R.id.TVAssetsListAmount);
		tvTotalAssets.setText(String.format("%,d¿ø", mTotalAssetsAmount));
		
		TextView tvTotalLiability = (TextView) findViewById(R.id.TVLiabilityListAmount);
		tvTotalLiability.setText(String.format("%,d¿ø", mTotalLiabilityAmount));
		
		updateAssetsPorgress();
	}
    
    protected void setButtonClickListener() {
		
		Button btnExpense = (Button)findViewById(R.id.BtnAssets);
		btnExpense.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainAssetsLayout.this, ReportAssetsLayout.class);
				startActivity(intent);
			}
		});
		
		Button btnIncome = (Button)findViewById(R.id.BtnLiability);
		btnIncome.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MainAssetsLayout.this, ReportLiabilityLayout.class);
				startActivity(intent);
			}
		});
	}
    
    protected void getListItem() {
    	clearTotalCount();
    	mAssetsItems = DBMgr.getAllItems(AssetsItem.TYPE);
    	updateListItem(mAssetsCategoryItems, mAssetsItems);
    	mLiabilityItems = DBMgr.getAllItems(LiabilityItem.TYPE);
    	updateListItem(mLiabilityCategoryItems, mLiabilityItems);
    }
    
    protected void setAdapterList() {
    	setAdapterAssetsList();
    	setAdapterLiabilityList();
    }
    
    protected void setAdapterLiabilityList() {
    	final ListView listItem = (ListView)findViewById(R.id.LVLiabilityCategory);
    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_assets_category, getListItems(LiabilityItem.TYPE));
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				onClickListItem(parent, view, position, id);
			}
		});
	}

	protected void setAdapterAssetsList() {
    	final ListView listItem = (ListView)findViewById(R.id.LVAssetsCategory);
    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_assets_category, getListItems(AssetsItem.TYPE));
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				onClickListItem(parent, view, position, id);
			}
		});
	}

	protected ArrayList<CategoryAmount> getListItems(int type) {
    	ArrayList<CategoryAmount> listItems = new ArrayList<CategoryAmount>();
    	Collection<CategoryAmount> categoryAmountItems = null;
    	
    	if (type == AssetsItem.TYPE) {
    		categoryAmountItems = mAssetsCategoryItems.values();
    	}
    	else if (type == LiabilityItem.TYPE) {
    		categoryAmountItems = mLiabilityCategoryItems.values();
    	}
    	else {
    		return null;
    	}
    	
    	for (CategoryAmount iterator:categoryAmountItems) {
			listItems.add(iterator);
		}
    	
    	return listItems;
    }
    
    public class ReportItemAdapter extends ArrayAdapter<CategoryAmount> {
    	private LayoutInflater mInflater;
    	private int mResource;

		public ReportItemAdapter(Context context, int resource,
				 List<CategoryAmount> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CategoryAmount item = (CategoryAmount)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			((TextView)convertView.findViewById(R.id.TVAssetsCategoryName)).setText(String.format("%s(%d)", item.getName(), item.getCount()));
    		((TextView)convertView.findViewById(R.id.TVAssetsCategoryTotalAmout)).setText(String.format("%,d¿ø", item.getTotalAmount()));			
			return convertView;
		}
    }
    
    protected void updateListItem(Map<Integer, CategoryAmount> mapCategoryItems, ArrayList<FinanceItem> arrItems) {
    	mapCategoryItems.clear();
		
		int itemSize = arrItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = arrItems.get(index);
			Category category = item.getCategory();
			if (category == null) {
				Log.w(LogTag.LAYOUT, ":: INVAILD CATEGORU :: ");
				continue;
			}
			
			Integer categoryID = category.getID();
			CategoryAmount categoryAmount = mapCategoryItems.get(categoryID);
			
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount();
				categoryAmount.set(categoryID, category.getName(), item.getAmount());
				mapCategoryItems.put(categoryID, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getAmount());
			}
			
			addTotalCount(item);
		}
	}
    
    protected void clearTotalCount() {
    	mTotalAssetsAmount = 0L;
    	mTotalLiabilityAmount = 0L;
    }
    
	protected void addTotalCount(FinanceItem item) {
		if (item.getType() == AssetsItem.TYPE) {
			mTotalAssetsAmount += item.getAmount();
		}
		else if (item.getType() == LiabilityItem.TYPE) {
			mTotalLiabilityAmount += item.getAmount();
		}
	}


	private void updateAssetsPorgress() {
		ProgressBar progress = (ProgressBar)findViewById(R.id.AssetsLiabilityPrograss);

		int max = (int)((mTotalAssetsAmount + mTotalLiabilityAmount)/100);
		int pos = (int)(mTotalAssetsAmount/100);
		
		progress.setMax(max);
		progress.setProgress(pos);
		
	}
}