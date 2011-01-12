package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
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
	protected Map<Integer, CategoryAmount> mCategoryItems = new HashMap<Integer, CategoryAmount>();
	protected ReportSeparationAdapter mSeparationAdapter = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
    
	@Override
	protected void onResume() {
		getSeparationData();
		setSeparationAdapterList();
		
		super.onResume();
	}
	
	@Override
	protected void getData() {
		if (mSeparationAdapter == null) return;		
		CategoryAmount categoryAmount = mSeparationAdapter.getItem(mSelectedSection);
		mCategoryID = categoryAmount.getCategoryID();
		
		((TextView)findViewById(R.id.TVListTitleRight)).setText(String.format("ÃÑ ±Ý¾× %,d¿ø", categoryAmount.getTotalAmount()));
		
		super.getData();
	}
	

    
    @Override
    public void initialize() {
    	setViewMode(VIEW_TWO_LINE);
    	super.initialize();
    }
    
	protected void getSelectedDate() {
		if (mCategoryItems.size() == 0 || mCategoryItems.size() > mSelectedSection) return;
		
		CategoryAmount categoryAmount = mSeparationAdapter.getItem(mSelectedSection);
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
				categoryAmount = new CategoryAmount();
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

    	final ListView listItem = (ListView)findViewById(R.id.LVSeparation);
    	mSeparationAdapter = new ReportSeparationAdapter(this, R.layout.report_list_separation, getListItems());
    	listItem.setAdapter(mSeparationAdapter);
    	
    	listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
			
			return convertView;
		}
		
		
    }
	
	
}