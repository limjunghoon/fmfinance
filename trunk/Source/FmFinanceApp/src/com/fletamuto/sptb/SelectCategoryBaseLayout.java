package com.fletamuto.sptb;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;

public abstract class SelectCategoryBaseLayout extends FmBaseActivity {
	private ArrayList<Category> mArrCategory = null;
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
		if (mArrCategory == null) return;
        GridView grid = (GridView)findViewById(R.id.GVCategory);
        CategoryButtonAdpter adapter = new CategoryButtonAdpter();
    	grid.setAdapter(adapter);
	}
	
	protected void onClickCategoryButton(CategoryButton btn) {
		Intent intent = new Intent();
		intent.putExtra("CATEGORY_ID", btn.getCategoryID());
		intent.putExtra("CATEGORY_NAME", btn.getCategoryName());
		setResult(RESULT_OK, intent);
		finish();
	}

    View.OnClickListener categoryListener = new View.OnClickListener() {
		public void onClick(View v) {
			onClickCategoryButton((CategoryButton)v);
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

	private class CategoryButtonAdpter extends BaseAdapter {
  			public int getCount() {
  				// TODO Auto-generated method stub
  				return mArrCategory.size();
  			}

  			public Object getItem(int arg0) {
  				// TODO Auto-generated method stub
  				return null;
  			}

  			public long getItemId(int arg0) {
  				// TODO Auto-generated method stub
  				return 0;
  			}

  			public View getView(int position, View convertView, ViewGroup arg2) {
  				// TODO Auto-generated method stub
  				Button btnCategory;
  				if (convertView == null) {
  					Category category = mArrCategory.get(position);
  					btnCategory = new CategoryButton(SelectCategoryBaseLayout.this, category);
  					btnCategory.setText(category.getName());
  				}
  				else {
  					btnCategory = (Button)convertView;
  				}
  				
  				btnCategory.setOnClickListener(categoryListener);
  				return btnCategory;
  			}
    }
}
