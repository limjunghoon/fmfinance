package com.fletamuto.sptb;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.db.DBMgr;

public abstract class SelectCategoryBaseLayout extends Activity {
	ArrayList<Category> arrCategory = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.category_base_new);
    }
	
	protected abstract void getCategoryList();
	
	protected void getCategoryList(int type) {
		arrCategory = DBMgr.getInstance().getCategory(type);
		if (arrCategory == null) return;
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
    
    class CategoryButtonAdpter extends BaseAdapter {
  			public int getCount() {
  				// TODO Auto-generated method stub
  				return arrCategory.size();
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
  					Category category = arrCategory.get(position);
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
