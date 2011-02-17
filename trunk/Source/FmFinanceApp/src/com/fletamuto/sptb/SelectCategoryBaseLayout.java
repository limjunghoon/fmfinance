package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public abstract class SelectCategoryBaseLayout extends SelectGridBaseLayout {
	private static boolean mSelectSubCategory = true;
	protected ArrayList<Category> mArrCategory = null;
	CategoryButtonAdpter mAdapterCategory;
	private int mType = -1;
	
	//Adapter 내의 Sub Category 시작점과 갯수 저장을 위한 변수
	private int subCategoryStartPosition = -1;
	private int subCategoryCount = -1;
	
	public final static int ACT_SUB_CATEGORY = MsgDef.ActRequest.ACT_SUB_CATEGORY;
	public final static int ACT_EDIT_CATEGORY = MsgDef.ActRequest.ACT_EDIT_CATEGORY;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        mSelectSubCategory = getIntent().getBooleanExtra(MsgDef.ExtraNames.SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY, true);
    }
	
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectCategoryBaseLayout.this, EditCategoryLayout.class);
		intent.putExtra("CATEGORY_TYPE", mType);
		if (mType == ExpenseItem.TYPE || mType == AssetsItem.TYPE) {
			intent.putExtra("CATEGORY_HAS_SUB", true);
		}
		
		startActivityForResult(intent, ACT_EDIT_CATEGORY);
	}
	
	//Adapter 내의 Sub Category 시작점과 갯수를 set/get 하는 부분 Start
	public void setSubCategoryStartPosition (int startPosition) {
		subCategoryStartPosition = startPosition;
	}
	public int getSubCategoryStartPosition () {
		return subCategoryStartPosition;
	}
	
	public void setSubCategoryCount (int categoryCount) {
		subCategoryCount = categoryCount;
	}
	public int getSubCategoryCount () {
		return subCategoryCount;
	}
	//End
	
	@Override
	public void getData() {
		getCategoryList();
	}
	
	protected void getCategoryList() {
		if (mType == -1) {
			Log.e(LogTag.LAYOUT, "== invaild category type");
			return;
		}
		mArrCategory = DBMgr.getCategory(mType);
		
	}
	
	@Override
	public void setAdaper() {
		setCategoryAdaper();
	}
	
	protected void setCategoryAdaper() {
		if (mArrCategory == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterCategory = new CategoryButtonAdpter(this, R.layout.grid_select, mArrCategory);
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
		intent.putExtra("CATEGORY_ID", category.getID());
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
			
			//Sub Category 영역을 구분 하기 위한 부분
			if (position > getSubCategoryStartPosition()-1 && position < getSubCategoryStartPosition() + getSubCategoryCount()) {
				LinearLayout lLayout = (LinearLayout) convertView.findViewById (R.id.BtnGridItemLL);
				lLayout.setBackgroundColor(Color.BLACK);
			}
			
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			//Sub Category 빈공간 넣는 부분
			if (category == null) {
				button.setVisibility(Button.INVISIBLE);
				return convertView;
			}

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
	
	@Override
	protected void clearAdapter() {
		if (mAdapterCategory != null) {
			mAdapterCategory.clear();
		}
	}

	public static boolean isSelectSubCategory() {
		return mSelectSubCategory;
	}
}
