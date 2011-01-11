package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fletamuto.sptb.ReportBaseCompare.CategoryAmount;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.FinanceCurrentDate;
import com.fletamuto.sptb.util.LogTag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainAssetsLayout extends FmBaseActivity {
	protected ArrayList<FinanceItem> mAssetsItems = null;
	protected Map<Integer, CategoryAmount> mAssetsCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ArrayList<FinanceItem> mLiabilityItems = null;
	protected Map<Integer, CategoryAmount> mLiabilityCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ReportItemAdapter mItemAdapter = null;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.main_asserts);
    	setButtonClickListener();
    	
    	getListItem();
    	setAdapterList();
    }
    
    @Override
    protected void onResume() {
    	getListItem();
    	setAdapterList();
    	
    	super.onResume();
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
    	mAssetsItems = DBMgr.getItems(IncomeItem.TYPE, FinanceCurrentDate.getDate());
    	mLiabilityItems = DBMgr.getItems(ExpenseItem.TYPE, FinanceCurrentDate.getDate());
    	
    	updateListItem();
    }
    
    protected void setAdapterList() {
    	Collection<CategoryAmount> categoryAmountItems = mAssetsCategoryItems.values();
    	ArrayList<CategoryAmount> assetsItems = new ArrayList<CategoryAmount>();
		
		for (CategoryAmount iterator:categoryAmountItems) {
			assetsItems.add(iterator);
		}
        
    	final ListView listItem = (ListView)findViewById(R.id.LVAssetsCategory);
    	mItemAdapter = new ReportItemAdapter(this, R.layout.report_list_assets_category, assetsItems);
    	listItem.setAdapter(mItemAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				onClickListItem(parent, view, position, id);
			}
		});
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
			
//			
			return convertView;
		}
    }
    
    protected void updateListItem() {
    	mAssetsCategoryItems.clear();
    	
    	mAssetsItems = DBMgr.getAllItems(AssetsItem.TYPE);
		if (mAssetsItems == null) return;
		
		int itemSize = mAssetsItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mAssetsItems.get(index);
			Category category = item.getCategory();
			if (category == null) {
				Log.w(LogTag.LAYOUT, ":: INVAILD CATEGORU :: ");
				continue;
			}
			Integer categoryID = category.getID();
			
			CategoryAmount categoryAmount = mAssetsCategoryItems.get(categoryID);
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount();
				categoryAmount.set(categoryID, category.getName(), item.getAmount());
				mAssetsCategoryItems.put(categoryID, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getAmount());
			}
		}
	}
    
	public class CategoryAmount {
		private int mCategoryID;
		private long mTotalAmount;
		private String mName;
		private int mCount = 1;
		
		public int getCategoryID() {
			return mCategoryID;
		}

		public long getTotalAmount() {
			return mTotalAmount;
		}
		public String getName() {
			return mName;
		}
		
		public int getCount() {
			return mCount;
		}
		
		public void addAmount(long amount) {
			mTotalAmount += amount;
			mCount++;
		}
		
		public void set(int id, String name, long amount) {
			mCategoryID = id;
			mName = name;
			mTotalAmount = amount;
		}
	}
}