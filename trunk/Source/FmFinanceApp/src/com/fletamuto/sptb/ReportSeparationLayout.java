package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.CategoryAmount;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public abstract class ReportSeparationLayout extends ReportBaseLayout {
	private int mSelectedSection = 0;
	private int mSelectedCategoryID = -1;
	protected Map<Integer, CategoryAmount> mCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ReportSeparationAdapter mSeparationAdapter = null;
	
	protected ListView mSeparationList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
    

    
	@Override
	protected void onResume() {
		getSeparationData();
		setSeparationAdapterList();
	
		setSelectedPosition();
		super.onResume();
	}
	
	@Override
	public void initialize() {
		mSeparationList = (ListView) findViewById(R.id.LVSeparation);
		setViewMode(VIEW_TWO_LINE);
		mSelectedCategoryID = getIntent().getIntExtra(MsgDef.ExtraNames.ITEM_ID, -1);
		super.initialize();
	}
	
	protected int getSelectedCategoryID() {
		return mSelectedCategoryID;
	}
	
	protected void setSelectedPosition() {
		if (mSeparationAdapter == null) {
			return;
		}
		
    	int size = mSeparationAdapter.getCount();
    	for (int index = 0; index < size; index++) {
    		CategoryAmount categoryAmount = mSeparationAdapter.getItem(index);
    		if (categoryAmount.getCategoryID() == mSelectedCategoryID) {
    			mSelectedSection = index;
    		}
    	}
	}


	@Override
	protected void getData() {
		if (mSeparationAdapter == null || mSeparationAdapter.getCount() == 0) 
			return;
		
		CategoryAmount categoryAmount = mSeparationAdapter.getItem(mSelectedSection);
		mCategoryID = categoryAmount.getCategoryID();
		mSelectedCategoryID = mCategoryID;
		
		((TextView)findViewById(R.id.TVListTitleRight)).setText(String.format("ÃÑ ±Ý¾× %,d¿ø", categoryAmount.getTotalAmount()));
		
		super.getData();
	}
	

    
  
    
	protected void getSeparationData() {
		updateListItem(DBMgr.getAllItems(getItemType()));
	}
	
	protected void updateListItem(ArrayList<FinanceItem> arrItems) {
		mCategoryItems.clear();
		
		int itemSize = arrItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = arrItems.get(index);
			Category category = item.getCategory();
			if (category == null) {
				Log.w(LogTag.LAYOUT, ":: INVAILD CATEGORU :: ");
				continue;
			}
			
			Integer categoryID = category.getID();
			CategoryAmount categoryAmount = mCategoryItems.get(categoryID);
			
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount(item.getType());
				categoryAmount.set(categoryID, category.getName(), item.getAmount());
				mCategoryItems.put(categoryID, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getAmount());
			}
		}
	}
	
	protected ArrayList<CategoryAmount> getListItems() {
    	ArrayList<CategoryAmount> listItems = new ArrayList<CategoryAmount>();
    	Collection<CategoryAmount> categoryAmountItems = mCategoryItems.values();
    	
    	for (CategoryAmount iterator:categoryAmountItems) {
			listItems.add(iterator);
		}
    	
    	return listItems;
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