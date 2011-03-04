package com.fletamuto.sptb;



import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public class SelectCategoryExpenseLayout extends SelectCategoryBaseLayout {

	private Category mMainCategory;
	
	final static int ACT_EDIT_CATEGORY = 0;
	final static int ACT_ADD_CATEGORY = 1;

	
	//Sub Category �� ��� ������ ArrayList
	ArrayList<Category> subCategory = null;
	//���� currentMainCagegoryID
	int currentMainCagegoryID = -1;
	//���� category ID
	Category dCategory = null;

	
	public SelectCategoryExpenseLayout() {
		setType(ExpenseItem.TYPE);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //���� ��ư ó��
        getGridRightBtn().setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setEditCategoryMode(true);	
				setCategoryAdaper();
			}
		});
    }
    
    protected void onClickCategoryButton(Category category) {
    	
    	//sub category Add ��ư �߰� �� ����
    	Category addButton = new Category(-3, "+");
    	
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
    			/*
    			mArrCategory.removeAll(subCategory);
    			setSubCategoryCount(-1);
    			setSubCategoryStartPosition(-1);
    			currentMainCagegoryID = -1;
    			subCategory = null;
    			setSubCategoryMode(false);
    			setCategoryAdaper();
    			return;
    			*/
    			Intent intent = new Intent();
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
				intent.putExtra("SUB_CATEGORY_ID", /*category.getID()*/-1);
				intent.putExtra("SUB_CATEGORY_NAME", /*category.getName()*/"");
				setResult(RESULT_OK, intent);
				setSubCategoryMode(false);
			
				finish();
				return;
    		}    		
    		
    		//SubCategory �� �ִ� ��Ȳ
    		if (getSubCategoryCount() > 0) {
    			//SubCategory �� ���� ���� ��
    			if (subCategory.contains(category)) {
    				if (category.getID() == -3) {
    					//Sub Category Add ��ư ������ �� ó��
    	    			Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    	    			intent.putExtra("EDIT_TYPE", type);
    	    			intent.putExtra("MAIN_CATEGORY_ID", mMainCategory.getID());
						intent.putExtra("MAIN_CATEGORY_NAME", mMainCategory.getName());
    					intent.putExtra("EDIT_TITLE", "�з� �߰�");
    					intent.putExtra("EDIT_MODE", "SUB_CATEGORY_ADD");

    					startActivityForResult(intent, ACT_ADD_CATEGORY);
    					return;
    				} else {
    					if (getEditCategoryMode()) {
    						//============���⿡ ���� ȭ������ �Ѿ�� �� ���� �ؾ� �� (Sub �׸� �κ�)
    						Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    						intent.putExtra("EDIT_TYPE", type);
    						intent.putExtra("CATEGORY_ID", category.getID());
    						intent.putExtra("CATEGORY_NAME", category.getName());
    						intent.putExtra("MAIN_CATEGORY_ID", mMainCategory.getID());
    						intent.putExtra("MAIN_CATEGORY_NAME", mMainCategory.getName());
    						intent.putExtra("CATEGORY_IMAGE_INDEX", category.getImageIndex());
    						intent.putExtra("EDIT_TITLE", "�з� ����");
    						intent.putExtra("EDIT_MODE", "SUB_CATEGORY_EDIT");

    						startActivityForResult(intent, ACT_EDIT_CATEGORY);
    						
    						return;
    					} else {
    						Intent intent = new Intent();
    						intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, mMainCategory.getID());
    						intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, mMainCategory.getName());
    						intent.putExtra("SUB_CATEGORY_ID", category.getID());
    						intent.putExtra("SUB_CATEGORY_NAME", category.getName());
    						setResult(RESULT_OK, intent);
    						setSubCategoryMode(false);
        				
    						finish();
    						return;
    					}
    				}
    				
    			}
    			mArrCategory.removeAll(subCategory);    			
    		}
    		
    		//Main Category Add ��ư ������ �� ó��
    		if (category.getID() == -2) {
    			Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    			intent.putExtra("EDIT_TYPE", type);
				intent.putExtra("EDIT_TITLE", "�з� �߰�");
				intent.putExtra("EDIT_MODE", "MAIN_CATEGORY_ADD");

				startActivityForResult(intent, ACT_ADD_CATEGORY);
				return;
    		}
    		
    		//Main Category ���� ȭ������ �Ѿ� ���� �κ�
    		if (getEditCategoryMode() && getSubCategoryMode() == false) {
    			//============���⿡ ���� ȭ������ �Ѿ�� �� ���� �ؾ� �� (Main �׸� �κ�)
    			Intent intent = new Intent(SelectCategoryExpenseLayout.this, NewEditCategoryLayout.class);
    			intent.putExtra("EDIT_TYPE", type);
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getID());
				intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
				intent.putExtra("CATEGORY_IMAGE_INDEX", category.getImageIndex());
				intent.putExtra("EDIT_TITLE", "�з� ����");
				intent.putExtra("EDIT_MODE", "MAIN_CATEGORY_EDIT");

				startActivityForResult(intent, ACT_EDIT_CATEGORY);
				return;
    		}
    		
    		mMainCategory = category;
    		currentMainCagegoryID = mainCagegoryID;
    		
    		//DB ���� Sub Category �������� ���� �κ�
    		subCategory = DBMgr.getSubCategory(type, mainCagegoryID);

    		//subCategory �������� add ��ư �߰�
    		subCategory.add(addButton);
    		setSubCategoryCount(subCategory.size());    		

    		//Sub Category �߰� �� ���� Grid ĭ�� ����� �ֱ� ���� �κ�
    		if (getSubCategoryCount() % 4 != 0) {
    			for (int i=0; i < 4 - (getSubCategoryCount() % 4); i++) {
    				subCategory.add(null);
    			}
    			setSubCategoryCount(subCategory.size());
    		}

    		//Sub Category ��ġ ��� �ϴ� �κ�
    		subCategoryStartPos = ((/*mainCagegoryID*/mArrCategory.indexOf(mMainCategory))/4+1) * 4;
    		if (subCategoryStartPos >= mArrCategory.size()) {
    			subCategoryStartPos = subCategoryStartPos - 4;
    		}    		
    		setSubCategoryStartPosition(subCategoryStartPos);    		

    		//Adapter �� ���� ��ġ��  Sub category �߰� �ϴ� �κ� 
    		mArrCategory.addAll(getSubCategoryStartPosition(), subCategory);
    		setSubCategoryMode(true);

    		setCategoryAdaper();
    	}
    	else {
    		super.onClickCategoryButton(category);
    	}
	}
    
    protected void onClickDeleteCategoryButton(Category category) {
    	
    	dCategory = category;
    	    	
    	new AlertDialog.Builder(SelectCategoryExpenseLayout.this)
    	.setMessage("���� �Ͻðڽ��ϱ�?")
    	.setPositiveButton("����", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				if (getSubCategoryMode()) {
					if (DBMgr.deleteSubCategory(ExpenseItem.TYPE, dCategory.getID()) == 0) {
						return;
					}
					
					mArrCategory.removeAll(subCategory);
					
					//Adapter�� Sub �з��� ���� �� ��ġ �� 
			    	int subCategoryStartPos = 0;
					//sub category Add ��ư �߰� �� ����
			    	Category addButton = new Category(-3, "+");
					//DB ���� Sub Category �������� ���� �κ�
		    		subCategory = DBMgr.getSubCategory(ExpenseItem.TYPE, currentMainCagegoryID);
		    		//subCategory �������� add ��ư �߰�
		    		subCategory.add(addButton);
		    		setSubCategoryCount(subCategory.size());    		
		    		
		    		//Sub Category �߰� �� ���� Grid ĭ�� ����� �ֱ� ���� �κ�
		    		if (getSubCategoryCount() % 4 != 0) {
		    			for (int i=0; i < 4 - (getSubCategoryCount() % 4); i++) {
		    				subCategory.add(null);
		    			}
		    			setSubCategoryCount(subCategory.size());
		    		}
		    		
		    		//Sub Category ��ġ ��� �ϴ� �κ�
		    		subCategoryStartPos = ((/*currentMainCagegoryID*/mArrCategory.indexOf(mMainCategory))/4+1) * 4;
		    		if (subCategoryStartPos >= mArrCategory.size()) {
		    			subCategoryStartPos = subCategoryStartPos - 4;
		    		}    		
		    		setSubCategoryStartPosition(subCategoryStartPos);    		
		    		
		    		//Adapter �� ���� ��ġ��  Sub category �߰� �ϴ� �κ� 
		    		mArrCategory.addAll(getSubCategoryStartPosition(), subCategory);
		    		setSubCategoryMode(true);					
					
				}
				else {
					if (DBMgr.deleteCategory(ExpenseItem.TYPE, dCategory.getID()) == 0) {
						return;
					}
					mArrCategory.remove(dCategory);
				}				 
				
				setCategoryAdaper();				
			}
		})
		.setNegativeButton("���", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();   	
    }
    
  //Back Ű ó��
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		if (getEditCategoryMode()) {
    			setEditCategoryMode(false);
    			setCategoryAdaper();
    		} else if (getSubCategoryCount() > 0) {
    			mArrCategory.removeAll(subCategory);
    			setSubCategoryCount(-1);
    			setSubCategoryStartPosition(-1);
    			currentMainCagegoryID = -1;
    			subCategory = null;
    			setSubCategoryMode(false);
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
    	} else if (requestCode == ACT_EDIT_CATEGORY || requestCode == ACT_ADD_CATEGORY) {
    		if (resultCode == RESULT_OK) {

    			if (getSubCategoryMode()) {

    				mArrCategory.removeAll(subCategory);
					
					//Adapter�� Sub �з��� ���� �� ��ġ �� 
			    	int subCategoryStartPos = 0;
					//sub category Add ��ư �߰� �� ����
			    	Category addButton = new Category(-3, "+");
					//DB ���� Sub Category �������� ���� �κ�
		    		subCategory = DBMgr.getSubCategory(ExpenseItem.TYPE, currentMainCagegoryID);
		    		//subCategory �������� add ��ư �߰�
		    		subCategory.add(addButton);
		    		setSubCategoryCount(subCategory.size());    		
		    		
		    		//Sub Category �߰� �� ���� Grid ĭ�� ����� �ֱ� ���� �κ�
		    		if (getSubCategoryCount() % 4 != 0) {
		    			for (int i=0; i < 4 - (getSubCategoryCount() % 4); i++) {
		    				subCategory.add(null);
		    			}
		    			setSubCategoryCount(subCategory.size());
		    		}
		    		
		    		//Sub Category ��ġ ��� �ϴ� �κ�
		    		subCategoryStartPos = ((/*currentMainCagegoryID*/mArrCategory.indexOf(mMainCategory))/4+1) * 4;
		    		if (subCategoryStartPos >= mArrCategory.size()) {
		    			subCategoryStartPos = subCategoryStartPos - 4;
		    		}    		
		    		setSubCategoryStartPosition(subCategoryStartPos);    		
		    		
		    		//Adapter �� ���� ��ġ��  Sub category �߰� �ϴ� �κ� 
		    		mArrCategory.addAll(getSubCategoryStartPosition(), subCategory);
		    		setSubCategoryMode(true);
		    		
		    		setCategoryAdaper();
    			} else {

    				updateAdapterCategory();
    			}   			
    		}
    		return;
    	} 
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
