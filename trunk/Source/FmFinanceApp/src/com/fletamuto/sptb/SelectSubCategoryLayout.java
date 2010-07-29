package com.fletamuto.sptb;

import java.util.ArrayList;

import com.fletamuto.sptb.SelectCategoryBaseLayout.CategoryButtonAdpter;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.db.DBMgr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class SelectSubCategoryLayout extends Activity {
	private int mainCagegoryId;
	private int type;
	ArrayList<Category> arrCategory = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_base_new);
        
        mainCagegoryId = getIntent().getIntExtra("MAIN_CATEGORY_ID", 0) ;
        type = getIntent().getIntExtra("ITEM_TYPE", 0) ;
        
        getSubCategoryList();
    }
    
    protected void getSubCategoryList() {
		arrCategory = DBMgr.getInstance().getSubCategory(type, mainCagegoryId);
		if (arrCategory == null) return;
        GridView grid = (GridView)findViewById(R.id.GVCategory);
        CategoryButtonAdpter adapter = new CategoryButtonAdpter();
    	grid.setAdapter(adapter);
	}
    
    protected void onClickCategoryButton(CategoryButton btn) {
		Intent intent = new Intent();
		intent.putExtra("SUB_CATEGORY_ID", btn.getCategoryID());
		intent.putExtra("SUB_CATEGORY_NAME", btn.getCategoryName());
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
  					btnCategory = new CategoryButton(SelectSubCategoryLayout.this, category);
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
