package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fletamuto.common.control.fmgraph.PieGraph;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.FinanceItem;

public abstract class ReportBaseCompare extends FmBaseActivity {
	
	protected int mType;
	protected ArrayList<FinanceItem> mFinanceItems;
	protected long mTotalAmout = 0L;
	protected  Map<Integer, CategoryAmount> mCategoryAmount = new HashMap<Integer, CategoryAmount>();
	
	
	protected abstract void onClickCategoryButton(CategoryAmount categoryAmount);
	protected abstract void updateChildView();
	
	protected void setItemType(int itemType) {
		mType = itemType;
	}
	
	
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    }
    
    protected void getData() {
		updateMapCategory();
	}

	protected void updateBarGraph() {
		final PieGraph pieGraph;	
       
		pieGraph = (PieGraph) findViewById (R.id.pgraph);
		pieGraph.setItemValues(new long[] {100, 100});
		pieGraph.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    		pieGraph.FindTouchItemID((int)event.getX(), (int)event.getY());
		    		return true;
		    	}
				return false;
			}
		});
		
	}

	public void addButtonInLayout() {
		LinearLayout ll = (LinearLayout)findViewById(R.id.LLItemButtons);
		ll.removeAllViews();
		
		Collection<CategoryAmount> categoryAmountItems = mCategoryAmount.values();
		
		for (CategoryAmount iterator:categoryAmountItems) {
			Button btnItem = new Button(getApplicationContext());
			btnItem.setText(String.format("%s    : %,d¿ø", iterator.getName(), iterator.getTotalAmount()));
			btnItem.setOnClickListener(categoryListener);
			btnItem.setTag(iterator);
			ll.addView(btnItem);
		}
		ll.invalidate();
	}
	
    protected View.OnClickListener categoryListener = new View.OnClickListener() {
		public void onClick(View v) {
			CategoryAmount categoryAmount = (CategoryAmount)v.getTag();
			onClickCategoryButton(categoryAmount);
		}
	};
	
	public class CategoryAmount {
		private int mCategoryID;
		private long mTotalAmount;
		private String mName;
		
		public int getCategoryID() {
			return mCategoryID;
		}

		public long getTotalAmount() {
			return mTotalAmount;
		}
		public String getName() {
			return mName;
		}
		
		public void addAmount(long amount) {
			mTotalAmount += amount;
		}
		
		public void set(int id, String name, long amount) {
			mCategoryID = id;
			mName = name;
			mTotalAmount = amount;
		}
	}
	
	public void updateMapCategory() {
		mCategoryAmount.clear();
		
		int itemSize = mFinanceItems.size();
		for (int index = 0; index < itemSize; index++) {
			FinanceItem item = mFinanceItems.get(index);
			Category category = getCategory(item);
			if (category == null) {
				Log.w(LogTag.LAYOUT, ":: INVAILD CATEGORU :: ");
				continue;
			}
			Integer categoryID = category.getID();
			
			CategoryAmount categoryAmount = mCategoryAmount.get(categoryID);
			if (categoryAmount == null) {
				categoryAmount = new CategoryAmount();
				categoryAmount.set(categoryID, category.getName(), item.getAmount());
				mCategoryAmount.put(categoryID, categoryAmount);
			}
			else {
				categoryAmount.addAmount(item.getAmount());
			}
		}
	}
	

	
	public Category getCategory(FinanceItem item) {
		if (item == null) return null;
		return item.getCategory();
	}
	


}