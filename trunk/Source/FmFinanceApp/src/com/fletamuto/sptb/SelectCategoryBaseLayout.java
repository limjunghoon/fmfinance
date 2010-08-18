package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.EditCategoryLayout.CategoryItemAdapter;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class SelectCategoryBaseLayout extends FmBaseActivity {
	protected ArrayList<Category> mArrCategory = null;
	CategoryButtonAdpter mAdapterCategory;
	private int mType = -1;
	
	public final static int ACT_SUB_CATEGORY = 1;
	public final static int ACT_EDIT_CATEGORY = 2;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.category_base_new, true);
        setTitleButtonListener();
        setEditButtonListener();
        setTitle(getResources().getString(R.string.btn_category_select));
        setTitleBtnVisibility(FmTitleLayout.BTN_RIGTH_01, View.VISIBLE);
    }
	
	
	protected void getCategoryList() {
		if (mType == -1) {
			Log.e(LogTag.LAYOUT, "== invaild category type");
			return;
		}
		mArrCategory = DBMgr.getInstance().getCategory(mType);
		setCategoryAdaper();
        
	}
	
	protected void setCategoryAdaper() {
		if (mArrCategory == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVCategory);
    	mAdapterCategory = new CategoryButtonAdpter(this, R.layout.category_grid, mArrCategory);
    	gridCategory.setAdapter(mAdapterCategory);
    	
	}
	
	protected void updateAdapterCategory() {
		if (mAdapterCategory != null) {
			mAdapterCategory.clear();
		}
		getCategoryList();
		setCategoryAdaper();
	}
	
	protected void onClickCategoryButton(Category category) {
		Intent intent = new Intent();
		intent.putExtra("CATEGORY_ID", category.getId());
		intent.putExtra("CATEGORY_NAME", category.getName());
		setResult(RESULT_OK, intent);
		finish();
	}

    View.OnClickListener categoryListener = new View.OnClickListener() {
		public void onClick(View v) {
			Category category = (Category)v.getTag();
			onClickCategoryButton(category);
		}
	};
	
	public void setEditButtonListener() {
		setTitleButtonListener(FmTitleLayout.BTN_RIGTH_01, new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(SelectCategoryBaseLayout.this, EditCategoryLayout.class);
				intent.putExtra("CATEGORY_TYPE", mType);
				if (mType == ExpenseItem.TYPE || mType == AssetsItem.TYPE) {
					intent.putExtra("CATEGORY_HAS_SUB", true);
				}
				
				startActivityForResult(intent, ACT_EDIT_CATEGORY);
			}
		});
	}
    
    public void setType(int type) {
		this.mType = type;
	}

	public int getType() {
		return mType;
	}
	private class CategoryButtonAdpter extends ArrayAdapter<Category> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CategoryButtonAdpter(Context context, int resource,
				 List<Category> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Category category = (Category)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			Button button = (Button)convertView.findViewById(R.id.BtnGridCategory);
			button.setText(category.getName());
			button.setOnClickListener(categoryListener);
			button.setTag(category);
			
			return convertView;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_EDIT_CATEGORY) {
			if (resultCode == RESULT_OK) {
			
					updateAdapterCategory();

    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
