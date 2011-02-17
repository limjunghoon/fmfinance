package com.fletamuto.sptb;



import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class SelectCategoryExpenseLayout extends SelectCategoryBaseLayout {

	private Category mMainCategory;

	
	//Sub Category �� ��� ������ ArrayList
	ArrayList<Category> subCategory = null;
	//���� currentMainCagegoryID
	int currentMainCagegoryID = -1;

	
	public SelectCategoryExpenseLayout() {
		setType(ExpenseItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
    
    
    protected void onClickCategoryButton(Category category) {
    	
    	//���õ� ���� �з� ID
    	int mainCagegoryID = -1;
    	//���õ� ���� �з� Type
    	int type = -1;
    	//Adapter�� Sub �з��� ���� �� ��ġ �� 
    	int subCategoryStartPos = 0;
    	
    	if (isSelectSubCategory()) {
/*
    		Intent intent = new Intent(SelectCategoryExpenseLayout.this, SelectSubCategoryLayout.class);
        	intent.putExtra("MAIN_CATEGORY_ID", category.getID());
        	intent.putExtra("MAIN_CATEGORY_NAME", category.getName());
        	intent.putExtra("ITEM_TYPE", ExpenseItem.TYPE);
        	startActivityForResult(intent, ACT_SUB_CATEGORY);
*/

    		mainCagegoryID = category.getID();
    		type = ExpenseItem.TYPE;
    		
    		if (type == -1 || mainCagegoryID == -1) {
    			Log.e(LogTag.LAYOUT, "== invaild category type");
    			return;
    		}
    		
    		//Main Category �ڱ� �ڽ��� ������ �� �ٽ� ������� �ϴ� �κ�
    		if (currentMainCagegoryID == category.getID() && getSubCategoryCount() > 0 && subCategory.contains(category) == false) {
    			mArrCategory.removeAll(subCategory);
    			setSubCategoryCount(-1);
    			setSubCategoryStartPosition(-1);
    			currentMainCagegoryID = -1;
    			subCategory = null;
    			setCategoryAdaper();
    			return;
    		}    		
    		
    		//SubCategory �� �ִ� ��Ȳ
    		if (getSubCategoryCount() > 0) {
    			//SubCategory �� ���� ���� ��
    			if (subCategory.contains(category)) {
    				Intent intent = new Intent();
    				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
    				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
    				intent.putExtra("SUB_CATEGORY_ID", category.getID());
    				intent.putExtra("SUB_CATEGORY_NAME", category.getName());
    				setResult(RESULT_OK, intent);
    				
        			finish();
        			return;
    			}
    			mArrCategory.removeAll(subCategory);    			
    		}
    		
    		mMainCategory = category;
    		currentMainCagegoryID = mainCagegoryID;
    		
    		//DB ���� Sub Category �������� ���� �κ�
    		subCategory = DBMgr.getSubCategory(type, mainCagegoryID);
    		setSubCategoryCount(subCategory.size());    		
    		
    		//Sub Category �߰� �� ���� Grid ĭ�� ����� �ֱ� ���� �κ�
    		if (getSubCategoryCount() % 4 != 0) {
    			for (int i=0; i < 4 - (getSubCategoryCount() % 4); i++) {
    				subCategory.add(null);
    			}
    			setSubCategoryCount(subCategory.size());
    		}
    		
    		//Sub Category ��ġ ��� �ϴ� �κ�
    		subCategoryStartPos = ((mainCagegoryID-1)/4+1) * 4;
    		if (subCategoryStartPos >= mArrCategory.size()) {
    			subCategoryStartPos = subCategoryStartPos - 4;
    		}    		
    		setSubCategoryStartPosition(subCategoryStartPos);    		
    		
    		//Adapter �� ���� ��ġ��  Sub category �߰� �ϴ� �κ� 
    		mArrCategory.addAll(getSubCategoryStartPosition(), subCategory);
    		setCategoryAdaper();
    	}
    	else {
    		super.onClickCategoryButton(category);
    	}
	}
    
  //Back Ű ó��
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		if (getSubCategoryCount() > 0) {
    			mArrCategory.removeAll(subCategory);
    			setSubCategoryCount(-1);
    			setSubCategoryStartPosition(-1);
    			currentMainCagegoryID = -1;
    			subCategory = null;
    			setCategoryAdaper();
    		} else {
    			disAppearAnimation();
    			setResult(RESULT_CANCELED);
    		}
    	}
      
    	return true;
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_SUB_CATEGORY) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
				intent.putExtra("SUB_CATEGORY_ID", data.getIntExtra("SUB_CATEGORY_ID", -1));
				intent.putExtra("SUB_CATEGORY_NAME", data.getStringExtra("SUB_CATEGORY_NAME"));
				setResult(RESULT_OK, intent);
    			finish();
    			return;
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
